package com.qyp.chat.util;

import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    ThreadLocal<UserInfoDTO> t = new ThreadLocal<>();

    public void set (UserInfoDTO userInfoDTO){
        t.set(userInfoDTO);
    }

    public UserInfoDTO get(){
        return t.get();
    }

    public void remove(){
        t.remove();
    }

    public SysSettingDTO getSysSetting(){
        //获取系统设置
        return new SysSettingDTO();
    }
}
