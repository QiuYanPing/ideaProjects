package com.qyp.chat.service.impl;


import ch.qos.logback.classic.spi.EventArgUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.*;
import com.qyp.chat.domain.enums.*;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.*;
import com.qyp.chat.service.IApplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.service.IContactService;
import com.qyp.chat.service.ISessionService;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.StringUtils;
import com.qyp.chat.util.UserUtils;
import com.qyp.chat.websocket.ChannelContextUtils;
import com.qyp.chat.websocket.MessageHandler;
import io.swagger.models.auth.In;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.hutool.core.util.NumberUtil.add;

/**
 * <p>
 * 申请 服务实现类
 * </p>
 *
 * @author
 * @since 2024-12-06
 */
@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements IApplyService {
    @Autowired
    UserUtils userUtils;

    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ContactMapper contactMapper;
    @Autowired
    ApplyMapper applyMapper;
    @Autowired
    IContactService contactService;
    @Autowired
    MessageHandler messageHandler;

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UserSessionMapper userSessionMapper;
    @Autowired
    ISessionService sessionService;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    StringUtils stringUtils;
    @Autowired
    ChannelContextUtils channelContextUtils;

    @Override
    @Transactional
    public Integer applyAdd(String contactId, String applyInfo) {
        UserInfoDTO userInfoDTO = userUtils.get();
        long time = System.currentTimeMillis();
        ContactTypeEnum typeEnum = ContactTypeEnum.getByPrefix(contactId);
        Integer joinType = null;
        String receiveUserId = contactId;

        //判断是否被拉黑
        LambdaQueryChainWrapper<Contact> wrapper = new LambdaQueryChainWrapper<>(contactMapper);
        Contact contact = wrapper.eq(Contact::getUserId, userInfoDTO.getUserId())
                .eq(Contact::getContactId, contactId).one();
        if (contact != null && ArrayUtil.contains(new Integer[]{
                ContactStatusEnum.BLACKlIST_BE.getStatus(), ContactStatusEnum.FIRST_BLACKLIST_BE.getStatus()
        }, contact.getStatus()))
            throw new BusinessException("你已被拉黑，不能发送申请！");


        if (ContactTypeEnum.GROUP == typeEnum) {
            Group group = groupMapper.selectById(contactId);
            if (group == null || GroupStatusEnum.DISSOLUTION.getStatus().equals(group.getStatus()))
                throw new BusinessException("群聊不存在或已解散！");
            receiveUserId = group.getGroupOwnerId();
            joinType = group.getJoinType();
        } else {
            User user = userMapper.selectById(contactId);
            if (user == null)
                throw new BusinessException("用户不存在！");
            joinType = user.getJoinType();
        }


        if (JoinTypeEnum.JSON.getType().equals(joinType)) {
            //直接加入
            addContact(userInfoDTO.getUserId(), receiveUserId, contactId, typeEnum.getType());
            return joinType;
        }

        //查看是否已发送过申请
        Apply oldApply = lambdaQuery().eq(Apply::getApplyUserId, userInfoDTO.getUserId())
                .eq(Apply::getContactId, contactId)
                .eq(Apply::getReceiveUserId, receiveUserId).one();

        Apply apply = new Apply();
        apply.setApplyInfo(applyInfo == null ?
                String.format(SysConstant.APPLY_INFO, userInfoDTO.getNickName())
                : applyInfo);
        apply.setLastApplyTime(time);
        if (oldApply == null) {
            //没有发送过申请
            apply.setApplyUserId(userInfoDTO.getUserId());
            apply.setContactId(contactId);
            apply.setContactType(typeEnum.getType());
            apply.setReceiveUserId(receiveUserId);
            apply.setStatus(ApplyStatusEnum.INIT.getStatus());
            save(apply);
        } else {
            apply.setStatus(ApplyStatusEnum.INIT.getStatus());
            LambdaUpdateWrapper<Apply> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Apply::getApplyUserId, userInfoDTO.getUserId())
                    .eq(Apply::getContactId, contactId)
                    .eq(Apply::getReceiveUserId, receiveUserId);
            update(apply, updateWrapper);

        }

        //首次发送申请或申请已被处理，则发送ws消息
        if (oldApply == null || !ApplyStatusEnum.INIT.getStatus().equals(oldApply.getStatus())) {
            // 发送ws消息
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageContent(apply.getApplyInfo());
            messageDTO.setMessageType(MessageTypeEnum.CONTACT_APPLY.getType());
            messageDTO.setContactId(receiveUserId);
            messageHandler.sendMessage(messageDTO);
        }

        return joinType;
    }

    @Override
    public List<Apply> loadApply(Integer pageNo) {
        UserInfoDTO userInfoDTO = userUtils.get();
        String userId = userInfoDTO.getUserId();
        /*LambdaQueryWrapper<Apply> applyLambdaQueryWrapper = new LambdaQueryWrapper<>();
        applyLambdaQueryWrapper.eq(Apply::getReceiveUserId,userId)
                .orderByDesc(Apply::getLastApplyTime);
        Page<Apply> page = page(Page.of(pageNo, SysConstant.Page_SIZE), applyLambdaQueryWrapper);*/

        //多表查询获取申请人的名称或被申请群聊名称
        Integer pageSize = SysConstant.Page_SIZE;
        //根据申请时间降序排序
        pageNo = (pageNo - 1) * pageSize;
        List<Apply> applyList = applyMapper.loadApply(userId, pageNo, pageSize);
        return applyList;
    }

    @Override
    @Transactional
    public void dealWithApply(Integer applyId, Integer status) {
        LocalDateTime time = LocalDateTime.now();
        UserInfoDTO userInfoDTO = userUtils.get();
        ApplyStatusEnum statusEnum = ApplyStatusEnum.getByStatus(status);
        //非法传参判断
        if (statusEnum == null || ApplyStatusEnum.INIT.getStatus().equals(status))
            throw new BusinessException(ExceptionEnum.OTHERS);

        //判断当前用户是否为接受人
        String userId = userInfoDTO.getUserId();
        Apply apply = getById(applyId);
        if (apply == null || !userId.equals(apply.getReceiveUserId()))
            throw new BusinessException(ExceptionEnum.OTHERS);

        //修改申请的状态和时间
        Apply updateApply = new Apply();
        updateApply.setStatus(status);
        updateApply.setLastApplyTime(System.currentTimeMillis());
        LambdaUpdateWrapper<Apply> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Apply::getApplyId, applyId)
                .eq(Apply::getStatus, ApplyStatusEnum.INIT.getStatus());
        int count = applyMapper.update(updateApply, wrapper);
        //不可重复修改申请状态
        if (count == 0)
            throw new BusinessException(ExceptionEnum.OTHERS);


        if (ApplyStatusEnum.PASS == statusEnum) {
            addContact(apply.getApplyUserId(), apply.getReceiveUserId(), apply.getContactId(), apply.getContactType());
        }

        if (ApplyStatusEnum.BLACKLIST == statusEnum) {
            LambdaQueryChainWrapper<Contact> queryChainWrapper = new LambdaQueryChainWrapper<>(contactMapper);
            Contact contact = queryChainWrapper.eq(Contact::getUserId, apply.getApplyUserId())
                    .eq(Contact::getContactId, apply.getContactId()).one();
            if (contact != null) {
                LambdaUpdateChainWrapper<Contact> updateChainWrapper = new LambdaUpdateChainWrapper<>(contactMapper);
                updateChainWrapper.set(Contact::getStatus, ContactStatusEnum.FIRST_BLACKLIST_BE.getStatus())
                        .set(Contact::getLastUpdateTime, time)
                        .eq(Contact::getUserId, apply.getApplyUserId())
                        .eq(Contact::getContactId, apply.getContactId()).update();
            } else {
                //添加记录
                Contact addContact = new Contact();
                addContact.setUserId(apply.getApplyUserId());
                addContact.setContactId(apply.getContactId());
                addContact.setContactType(apply.getContactType());
                addContact.setCreateTime(time);
                addContact.setLastUpdateTime(time);
                addContact.setStatus(ContactStatusEnum.FIRST_BLACKLIST_BE.getStatus());
                contactMapper.insert(addContact);
            }
        }

    }

    public void addContact(String applyUserId, String receiveUserId, String contactId, Integer contactType) {
        //如果是群聊的话，判断是否超过最大群成员数(存在并发问题：悲观锁解决)
        ContactTypeEnum typeEnum = ContactTypeEnum.getByPrefix(contactId);
        if (ContactTypeEnum.GROUP == typeEnum) {
            LambdaQueryChainWrapper<Contact> queryChainWrapper = new LambdaQueryChainWrapper<>(contactMapper);
            Integer count = queryChainWrapper.eq(Contact::getContactId, contactId)
                    .eq(Contact::getStatus, ContactStatusEnum.FRIEND.getStatus()).count();
            SysSettingDTO sysSetting = userUtils.getSysSetting();
            Integer maxGroupMember = sysSetting.getMaxGroupMember();
            if (count < maxGroupMember) {
                synchronized (contactId.intern()) {
                    if (count < maxGroupMember) {
                        //添加记录
                        beginAdd(applyUserId, receiveUserId, contactId, contactType);
                        return;
                    }
                }
            } else {
                throw new BusinessException("群成员已满！");
            }
        }
        beginAdd(applyUserId, receiveUserId, contactId, contactType);
        return;
    }

    private void beginAdd(String applyUserId, String receiveUserId, String contactId, Integer contactType) {
        LocalDateTime time = LocalDateTime.now();
        User applyUser = userMapper.selectById(applyUserId);


        //添加申请人的contact记录
        Contact contact = new Contact();
        contact.setUserId(applyUserId);
        contact.setContactId(contactId);
        contact.setContactType(contactType);
        contact.setCreateTime(time);
        contact.setLastUpdateTime(time);
        contact.setStatus(ContactStatusEnum.FRIEND.getStatus());
        contactService.saveOrUpdate(contact,new LambdaUpdateWrapper<Contact>().eq(Contact::getUserId,contact.getUserId())
                .eq(Contact::getContactId,contact.getContactId()));
        //如果contactType为好友时，添加联系人的contact记录
        if (ContactTypeEnum.USER.getType().equals(contactType)) {
            contact = new Contact();
            contact.setUserId(contactId);
            contact.setContactId(applyUserId);
            contact.setContactType(contactType);
            contact.setCreateTime(time);
            contact.setLastUpdateTime(time);
            contact.setStatus(ContactStatusEnum.FRIEND.getStatus());
            contactService.saveOrUpdate(contact,new LambdaUpdateWrapper<Contact>().eq(Contact::getUserId,contact.getUserId())
                    .eq(Contact::getContactId,contact.getContactId()));
        }


        /*contactService.saveBatch(list);*/
        //todo 创建联系人冗余： 如果为好友，将接收人更新缓存，将申请人加入为联系人
        redisUtils.setContactList(applyUserId,List.of(contactId));
        if(ContactTypeEnum.USER.getType().equals(contactType)){
            redisUtils.setContactList(contactId,List.of(applyUserId));
        }
        channelContextUtils.

        //todo 创建回话 发送ws信息
        Apply apply = lambdaQuery().eq(Apply::getApplyUserId, applyUserId)
                .eq(Apply::getContactId, contactId).one();
        Session session = new Session();
        String sessionId = stringUtils.createSession(applyUserId, contactId);
        if(ContactTypeEnum.GROUP.getType().equals(contactType))
            sessionId = stringUtils.createSession(contactId,"");
        session.setSessionId(sessionId);
        session.setLastMessage(apply.getApplyInfo());
        session.setLastReceiveTime(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        sessionService.saveOrUpdate(session);//如果之前创建过，则不再创建

        UserSession applySession = new UserSession();
        applySession.setUserId(applyUserId);
        applySession.setContactId(contactId);
        applySession.setSessionId(sessionId);
        Group group = groupMapper.selectById(contactId);
        applySession.setContactName(group.getGroupName());
        if(ContactTypeEnum.USER.getType().equals(contactType)){
            User user = userMapper.selectById(contactId);
            applySession.setContactName(user.getNickName());

            UserSession contactSession = new UserSession();
            contactSession.setUserId(contactId);
            contactSession.setContactId(applyUserId);
            contactSession.setSessionId(sessionId);
            contactSession.setContactName(applyUser.getNickName());
            userSessionMapper.insert(contactSession);
        }
        userSessionMapper.insert(applySession);

        Message message = new Message();
        message.setSessionId(sessionId);
        message.setSendUserId(applyUserId);
        message.setSendUserNickName(applyUser.getNickName());
        message.setSendTime(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        message.setContactId(contactId);
        message.setContactType(contactType);
        message.setMessageContent(apply.getApplyInfo());
        message.setMessageType(MessageTypeEnum.ADD_FRIEND.getType());
        message.setStatus(MessageStatusEnum.SENDED.getStatus());
        messageMapper.insert(message);

        //发送ws信息
        MessageDTO contactMessageDTO = BeanUtil.toBean(message, MessageDTO.class);
        contactMessageDTO.setExtendData();
        channelContextUtils


    }
}
