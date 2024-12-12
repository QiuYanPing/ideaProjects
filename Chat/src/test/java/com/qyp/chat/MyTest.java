package com.qyp.chat;


import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
@SpringBootTest
public class MyTest {
    @Resource
    StringRedisTemplate stringRedisTemplate ;
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
}
