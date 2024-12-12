package com.qyp.chat.domain.enums;





public enum JoinTypeEnum {
    JSON(0, "直接加入"),
    APPLY(1, "需要审核");

    private Integer type;
    private String desc;


    JoinTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
