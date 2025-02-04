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
import org.springframework.beans.factory.annotation.Autowired;

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

    @ApiModelProperty(value = "发表时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String[] fileDesc;

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

    public String[] getFileDesc() {
        if(file != null)
            return file.split("\\|");
        return null;
    }
}
