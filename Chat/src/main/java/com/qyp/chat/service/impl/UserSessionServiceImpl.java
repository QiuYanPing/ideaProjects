package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.UserSession;
import com.qyp.chat.mapper.UserSessionMapper;
import com.qyp.chat.service.IUserSessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户会话 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class UserSessionServiceImpl extends ServiceImpl<UserSessionMapper, UserSession> implements IUserSessionService {

}
