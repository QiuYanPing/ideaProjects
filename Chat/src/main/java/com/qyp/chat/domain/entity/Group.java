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
 * 群聊
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`group`")
@ApiModel(value="Group对象", description="群聊")
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群id")
    @TableId(value = "group_id")
    private String groupId;

    @ApiModelProperty(value = "群名")
    private String groupName;

    @ApiModelProperty(value = "群主id")
    private String groupOwnerId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "群公告")
    private String groupNotice;

    @ApiModelProperty(value = "加入群聊类型 0：直接加入 1：管理员同意后加入")
    private Integer joinType;

    @ApiModelProperty(value = "状态1：正常 0：解散")
    private Integer status;

    private Integer memberCount;


}
