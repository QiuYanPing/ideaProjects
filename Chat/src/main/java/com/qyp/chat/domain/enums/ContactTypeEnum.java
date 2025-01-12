package com.qyp.chat.domain.enums;

import io.swagger.models.auth.In;

public enum ContactTypeEnum {
    USER(0,"U","好友"),
    GROUP(1,"G","群聊");

    private Integer type;
    private String prefix;
    private String desc;

    ContactTypeEnum(Integer type, String prefix, String desc) {
        this.type = type;
        this.prefix = prefix;
        this.desc = desc;
    }

    public  Integer getType() {
        return type;
    }

    public  String getPrefix() {
        return prefix;
    }

    public  String getDesc() {
        return desc;
    }

    public static ContactTypeEnum getByPrefix(String id){
        String prefix = id.substring(0, 1);
        for (ContactTypeEnum value : ContactTypeEnum.values()) {
            if(value.getPrefix().equals(prefix))
                return value;
        }
        return null;
    }
}
