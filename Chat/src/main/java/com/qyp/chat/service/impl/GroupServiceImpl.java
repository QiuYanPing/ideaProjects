package com.qyp.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.*;
import com.qyp.chat.domain.enums.*;
import com.qyp.chat.domain.vo.GroupVO;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.*;
import com.qyp.chat.service.IGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.StringUtils;
import com.qyp.chat.util.UserUtils;
import com.qyp.chat.websocket.ChannelContextUtils;
import com.qyp.chat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {
    @Autowired
    UserUtils userUtils;
    @Autowired
    ContactMapper contactMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    StringUtils stringUtils;
    @Autowired
    SessionMapper sessionMapper;
    @Autowired
    UserSessionMapper userSessionMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ChannelContextUtils channelContextUtils;
    @Autowired
    MessageHandler messageHandler;

    @Override
    @Transactional
    public void saveGroup(Group group, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
        LocalDateTime dateTime = LocalDateTime.now();
        UserInfoDTO userInfoDTO = userUtils.get();
        if(StrUtil.isEmpty(group.getGroupId())){
            //新增
            SysSettingDTO sysSetting = userUtils.getSysSetting();
            Integer maxGroupCount = sysSetting.getMaxGroupCount();
            //查询当前用户已创建的群聊数量
            Integer count = lambdaQuery().eq(Group::getGroupOwnerId, userInfoDTO.getUserId()).count();
            if(count == maxGroupCount)
                throw new BusinessException("创建的群聊数已达最大！");

            String groupId = createGroupId();
            group.setGroupId(groupId);
            group.setCreateTime(dateTime);
            save(group);

            //将群聊添加为联系人
            Contact contact = new Contact();
            contact.setUserId(userInfoDTO.getUserId());
            contact.setContactType(ContactTypeEnum.GROUP.getType());
            contact.setContactId(groupId);
            contact.setCreateTime(dateTime);
            contact.setLastUpdateTime(dateTime);
            contact.setStatus(ContactStatusEnum.FRIEND.getStatus());
            contactMapper.insert(contact);

            //todo 创建回话
            Session session = new Session();
            String sessionId = stringUtils.createSession(groupId, "");
            session.setSessionId(sessionId);
            session.setLastMessage(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            session.setLastReceiveTime(dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            sessionMapper.insert(session);

            UserSession userSession = new UserSession();
            userSession.setUserId(userInfoDTO.getUserId());
            userSession.setContactId(groupId);
            userSession.setSessionId(sessionId);
            userSession.setContactName(group.getGroupName());
            userSessionMapper.insert(userSession);
            //更新冗余
            redisUtils.setContactList(userInfoDTO.getUserId(),List.of(groupId));
            channelContextUtils.addUser2Group(userInfoDTO.getUserId(),groupId);

            //todo 发送创建群聊成功的信息
            Message message = new Message();
            message.setSessionId(sessionId);
            message.setSendTime(dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            message.setContactId(groupId);
            message.setContactType(ContactTypeEnum.GROUP.getType());
            message.setMessageContent(MessageTypeEnum.GROUP_CREATE.getInitMessage());
            message.setMessageType(MessageTypeEnum.GROUP_CREATE.getType());
            message.setStatus(MessageStatusEnum.SENDED.getStatus());
            messageMapper.insert(message);


            MessageDTO messageDTO = BeanUtil.toBean(message, MessageDTO.class);
            messageHandler.sendMessage(messageDTO);
        }else{
            //修改
            //判断当前用户是否为群主，只有为群主才可以对群信息进行修改
            Group  dbGroup = getById(group.getGroupId());
            if(!userInfoDTO.getUserId().equals(dbGroup.getGroupOwnerId()))
                throw new BusinessException("非法修改！！");
            updateById(group);

            //todo 更新冗余信息
            String updateName = null;
            if(group.getGroupName()!= null && !dbGroup.getGroupName().equals(group.getGroupName())){
                updateName = group.getGroupName();
            }
            if(updateName == null)
                return;
            UserSession userSession = new UserSession();
            userSession.setContactName(updateName);
            userSessionMapper.update(userSession,
                    new LambdaUpdateWrapper<UserSession>().eq(UserSession::getContactId,group.getGroupId()));
            //todo 发送ws信息
            Message message = new Message();
            message.setSessionId(stringUtils.createSession(group.getGroupId(),""));
            message.setSendTime(dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
            message.setContactId(group.getGroupId());
            message.setContactType(ContactTypeEnum.GROUP.getType());
            message.setMessageContent(MessageTypeEnum.GROUP_NAME_UPDATE.getInitMessage());
            message.setMessageType(MessageTypeEnum.GROUP_NAME_UPDATE.getType());
            message.setStatus(MessageStatusEnum.SENDED.getStatus());
            messageMapper.insert(message);

            MessageDTO messageDTO = BeanUtil.toBean(message,MessageDTO.class);
            messageHandler.sendMessage(messageDTO);
        }
        if(avatarFile == null){
           return;
        }
        //上传群聊头像
        String projectFolder = appConfig.getProjectFolder();
        String path = projectFolder + SysConstant.FILE_FOLDER_FILE + SysConstant.FILE_FOLDER_AVATAR;
        File fileFolder = new File(path);
        if(!fileFolder.exists()){
            //文件夹不存在
            fileFolder.mkdirs();
        }
        String filePath = fileFolder.getPath()+"/"+group.getGroupId()+SysConstant.IMAGE_SUFFIX;
        String coverPath = fileFolder.getPath()+"/"+group.getGroupId()+SysConstant.COVER_IMAGE_SUFFIX;
        avatarFile.transferTo(new File(filePath));
        avatarCover.transferTo(new File(coverPath));

    }

    @Override
    public List<Group> loadMyGroup(String userId) {
        List<Group> list = lambdaQuery().eq(Group::getGroupOwnerId, userId)
                .orderByDesc(Group::getCreateTime).list();
        return list;
    }

    @Override
    public Group getGroupInfo(String userId, String groupId) {
        Group group = getGroup(userId, groupId);
        //查询群聊人数
        LambdaQueryChainWrapper<Contact> wrapper = new LambdaQueryChainWrapper<>(contactMapper);
        Integer count = wrapper.eq(Contact::getContactId, groupId).
                eq(Contact::getStatus,ContactStatusEnum.FRIEND.getStatus()).count();
        group.setMemberCount(count);
        return group;
    }

    @Override
    public GroupVO getGroupInfoDetail(String userId, String groupId) {
        Group group = getGroup(userId, groupId);
        //查询群聊成员信息
        List<Contact> contactList = contactMapper.selectGroupMember(groupId);
        GroupVO groupVO = new GroupVO();
        groupVO.setGroup(group);
        groupVO.setContactList(contactList);
        return groupVO;
    }

    @Override
    public List<Group> loadGroup() {

        return groupMapper.loadGroup();
    }

    @Override
    @Transactional
    public void dissolutionGroup(String groupOwnerId, String groupId) {
        //判断是否有权限解散群聊
        Group group = getById(groupId);
        if(group == null || !groupOwnerId.equals(group.getGroupOwnerId()))
            throw new BusinessException(ExceptionEnum.OTHERS);

        lambdaUpdate().set(Group::getStatus,GroupStatusEnum.DISSOLUTION.getStatus())
                .eq(Group::getGroupId,groupId).update(); //逻辑删除

        //更新联系人信息
        LambdaUpdateChainWrapper<Contact> contactLambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(contactMapper);
        contactLambdaUpdateChainWrapper.set(Contact::getStatus,ContactStatusEnum.DEL_BE.getStatus())
                .eq(Contact::getContactId,groupId).update();

        //todo 移除相关成员的联系人信息
        LambdaQueryChainWrapper<Contact> contactLambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(contactMapper);
        List<Contact> list = contactLambdaQueryChainWrapper.eq(Contact::getContactId, groupId)
                .eq(Contact::getStatus, ContactStatusEnum.FRIEND.getStatus()).list();
        List<String> contactList = list.stream().map(item -> item.getUserId()).collect(Collectors.toList());
        for (String userId : contactList) {
            redisUtils.removeContactList(userId,groupId);
        }
        //todo 1.更新会话信息 2.记录群信息 3.发生解散消息
        Message message = new Message();
        message.setSessionId(stringUtils.createSession(groupId,""));
        message.setSendTime(System.currentTimeMillis());
        message.setContactId(groupId);
        message.setContactType(ContactTypeEnum.GROUP.getType());
        message.setMessageContent(MessageTypeEnum.DISSOLUTION_GROUP.getInitMessage());
        message.setMessageType(MessageTypeEnum.DISSOLUTION_GROUP.getType());
        message.setStatus(MessageStatusEnum.SENDED.getStatus());
        messageMapper.insert(message);

        MessageDTO messageDTO = BeanUtil.toBean(message,MessageDTO.class);
        messageHandler.sendMessage(messageDTO);

        ChannelContextUtils.removeGroup(groupId);
        userSessionMapper.delete(new LambdaQueryWrapper<UserSession>().eq(UserSession::getContactId,groupId));
    }

    private Group getGroup(String userId, String groupId) {
        //当前用户需再群聊中
        LambdaQueryWrapper<Contact> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Contact::getContactId, groupId)
                .eq(Contact::getUserId, userId);
        Contact contact = contactMapper.selectOne(lambdaQueryWrapper);
        if(contact == null || !ContactStatusEnum.FRIEND.getStatus().equals(contact.getStatus()))
            throw new BusinessException("你不在群聊中或群聊不存在！");

        //查询的群聊应存在
        Group group = lambdaQuery().eq(Group::getGroupId, groupId).one();
        if(group == null || !GroupStatusEnum.NORMAL.getStatus().equals(group.getStatus()))
            throw new BusinessException("群聊不存在或群聊已解散！");
        return group;
    }


    private String createGroupId(){
        return ContactTypeEnum.GROUP.getPrefix()+ RandomUtil.randomString(11);
    }
}
