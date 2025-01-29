package com.qyp.chat.controller;

import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.entity.Message;
import com.qyp.chat.service.IChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    @Autowired
    IChatService chatService;

    @PostMapping("/message")
    public R sendMessage(String contactId,
                         String messageContent,
                         Integer messageType,
                         Long fileSize,
                         String fileName,
                         Integer fileType){
        Message message = new Message();
        message.setContactId(contactId);
        message.setMessageContent(messageContent);
        message.setMessageType(messageType);
        message.setFileSize(fileSize);
        message.setFileName(fileName);
        message.setFileType(fileType);
        MessageDTO messageDTO = chatService.sendMessage(message);
        return R.success(messageDTO);
    }



}
