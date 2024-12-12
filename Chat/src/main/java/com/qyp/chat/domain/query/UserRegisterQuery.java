package com.qyp.chat.domain.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegisterQuery {
    private String email;
    private String nickName;
    private String password;


    private String code;

}
