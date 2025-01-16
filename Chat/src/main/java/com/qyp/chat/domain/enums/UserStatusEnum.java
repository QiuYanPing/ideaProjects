package com.qyp.chat.domain.enums;

import io.swagger.models.auth.In;

public enum UserStatusEnum {
    DISABLE(0,"禁用"),
    ENABLE(1,"启用");

    private Integer status;
    private String desc;

    UserStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static UserStatusEnum getByStatus(String name){
        UserStatusEnum userStatusEnum = UserStatusEnum.valueOf(name.toUpperCase());
        return userStatusEnum;
    }
}
