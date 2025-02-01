package com.qyp.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value="User对象", description="用户")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "user_id")
    private String userId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "添加好友设置0：直接加入 1：同意后加好友")
    private Integer joinType;

    @ApiModelProperty(value = "性别0：女 1：男")
    private Integer sex;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "个性签名")
    private String personalSignature;

    @ApiModelProperty(value = "状态0：禁用 1：启用")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "地区")
    private String areaName;

    @ApiModelProperty(value = "地区编码")
    private String areaCode;

    @ApiModelProperty(value = "最后离线时间")
    private Long lastOffTime;

    @ApiModelProperty(value = "金币")
    private Integer coins;


}
