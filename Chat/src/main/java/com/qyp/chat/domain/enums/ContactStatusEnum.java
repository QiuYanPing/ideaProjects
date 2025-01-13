package com.qyp.chat.domain.enums;

import cn.hutool.core.util.StrUtil;

import java.util.Locale;

public enum ContactStatusEnum {
    NOT_FRIEND(0,"非好友"),
    FRIEND(1,"好友"),
    DEL(2,"已删除好友"),
    DEL_BE(3,"被好友删除"),
    BLACKlIST(4,"已拉黑好友"),
    BLACKlIST_BE(5,"被好友拉黑"),


    FIRST_BLACKLIST_BE(6,"首次被拉黑"); //首次被拉黑的不显示在列表，而已被添加为好友后再被拉黑的需要显示在列表中，故需要区分两种情况


    private Integer status;
    private String desc;

    ContactStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }


    public static ContactStatusEnum getByName(String name){
        if(StrUtil.isEmpty(name))
            return null;
        return  ContactStatusEnum.valueOf(name.toUpperCase());
    }


    public static ContactStatusEnum getByStatus(Integer status){
        ContactStatusEnum[] values = ContactStatusEnum.values();
        for (ContactStatusEnum item : values) {
            if(item.getStatus().equals(status))
                return item;
        }
        return null;
    }
}
