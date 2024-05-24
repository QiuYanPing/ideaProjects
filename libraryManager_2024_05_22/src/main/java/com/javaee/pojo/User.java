package com.javaee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String userName;
    private String password;
    private String name;
    private int gender;
    private String image;
    private String site;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
