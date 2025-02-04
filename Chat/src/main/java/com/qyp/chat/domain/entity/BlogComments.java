package com.qyp.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.SysConstant;
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
    private Integer blogId;

    @ApiModelProperty(value = "评论")
    @TableField(value = "`comment`")
    private String comment;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "点赞数")
    private Integer likes;


    @TableField(exist = false)
    private String nickName;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private Boolean isLike;


    public String getAvatar() {
        if(userId != null){
            AppConfig appConfig = new AppConfig();
            String path = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE + SysConstant.FILE_FOLDER_AVATAR + userId + SysConstant.IMAGE_SUFFIX;
            File file = new File(path);
            String avatarPath = "/"+ SysConstant.FILE_FOLDER_AVATAR+userId+SysConstant.IMAGE_SUFFIX;;
            if(!file.exists())
                avatarPath =  "/"+ SysConstant.FILE_FOLDER_AVATAR+"default"+SysConstant.IMAGE_SUFFIX;
            return avatarPath;
        }
        return null;
    }
}
