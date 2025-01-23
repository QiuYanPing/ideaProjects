package com.qyp.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.qyp.chat.domain.entity.Session;
import com.qyp.chat.mapper.SessionMapper;
import com.qyp.chat.service.ISessionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 会话 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session> implements ISessionService {


}
