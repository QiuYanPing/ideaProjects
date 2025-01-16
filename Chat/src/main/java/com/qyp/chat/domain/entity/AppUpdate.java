package com.qyp.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 版本管理
 * </p>
 *
 * @author 
 * @since 2025-01-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("app_update")
@ApiModel(value="AppUpdate对象", description="版本管理")
public class AppUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "版本号")
    @TableField("version")
    private String version;

    @ApiModelProperty(value = "更新描述")
    @TableField("update_desc")
    private String updateDesc;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "灰度发布 0：未发布 1：灰度发布 2：全网发布")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "灰度id")
    @TableField("grayscale_uid")
    private String grayscaleUid;

    @ApiModelProperty(value = "文件类型0：本地文件 1：外链")
    @TableField("file_type")
    private Integer fileType;

    @ApiModelProperty(value = "外链地址")
    @TableField("outer_link")
    private String outerLink;


    @TableField(exist = false)
    private String[] updateDescArray;

    public String[] getUpdateDescArray() {
        if(updateDesc!=null)
            return updateDesc.split("\\|");
        return updateDescArray;
    }
}
