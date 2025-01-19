package com.qyp.chat.util;


import cn.hutool.json.JSONUtil;
import com.qyp.chat.constant.RedisConstant;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate ;



    public <T> T get(String key,Class<T> type){
        String jsonStr = stringRedisTemplate.opsForValue().get(key);
        if(jsonStr == null)
            return null;
        return JSONUtil.toBean(jsonStr,type);
    }

    public <T> void set(String key,T value){
        String s = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key,s);
    }

    public <T> void set(String key,T value,Long time ,TimeUnit timeType){
        String s = JSONUtil.toJsonStr(value);
        stringRedisTemplate.opsForValue().set(key,s,time,timeType);
    }




    public void test(){
        stringRedisTemplate.opsForValue().set("age","12");
    }

    public UserInfoDTO getUserInfoDTO(String token, Class<UserInfoDTO> userInfoDTOClass) {
        if(token == null)
            return null;
        return get(RedisConstant.CHAT_WS_TOKEN + token,UserInfoDTO.class);
    }
    public void setUserInfoDTO(String token,UserInfoDTO userInfoDTO) {
        set(RedisConstant.CHAT_WS_TOKEN+token,userInfoDTO,RedisConstant.CHAT_WS_TOKEN_EXPIRE,TimeUnit.SECONDS);
    }




    public Long getUserHeartBeat(String userId){
       return get(RedisConstant.CHAT_WS_USER_HEART_BEAT + userId, Long.class);
    }
    public void saveUserHeartBeat(String userId){
        stringRedisTemplate.opsForValue().setIfAbsent(RedisConstant.CHAT_WS_USER_HEART_BEAT + userId,"",
                RedisConstant.CHAT_WS_USER_HEART_BEAT_EXPIRES,
                TimeUnit.SECONDS); //心跳超时时间6s
    }
    public void removeUserHeartBeat(String userId){
        stringRedisTemplate.delete(RedisConstant.CHAT_WS_USER_HEART_BEAT + userId);
    }


    public SysSettingDTO getSysSetting(){
        return get(RedisConstant.CHAT_ADMIN_SYSTEM_SETTING,SysSettingDTO.class);
    }

    public void setSysSetting(SysSettingDTO sysSetting){
        set(RedisConstant.CHAT_ADMIN_SYSTEM_SETTING,sysSetting);
    }


    public List<String> getContactList(String userId){
        return stringRedisTemplate.opsForList().range(RedisConstant.CHAT_WS_USER_CONTACT + userId, 0, -1);
    }
    public void setContactList(String userId,List<String> allContactId){
        stringRedisTemplate.opsForList().leftPushAll(RedisConstant.CHAT_WS_USER_CONTACT+userId,allContactId);
        stringRedisTemplate.expire(RedisConstant.CHAT_WS_USER_CONTACT+userId,RedisConstant.CHAT_WS_TOKEN_EXPIRE,TimeUnit.SECONDS);
    }



}
