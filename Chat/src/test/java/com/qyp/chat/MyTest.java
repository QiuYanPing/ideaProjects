package com.qyp.chat;


import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.SysConstant;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
public class MyTest {
    @Resource
    StringRedisTemplate stringRedisTemplate ;
    @Autowired
    AppConfig appConfig;

    @Test
    public void test(){
        stringRedisTemplate.opsForValue().set("age","12");

        String age = stringRedisTemplate.opsForValue().get("age");
        System.out.println("age = " + age);
    }

    @Test
    public void testMd5(){
        String s = "qyp";
        String message = DigestUtils.md5Hex(s);
        System.out.println("message = " + message);

        //解密
    }
    @Test
    public void testFolder(){
        //上传群聊头像
        String projectFolder = appConfig.getProjectFolder();
        String path = projectFolder + SysConstant.FILE_FOLDER_FILE + SysConstant.FILE_FOLDER_AVATAR;
        File fileFolder = new File(path);
        if(!fileFolder.exists()){
            //文件夹不存在
            fileFolder.mkdirs();
        }
    }

    @Test
    public void testApplyInfo(){
        String s = String.format(SysConstant.APPLY_INFO, "qiuyanping");
        System.out.println("s = " + s);
    }

    @Test
    public void testTime(){
        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());
        LocalDateTime time = LocalDateTime.now();
        System.out.println("time = " + time);
        System.out.println("time.toEpochSecond(ZoneOffset.UTC) = " + time.toEpochSecond(ZoneOffset.of("+8")));
        System.out.println("time.toInstant(ZoneOffset.UTC).toEpochMilli() = " + time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }
}
