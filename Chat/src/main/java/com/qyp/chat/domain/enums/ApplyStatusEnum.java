package com.qyp.chat.domain.enums;

import cn.hutool.core.util.StrUtil;

public enum ApplyStatusEnum {
    INIT(0,"待处理"),
    PASS(1,"已同意"),
    REJECT(2,"已拒绝"),
    BLACKLIST(3,"已拉黑");

    private Integer status;
    private String desc;

    ApplyStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public static ApplyStatusEnum getByStatus(Integer status){
        for (ApplyStatusEnum item : ApplyStatusEnum.values()) {
            if(item.getStatus().equals(status))
                return item;
        }
        return null;
    }

    public static ApplyStatusEnum getByStatus(String status){
        if(StrUtil.isEmpty(status))
            return null;
        return ApplyStatusEnum.valueOf(status.toUpperCase());
    }
}
