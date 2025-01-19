package com.qyp.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.qyp.chat.domain.enums.ContactTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户会话
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_session")
@ApiModel(value="UserSession对象", description="用户会话")
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id")
    private String userId;

    @ApiModelProperty(value = "联系人id")
    private String contactId;

    @ApiModelProperty(value = "会话id")
    private String sessionId;

    @ApiModelProperty(value = "联系人名称")
    private String contactName;


    @TableField(exist = false)
    private String lastMessage;
    @TableField(exist = true)
    private Long lastReceiveTime;
    @TableField(exist = false)
    private Integer memberCount;
    @TableField(exist = false)
    private Integer contactType;

    public Integer getContactType() {
        return ContactTypeEnum.getByPrefix(contactId).getType();
    }
}
