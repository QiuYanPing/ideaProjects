package com.qyp.chat.domain.dto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class MessageDTO <T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long messageId;
    private String sessionId;

    private String sendUserId;
    private String sendUserNickName;
    private Long sendTime;

    private String contactId;
    private String contactName;
    private Integer contactType;

    private String lastMessage;
    private String messageContent;
    private Integer messageType;
    private Integer status; //0:发送中 1：已发送

    private Long fileSize;
    private String fileName;
    private Integer fileType;

    private T extendData;
    private Integer memberCount;

    public String getLastMessage() {
        if(StrUtil.isEmpty(lastMessage))
            return messageContent;
        return lastMessage;
    }
}
