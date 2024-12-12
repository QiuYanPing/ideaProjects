package com.qyp.chat.controller;

import com.qyp.chat.ChatApplication;
import com.qyp.chat.constant.RedisConstant;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.query.UserRegisterQuery;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.service.IUserService;
import com.qyp.chat.util.RedisUtils;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    IUserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @PostMapping("/checkCode")
    public R checkCode(){
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100,43);
        String code = captcha.text();
        String image = captcha.toBase64();
        String token = UUID.randomUUID().toString();
        //验证码一分钟过期
        redisUtils.set(RedisConstant.CHAT_ACCOUNT_CHECK_CODE+token,code,
                RedisConstant.CHAT_ACCOUNT_CHECK_CODE_EXPIRES,
                TimeUnit.SECONDS);
        ConcurrentHashMap<String,String> data = new ConcurrentHashMap<>();
        data.put("image",image);
        data.put("codeToken",token);
        log.info("获取code：{}",code);
        return R.success(data);
    }

    @PostMapping("/register")
    public R register(HttpServletRequest request,@RequestBody UserRegisterQuery user){
        String token = request.getHeader("codeToken");
        //检验验证码
        String trueCode = stringRedisTemplate.opsForValue().get(RedisConstant.CHAT_ACCOUNT_CHECK_CODE + token);
        String code = user.getCode();
        if(!trueCode.equals(code))
            return R.error("校验码错误!");
        //注册
        userService.register(user);
        return R.success(null);
    }


    @PostMapping("/login")
    public R login(HttpServletRequest request,@RequestBody UserRegisterQuery user) {
        String token = request.getHeader("token");
        //检验验证码
        String trueCode = stringRedisTemplate.opsForValue().get(RedisConstant.CHAT_ACCOUNT_CHECK_CODE + token);
        String code = user.getCode();
        if(trueCode!=null && !trueCode.equals(code))
            return R.error("校验码错误!");
        //注册
        return userService.login(user);
    }


}
