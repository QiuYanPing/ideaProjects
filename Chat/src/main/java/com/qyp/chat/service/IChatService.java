package com.qyp.chat.service;

import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.entity.Message;
import org.springframework.stereotype.Service;


public interface IChatService {

    MessageDTO sendMessage(Message message);
}
