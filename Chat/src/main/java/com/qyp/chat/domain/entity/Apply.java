package com.qyp.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.qyp.chat.domain.enums.ApplyStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 申请
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("apply")
@ApiModel(value="Apply对象", description="申请")
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申请id")
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Integer applyId;

    @ApiModelProperty(value = "申请人id")
    private String applyUserId;

    @ApiModelProperty(value = "接收人id")
    private String receiveUserId;

    @ApiModelProperty(value = "联系人类型0：好友 1：群聊")
    private Integer contactType;

    @ApiModelProperty(value = "联系人id或者群组id")
    private String contactId;

    @ApiModelProperty(value = "最后申请时间")
    private Long lastApplyTime;

    @ApiModelProperty(value = "状态0：待处理 1：已同意 2：已拒绝 3：已拉黑")
    private Integer status;

    @ApiModelProperty(value = "申请信息")
    private String applyInfo;

    @TableField(exist = false)
    @ApiModelProperty(value = "当申请好友时，显示申请人；当申请群聊时，显示群聊名称")
    private String contactName;


    @TableField(exist = false)
    private String statusName;

    public String getStatusName() {
        ApplyStatusEnum statusEnum = ApplyStatusEnum.getByStatus(status);
        return statusEnum == null ? null :statusEnum.getDesc();
    }
}
