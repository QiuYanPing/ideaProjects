package com.qyp.chat.mapper;

import com.qyp.chat.domain.entity.UserSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户会话 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface UserSessionMapper extends BaseMapper<UserSession> {

    List<UserSession> selectAllUserSession(String userId);
}
