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
 * 优惠卷
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("coupons")
@ApiModel(value="Coupons对象", description="优惠卷")
public class Coupons implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "优惠卷id")
    @TableId(value = "coupons_id", type = IdType.AUTO)
    private Integer couponsId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "价值")
    private Integer cost;

    @ApiModelProperty(value = "过期时间")
    private Long outdateTime;

    @ApiModelProperty(value = "优惠卷数量")
    private Integer count;

    @ApiModelProperty(value = "兑换开始时间")
    private Long beginTime;

    @ApiModelProperty(value = "兑换结束时间")
    private Long endTime;


}
