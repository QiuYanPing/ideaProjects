package com.qyp.chat.service;

import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


public interface IChatService {

    MessageDTO sendMessage(Message message);

    void uploadFile(long parseLong, MultipartFile file, MultipartFile fileCover);

    void downloadFile(long parseLong,HttpServletResponse response,Boolean isCover);
}
