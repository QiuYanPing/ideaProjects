package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.User;
import com.qyp.chat.mapper.UserMapper;
import com.qyp.chat.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
