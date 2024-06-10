package com.javaee.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javaee.mapper.UserMapper;
import com.javaee.pojo.PageBean;
import com.javaee.pojo.User;
import com.javaee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public PageBean list(Integer page, Integer pageSize,
                         String userName, String name, Integer gender,
                         LocalDateTime begin, LocalDateTime end) {
        //1.设置分页参数
        PageHelper.startPage(page,pageSize);
        //2.执行分页查询
        List<User> userList = userMapper.list(userName,name,gender,begin,end);
        Page<User> p = (Page<User>) userList;
        //3.封装PageBean对象，并返回
        PageBean pageBean = new PageBean(p.getTotal(), p.getResult());
        return pageBean;
    }

    @Override
    public User selectById(int id) {
        User user = userMapper.selectById(id);
        return user;
    }

    @Override
    public void insert(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }



    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        String updateTime = user.getUpdateTime().toString();
        String userName = user.getUserName();
        String password = user.getPassword();
        String name = user.getName();
        int gender = user.getGender();
        String image = user.getImage();
        String site = user.getSite();
        int id = user.getId();
        userMapper.update(userName,password,name,gender,image,site,updateTime,id);
    }

    @Override
    public void delete(List<Integer> ids) {
        userMapper.delete(ids);
    }

    @Override
    public User login(User user) {
        return userMapper.selectByUserNamePassword(user);
    }
}
