package com.javaee.controller;

import com.javaee.anno.Log;
import com.javaee.pojo.PageBean;
import com.javaee.pojo.Result;
import com.javaee.pojo.User;
import com.javaee.service.UserService;
import com.javaee.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Value("${myToken}")
    String jwt;
    @GetMapping("/user")
    public Result list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       String userName, String name, Integer gender,
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime begin,
                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime end){
        log.info("分页查询，参数{},{}",page,pageSize);
        PageBean pageBean = userService.list(page,pageSize,userName,name,gender,begin,end);
        return Result.success(pageBean);
    }
    @GetMapping("/user/{id}")
    public Result selectById(@PathVariable int id){
        User user = userService.selectById(id);
        log.info("根据id查询用户：{}",id);
        return Result.success(user);
    }
    @Log
    @PostMapping("/user")
    public Result insert(@RequestBody User user){
        userService.insert(user);
        log.info("添加用户id:{}",user.getId());
        return Result.success();
    }
    @Log
    @PutMapping("/user")
    public Result update(@RequestBody User user){
        userService.update(user);
        log.info("修改用户id:{}",user.getId());
        return Result.success();
    }
    @Log
    @DeleteMapping("/user/{ids}")
    public Result delete(@PathVariable List<Integer> ids){
        userService.delete(ids);
        log.info("根据ids删除用户：{}",ids);
        return Result.success();
    }
    @GetMapping("/myself")
    public Result getMyself(){
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer id = (Integer) claims.get("id");
        User user = userService.selectById(id);
        log.info("根据id查询用户：{}",id);
        return Result.success(user);
    }

}
