package com.qyp.chat.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.RedisConstant;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.enums.JoinTypeEnum;
import com.qyp.chat.domain.enums.UserStatusEnum;
import com.qyp.chat.domain.query.UserRegisterQuery;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.ContactMapper;
import com.qyp.chat.mapper.UserMapper;
import com.qyp.chat.service.IContactService;
import com.qyp.chat.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.UserUtils;
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

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    AppConfig appConfig;

    @Autowired
    ContactMapper contactMapper;

    @Autowired
    UserUtils userUtils;

    @Override
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
        //todo 创建机器人好友

    }

    @Override
    public R login(UserRegisterQuery user) {
        User one = lambdaQuery().eq(User::getEmail, user.getEmail()).one();
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

        //todo 查询我的联系人
        //todo 查询我的群聊

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
        //todo 从好友的列表中删除我

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
    }

    private String createUserId() {
        return ContactTypeEnum.USER.getPrefix() +RandomUtil.randomString(11);
    }
}
