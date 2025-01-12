package com.qyp.chat.domain.dto;

import com.qyp.chat.domain.enums.ContactStatusEnum;
import lombok.Data;

@Data
public class ContactSearchDTO {
    private String contactId;
    private String contactType;
    private Integer status;
    private String statusName;

    private String nickName;
    private Integer sex;
    private String areaName;

    public String getStatusName() {
        //将status类型装换
        ContactStatusEnum contactStatusEnum = ContactStatusEnum.getByStatus(status);
        return contactStatusEnum == null ? null : contactStatusEnum.getDesc();
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }


}
