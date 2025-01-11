package com.qyp.chat.exception;

import com.qyp.chat.exception.enums.ExceptionEnum;
import io.swagger.models.auth.In;

public class BusinessException extends RuntimeException{
    ExceptionEnum exception;

    public BusinessException(ExceptionEnum exception) {
        this.exception = exception;
    }

    public ExceptionEnum getException() {
        return exception;
    }

    public BusinessException(String msg) {
        ExceptionEnum others = ExceptionEnum.OTHERS;
        others.setMsg(msg);
        this.exception = others;
    }
}
