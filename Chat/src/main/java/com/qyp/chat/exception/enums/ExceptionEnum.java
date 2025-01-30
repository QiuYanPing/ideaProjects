package com.qyp.chat.exception.enums;

public enum ExceptionEnum {
    UNAUTH(404,"权限不足"),
    OTHERS(600,"其他异常"),
    DUPLICATE(601,"重复信息"),
    NOT_FRIEND(901,"您不是对方的好友"),
    NOT_IN_GROUP(902,"您不在群聊中");

    private Integer code;
    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
