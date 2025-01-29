package com.qyp.chat.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.RedisConstant;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.*;
import com.qyp.chat.domain.enums.*;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.query.UserRegisterQuery;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.*;
import com.qyp.chat.service.IContactService;
import com.qyp.chat.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.StringUtils;
import com.qyp.chat.util.UserUtils;
import com.qyp.chat.websocket.ChannelContextUtils;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final RedisUtils redisUtils;

    private final AppConfig appConfig;


    private final ContactMapper contactMapper;
    private final SessionMapper sessionMapper;
    private final UserSessionMapper userSessionMapper;
    private final MessageMapper messageMapper;


    private final UserUtils userUtils;
    private final StringUtils stringUtils;

    private final ChannelContextUtils channelContextUtils;

    @Override
    @Transactional
    public void register(UserRegisterQuery user){

        String email = user.getEmail();
        User one = lambdaQuery().eq(User::getEmail, email).one();
        if(one != null)
            throw  new BusinessException("已经注册过用户！");
        //添加用户
        User user1 = new User();
        user1.setUserId(createUserId());
        user1.setEmail(user.getEmail());
        //密码加密
        user1.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user1.setNickName(user.getNickName());
        user1.setStatus(UserStatusEnum.ENABLE.getStatus());
        user1.setJoinType(JoinTypeEnum.APPLY.getType());
        user1.setCreateTime(LocalDateTime.now());
        user1.setLastOffTime(System.currentTimeMillis());
        save(user1);
        //创建机器人好友
        addContact4Robot(user1.getUserId());

    }

    private void addContact4Robot(String userId) {
        SysSettingDTO sysSetting = userUtils.getSysSetting();
        String contactId = sysSetting.getRobotUid();
        String contactName = sysSetting.getRobotNickName();
        String sendMessage = sysSetting.getRobotWelcome();
        sendMessage = stringUtils.cleanHtml(sendMessage);
        LocalDateTime time = LocalDateTime.now();
        //添加联系人
        Contact contact = new Contact();
        contact.setUserId(userId);
        contact.setContactId(contactId);
        contact.setContactType(ContactTypeEnum.USER.getType());
        contact.setCreateTime(time);
        contact.setStatus(ContactStatusEnum.FRIEND.getStatus());
        contact.setLastUpdateTime(time);
        contactMapper.insert(contact);
        //添加会话
        Session session = new Session();
        String sessionId = stringUtils.createSession(userId, contactId);
        session.setSessionId(sessionId);
        session.setLastMessage(sendMessage);
        session.setLastReceiveTime(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        sessionMapper.insert(session);
        //添加用户会话
        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setContactId(contactId);
        userSession.setSessionId(sessionId);
        userSession.setContactName(contactName);
        userSessionMapper.insert(userSession);
        //添加信息
        Message message = new Message();
        message.setSessionId(sessionId);
        message.setSendUserId(contactId);
        message.setSendTime(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        message.setContactId(userId);
        message.setContactType(ContactTypeEnum.USER.getType());
        message.setMessageContent(sendMessage);
        message.setMessageType(MessageTypeEnum.CHAT.getType());
        message.setStatus(MessageStatusEnum.SENDED.getStatus());
        messageMapper.insert(message);
    }





    @Override
    public R login(UserRegisterQuery user) {
        User one = lambdaQuery().eq(User::getEmail, user.getEmail()).one(); // 先查出用户信息，再比较，防止sql注入问题
        if(one == null)
            throw new BusinessException("账号不存在！");

        String s = DigestUtils.md5Hex(user.getPassword());
        if(!one.getPassword().equals(s))
            throw new BusinessException("密码不正确！");

        if(UserStatusEnum.DISABLE.getStatus().equals(one.getStatus()))
            throw new BusinessException("账号被禁用！");


        //单点登录检查
        Long userHeartBeat = redisUtils.getUserHeartBeat(one.getUserId());
        if(userHeartBeat != null)
            throw new BusinessException("账号已登录！");

        //查询我的联系人、我的群聊
        LambdaQueryChainWrapper<Contact> wrapper = new LambdaQueryChainWrapper<>(contactMapper);
        List<Contact> contactList = wrapper.eq(Contact::getUserId, one.getUserId())
                .eq(Contact::getStatus, ContactStatusEnum.FRIEND.getStatus()).list();
        //放入redis缓存中
        List<String> allContactId = contactList.stream().map(item -> item.getContactId()).collect(Collectors.toList());
        if(allContactId.size() != 0)
            redisUtils.setContactList(one.getUserId(),allContactId);


        String token = DigestUtils.md5Hex(one.getUserId()+RandomUtil.randomString(20));//加上userId，降低重复概率
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserId(one.getUserId());
        userInfoDTO.setEmail(one.getEmail());
        userInfoDTO.setNickName(one.getNickName());
        userInfoDTO.setToken(token);
        String adminEmails = appConfig.getAdminEmails();
        if (ArrayUtil.contains(adminEmails.split(","),one.getEmail())) {
            userInfoDTO.setAdmin(true);
        }
        redisUtils.setUserInfoDTO(token,userInfoDTO);
        return R.success(userInfoDTO);
    }

    @Override
    @Transactional
    public void removeContact(String contactId,ContactStatusEnum statusEnum) {
        UserInfoDTO userInfoDTO = userUtils.get();
        String userId = userInfoDTO.getUserId();
        LocalDateTime time = LocalDateTime.now();

        LambdaQueryChainWrapper<Contact> queryChainWrapper = new LambdaQueryChainWrapper<>(contactMapper);
        Contact contact = queryChainWrapper.eq(Contact::getUserId, userId)
                .eq(Contact::getContactId, contactId).one();
        if(contact == null || !ContactStatusEnum.FRIEND.getStatus().equals(contact.getStatus())){
            throw new BusinessException(ExceptionEnum.OTHERS);
        }

        //修改contactStatus
        Contact updateContact = new Contact();
        updateContact.setStatus(statusEnum.getStatus());
        updateContact.setLastUpdateTime(time);
        LambdaUpdateWrapper<Contact> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Contact::getUserId,userId)
                .eq(Contact::getContactId,contactId);
        contactMapper.update(updateContact,updateWrapper);

        updateContact = new Contact();
        updateContact.setStatus(ContactStatusEnum.DEL == statusEnum ?
                ContactStatusEnum.DEL_BE.getStatus() : ContactStatusEnum.BLACKlIST_BE.getStatus());
        updateContact.setLastUpdateTime(time);
        updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Contact::getUserId,contactId)
                .eq(Contact::getContactId,userId);
        contactMapper.update(updateContact,updateWrapper);
        //todo 从我的好友列表中删除缓冲
        redisUtils.removeContactList(userId,contactId);
        //todo 从好友的列表中删除我
        redisUtils.removeContactList(contactId,userId);

    }

    @Override
    @Transactional
    public void saveMyself(User user, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {

        if(avatarFile != null){
            //更新头像
            String basePath = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE + SysConstant.FILE_FOLDER_AVATAR;
            File file = new File(basePath);
            if(!file.exists())
                file.mkdirs();

            String path = file.getPath()+"/"+user.getUserId()+SysConstant.IMAGE_SUFFIX;
            String coverPath = file.getPath()+"/"+user.getUserId()+SysConstant.COVER_IMAGE_SUFFIX;

            avatarFile.transferTo(new File(path));
            avatarCover.transferTo(new File(coverPath));

        }
        //判断是否需要更新冗余
        User dbUser = getById(user.getUserId());
        updateById(user);
        String contactNameUpdate = null;
        if(user.getNickName()!= null && !dbUser.getNickName().equals(user.getNickName())){
            contactNameUpdate = user.getNickName();
        }
        //todo 更新会话信息中的用户名称
        UserSession userSession = new UserSession();
        userSession.setContactName(contactNameUpdate);
        userSessionMapper.update(userSession,
                new LambdaUpdateWrapper<UserSession>().eq(UserSession::getContactId,user.getUserId()));

        String token = userUtils.get().getToken();
        UserInfoDTO userInfoDTO = redisUtils.getUserInfoDTO(token, UserInfoDTO.class);
        userInfoDTO.setNickName(contactNameUpdate);
        redisUtils.setUserInfoDTO(token,userInfoDTO);

    }

    @Override
    public void updateUserStatus(String status, String userId) {
        UserStatusEnum statusEnum = UserStatusEnum.getByStatus(status);
        if(statusEnum == null)
            throw new BusinessException(ExceptionEnum.OTHERS);

        lambdaUpdate().set(User::getStatus,statusEnum.getStatus())
                .eq(User::getUserId,userId).update();

    }

    @Override
    public void forceOffLine(String userId) {
        //todo 强制下线
        channelContextUtils.closeContact(userId);
    }

    private String createUserId() {
        return ContactTypeEnum.USER.getPrefix() +RandomUtil.randomString(11);
    }
}
