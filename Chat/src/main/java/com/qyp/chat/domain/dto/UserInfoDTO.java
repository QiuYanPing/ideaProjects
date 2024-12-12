package com.qyp.chat.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserInfoDTO {

    private String userId;
    private String email;
    private String nickName;
    private boolean admin;

    private String token;
}
