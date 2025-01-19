package com.qyp.chat.domain.vo;

import lombok.Data;

@Data
public class AppUpdateVO {
    private Integer id;
    private String version;
    private String[] updateDescArray;
    private Integer fileType;
    private String outerLink;

    private String fileName;
    private Long size;
}
