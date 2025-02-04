package com.qyp.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.entity.Message;
import com.qyp.chat.domain.entity.Session;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.domain.enums.MessageStatusEnum;
import com.qyp.chat.domain.enums.MessageTypeEnum;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.ContactMapper;
import com.qyp.chat.mapper.MessageMapper;
import com.qyp.chat.mapper.SessionMapper;
import com.qyp.chat.service.IChatService;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.StringUtils;
import com.qyp.chat.util.UserUtils;
import com.qyp.chat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class ChatServiceImpl implements IChatService {

    @Autowired
    UserUtils userUtils;
    @Autowired
    StringUtils stringUtils;
    @Autowired
    AppConfig appConfig;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    ContactMapper contactMapper;
    @Autowired
    SessionMapper sessionMapper;

    @Autowired
    MessageHandler messageHandler;

    @Autowired
    RedisUtils redisUtils;

    @Override
    @Transactional
    public MessageDTO sendMessage(Message message) {
        SysSettingDTO sysSetting = userUtils.getSysSetting();
        String robotUid = sysSetting.getRobotUid();
        String robotNickName = sysSetting.getRobotNickName();
        long time = System.currentTimeMillis();
        String contactId = message.getContactId();
        ContactTypeEnum contactTypeEnum = ContactTypeEnum.getByPrefix(contactId);

        String sessionId = null;
        LambdaQueryChainWrapper<Contact> contactLambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(contactMapper);
        Contact contact = contactLambdaQueryChainWrapper.eq(Contact::getUserId, message.getSendUserId())
                .eq(Contact::getContactId, contactId)
                .eq(Contact::getStatus, ContactStatusEnum.FRIEND.getStatus()).one();
        if (message.getSendUserId().equals(robotUid))
            contact = new Contact();

        if (ContactTypeEnum.USER == contactTypeEnum) {
            //判断是否为好友
            if (contact == null)
                throw new BusinessException(ExceptionEnum.NOT_FRIEND);
            sessionId = stringUtils.createSession(message.getSendUserId(), contactId);
        } else {
            //判断是否在群聊中
            if (contact == null)
                throw new BusinessException(ExceptionEnum.NOT_IN_GROUP);
            sessionId = stringUtils.createSession(contactId, "");
        }

        //保存message到数据库
        message.setSessionId(sessionId);
        message.setSendUserId(message.getSendUserId());
        message.setSendUserNickName(message.getSendUserNickName());
        message.setSendTime(time);
        message.setContactType(contactTypeEnum.getType());
        String messageContent = stringUtils.cleanHtml(message.getMessageContent());
        message.setMessageContent(messageContent);
        message.setStatus(MessageStatusEnum.SENDED.getStatus());
        if (MessageTypeEnum.MEDIA_CHAT.getType().equals(message.getMessageType())) {
            message.setStatus(MessageStatusEnum.SENDING.getStatus());//设置发送中，确保消息发送顺序的正确性
        }
        messageMapper.insert(message);

        //更新session的最新信息和最新的接收时间
        Session session = new Session();
        session.setSessionId(sessionId);
        session.setLastReceiveTime(time);
        if (ContactTypeEnum.GROUP == contactTypeEnum) {
            session.setLastMessage(message.getSendUserNickName() + ":" + messageContent);
        } else {
            session.setLastMessage(messageContent);
        }
        sessionMapper.updateById(session);

        //发送ws信息给联系人
        MessageDTO messageDTO = BeanUtil.toBean(message, MessageDTO.class);
        if (contactId.equals(robotUid)) {
            //发送给机器人
            //todo 可以调用ai，实现自动回复
            Message robotMessage = new Message();
            robotMessage.setSendUserId(robotUid);
            robotMessage.setSendUserNickName(robotNickName);
            robotMessage.setContactId(message.getSendUserId());
            robotMessage.setMessageContent("目前无法解析文字进行回复。");
            robotMessage.setMessageType(MessageTypeEnum.CHAT.getType());
            robotMessage.setStatus(MessageStatusEnum.SENDED.getStatus());
            sendMessage(robotMessage);
        } else {
            messageHandler.sendMessage(messageDTO);
        }
        return messageDTO;
    }

    @Override
    public void uploadFile(long messageId, MultipartFile file, MultipartFile fileCover) {
        UserInfoDTO userInfoDTO = userUtils.get();
        SysSettingDTO sysSetting = userUtils.getSysSetting();
        Message message = messageMapper.selectById(messageId);
        if (message == null)
            throw new BusinessException(ExceptionEnum.OTHERS);

        //判断是否为发送者，防止篡改
        if (!message.getSendUserId().equals(userInfoDTO.getUserId())) {
            throw new BusinessException(ExceptionEnum.OTHERS);
        }

        String fileName = file.getOriginalFilename();
        String fileSuffix = stringUtils.getSuffix(fileName);
        String realName = messageId + fileSuffix;

        //判断文件类型
        //判断文件size是否满足要求
        if (ArrayUtil.contains(SysConstant.IMAGE_SUFFIX_LIST, fileSuffix)
                && message.getFileSize() > sysSetting.getMaxImageSize() * 1024 * 1024) {
            throw new BusinessException("图片大小太大");
        } else if (ArrayUtil.contains(SysConstant.VIDEO_SUFFIX_LIST, fileSuffix)
                && message.getFileSize() > sysSetting.getMaxVideoSize() * 1024 * 1024) {
            throw new BusinessException("视频大小太大");
        } else if (!ArrayUtil.contains(SysConstant.IMAGE_SUFFIX_LIST, fileSuffix)
                && !ArrayUtil.contains(SysConstant.VIDEO_SUFFIX_LIST, fileSuffix)
                && message.getFileSize() > sysSetting.getMaxFileSize() * 1024 * 1024) {
            throw new BusinessException("文件大小太大");
        }


        //上传文件
        LocalDate date = Instant.ofEpochMilli(message.getSendTime()).atZone(ZoneOffset.of("+8")).toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM");
        String monthFolder = dateTimeFormatter.format(date);
        String path = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE + monthFolder;
        File filePath = new File(path);
        if (!filePath.exists())
            filePath.mkdirs();
        try {
            file.transferTo(new File(filePath + "/" + realName));
            fileCover.transferTo(new File(filePath + "/" + messageId + SysConstant.COVER_IMAGE_SUFFIX));
        } catch (IOException e) {
            throw new BusinessException("文件上传失败");
        }
        //上传成功后，更新message的状态
        message.setStatus(MessageStatusEnum.SENDED.getStatus());
        messageMapper.updateById(message);
        //通知用户文件加载成功
        MessageDTO messageDTO = BeanUtil.toBean(message, MessageDTO.class);
        messageDTO.setMessageType(MessageTypeEnum.FILE_UPLOAD.getType());
        messageHandler.sendMessage(messageDTO);

    }

    @Override
    public void downloadFile(long messageId, HttpServletResponse response, Boolean isCover) {
        Message message = messageMapper.selectById(messageId);
        UserInfoDTO userInfoDTO = userUtils.get();

        //找到文件
        File file = null;
        if(!StringUtils.isNumber(messageId + "")){
            //查找头像图片
            String path = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE + SysConstant.FILE_FOLDER_AVATAR;
            File filePath = new File(path);
            if(!filePath.exists())
                throw new BusinessException(ExceptionEnum.OTHERS);
            file = new File(filePath.getPath()+"/"+messageId+SysConstant.IMAGE_SUFFIX);
            if(isCover)
                file = new File(filePath.getPath()+"/"+messageId+SysConstant.COVER_IMAGE_SUFFIX);
        }else{
            //判断是否为接受者
            String contactId = message.getContactId();
            ContactTypeEnum contactTypeEnum = ContactTypeEnum.getByPrefix(contactId);
            if (ContactTypeEnum.USER == contactTypeEnum) {
                if (!userInfoDTO.getUserId().equals(message.getContactId()))
                    throw new BusinessException(ExceptionEnum.OTHERS);
            } else {
                List<String> contactList = redisUtils.getContactList(userInfoDTO.getUserId());
                String[] array = contactList.toArray(new String[0]);
                if (!ArrayUtil.contains(array, contactId))
                    throw new BusinessException(ExceptionEnum.OTHERS);
            }

            //查找聊天文件
            LocalDate localDate = Instant.ofEpochMilli(message.getSendTime()).atZone(ZoneOffset.of("+8")).toLocalDate();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
            String monthFolder = dtf.format(localDate);
            String path = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE + monthFolder;
            File filePath = new File(path);
            if (!filePath.exists())
                throw new BusinessException(ExceptionEnum.OTHERS);
            String fileName = message.getFileName();
            String suffix = stringUtils.getSuffix(fileName);
            String realName = messageId + suffix;
            file = new File(filePath.getPath() + "/" + realName);
            if (isCover)
                file = new File(filePath.getPath() + "/" + messageId + SysConstant.COVER_IMAGE_SUFFIX);
        }
        response.setContentType("application/x-msdownload;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment;");
        response.setContentLengthLong(file.length());
        //下载文件
        OutputStream out = null;
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
            out = response.getOutputStream();
            byte[] bytes = new byte[1024*64];
            int len = 0;
            while ((len = in.read(bytes)) != -1){
                out.write(bytes,0,len);
            }
        } catch (IOException e) {
            throw new BusinessException("文件下载失败");
        } finally {
            //关流
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
