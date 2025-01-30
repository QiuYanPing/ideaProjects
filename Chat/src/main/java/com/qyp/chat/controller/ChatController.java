package com.qyp.chat.controller;

import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Message;
import com.qyp.chat.service.IChatService;
import com.qyp.chat.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    @Autowired
    IChatService chatService;
    @Autowired
    UserUtils userUtils;

    @PostMapping("/message")
    public R sendMessage(String contactId,String messageContent,Integer messageType,
                         Long fileSize,
                         String fileName,
                         Integer fileType){
        UserInfoDTO userInfoDTO = userUtils.get();
        Message message = new Message();
        message.setSendUserId(userInfoDTO.getUserId());
        message.setSendUserNickName(userInfoDTO.getNickName());
        message.setContactId(contactId);
        message.setMessageContent(messageContent);
        message.setMessageType(messageType);
        message.setFileSize(fileSize);
        message.setFileName(fileName);
        message.setFileType(fileType);
        MessageDTO messageDTO = chatService.sendMessage(message);
        return R.success(messageDTO);
    }

    @PostMapping("/uploadFile")
    public R uploadFile(String messageId , MultipartFile file,MultipartFile fileCover){
        chatService.uploadFile(Long.parseLong(messageId),file,fileCover);
        return R.success(null);
    }


    @PostMapping("/downloadFile")
    public void downloadFile(String messageId, HttpServletResponse response,Boolean isCover){
        chatService.downloadFile(Long.parseLong(messageId),response,isCover);
    }



}
