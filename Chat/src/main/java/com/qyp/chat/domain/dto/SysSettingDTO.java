package com.qyp.chat.domain.dto;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class SysSettingDTO {
    //系统设置
    Integer MaxGroupMember = 200;
    Integer MaxGroupCount  = 10;

    String RobotNickName = "robot";
    String RobotUid = "U12345678901";
    String RobotWelcome = "你好啊！！";

    Integer MaxFileSize = 500;
    Integer MaxImageSize = 500;
    Integer MaxVideoSize = 500; //单位MB

}
