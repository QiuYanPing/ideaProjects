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
 * 博文评论表
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog_comments")
@ApiModel(value="BlogComments对象", description="博文评论表")
public class BlogComments implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论id")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Long commentId;

    @ApiModelProperty(value = "评论人")
    private String userId;

    @ApiModelProperty(value = "博文id")
    private Long blogId;

    @ApiModelProperty(value = "评论")
    private String comment;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;


}
