package com.javaee.service;

import com.javaee.pojo.PageBean;
import com.javaee.pojo.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    PageBean list(Integer page, Integer pageSize,
                  String userName, String name, Integer gender,
                  LocalDateTime begin, LocalDateTime end);
    User selectById(int id);
    void insert(User user);
    void update(User user);
    void delete(List<Integer> ids);

    User login(User user);
}
