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
 * 博文表
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog")
@ApiModel(value="Blog对象", description="博文表")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博文id")
    @TableId(value = "blog_id", type = IdType.AUTO)
    private Integer blogId;

    @ApiModelProperty(value = "发表者")
    private String userId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "图片或者视频，使用|分割")
    private String file;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;


}
