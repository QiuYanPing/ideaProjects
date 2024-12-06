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
 * 联系人
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contact")
@ApiModel(value="Contact对象", description="联系人")
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private String userId;

    @ApiModelProperty(value = "联系人id或群聊id")
    private String contactId;

    @ApiModelProperty(value = "联系人类型0：好友 1：群聊")
    private Boolean contactType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "状态0：非好友 2：好友 3：已删除好友 4：被好友删除 5：已拉黑好友 6：被好友拉黑")
    private Boolean status;

    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime lastUpdateTime;


}
