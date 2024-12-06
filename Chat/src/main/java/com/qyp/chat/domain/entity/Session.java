package com.qyp.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会话
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("session")
@ApiModel(value="Session对象", description="会话")
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "会话id")
    @TableId(value = "session_id", type = IdType.AUTO)
    private String sessionId;

    @ApiModelProperty(value = "最后接受的消息")
    private String lastMessage;

    @ApiModelProperty(value = "最后接受消息时间（毫秒）")
    private Long lastReceiveTime;


}
