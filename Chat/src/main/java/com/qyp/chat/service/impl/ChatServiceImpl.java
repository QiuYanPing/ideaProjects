package com.qyp.chat.service.impl;

import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Message;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.mapper.MessageMapper;
import com.qyp.chat.service.IChatService;
import com.qyp.chat.service.IMessageService;
import com.qyp.chat.service.IUserService;
import com.qyp.chat.util.StringUtils;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    UserUtils userUtils;
    @Autowired
    StringUtils stringUtils;
    @Autowired
    MessageMapper messageMapper;
    @Override
    public MessageDTO sendMessage(Message message) {
        UserInfoDTO userInfoDTO = userUtils.get();
        long time = System.currentTimeMillis();
        String contactId = message.getContactId();
        ContactTypeEnum contactTypeEnum = ContactTypeEnum.getByPrefix(contactId);
        String sessionId = null;
        if(ContactTypeEnum.USER == contactTypeEnum){
            sessionId = stringUtils.createSession(userInfoDTO.getUserId(),contactId);
        }else{
            sessionId = stringUtils.createSession(contactId,"");
        }
        //保存message到数据库
        message.setSessionId(sessionId);
        message.setSendUserId(userInfoDTO.getUserId());
        message.setSendUserNickName(userInfoDTO.getNickName());
        message.setSendTime(time);
        message.setContactType(contactTypeEnum.getType());
        String messageContent = stringUtils.cleanHtml(message.getMessageContent());
        message.setMessageContent(messageContent);
        messageMapper.insert(message);

        //更新session的最新信息和最新的接收时间
        //发送ws信息给联系人

        return null;
    }
}
