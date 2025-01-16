package com.qyp.chat.util;

import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    RedisUtils redisUtils;

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
        SysSettingDTO sysSetting = redisUtils.getSysSetting();
        //没有设置，则放回一个默认的系统设置
        return sysSetting == null? new SysSettingDTO():sysSetting;
    }
}
