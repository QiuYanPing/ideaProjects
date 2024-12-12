package com.qyp.chat.exception;

import io.swagger.models.auth.In;

public class BusinessException extends RuntimeException{
    private String msg;

    public BusinessException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
