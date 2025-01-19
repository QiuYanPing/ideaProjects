package com.qyp.chat.domain.dto;

import com.qyp.chat.domain.entity.Message;
import com.qyp.chat.domain.entity.UserSession;
import lombok.Data;

import java.util.List;

@Data
public class WSInitDTO {
    private List<UserSession>  userSessionList;
    private List<Message> messageList; // 离线信息
    private Integer applyCount;
}
