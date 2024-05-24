package com.javaee.controller;

import com.javaee.pojo.Result;
import com.javaee.pojo.User;
import com.javaee.service.UserService;
import com.javaee.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    UserService userService;
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        log.info("用户登录：{}",user);
        User e = userService.login(user);
        if(e!=null){
            Map<String, Object> claims = new HashMap<>();
            claims.put("id",e.getId());
            claims.put("userName",e.getUserName());
            claims.put("name",e.getName());
            String jwt = JwtUtils.generateJwt(claims);
            return Result.success(jwt);
        }
        return Result.error("用户或者密码错误");
    }
}
