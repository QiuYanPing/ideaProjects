package com.qyp.chat.domain.enums;

public enum AppUpdateTypeEnum {
    LOCAL(0,"本地文件"),
    OUTER_LINK(1,"外链");

    private Integer type;
    private String desc;

    AppUpdateTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }


    public static  AppUpdateTypeEnum getByType(Integer type){
        for (AppUpdateTypeEnum item : AppUpdateTypeEnum.values()) {
            if(item.getType().equals(type))
                return item;
        }
        return null;
    }
}
