package com.qyp.chat.domain.vo;

import lombok.Data;

@Data
public class UserVO {
    private String userId;
    private String email;
    private String nickName;
    private Integer sex;
    private Integer joinType;
    private Boolean admin;
    private String personalSignature;
    private Integer status;
}
