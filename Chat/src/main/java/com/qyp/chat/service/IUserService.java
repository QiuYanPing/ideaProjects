package com.qyp.chat.service;

import com.qyp.chat.domain.R;
import com.qyp.chat.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.query.UserRegisterQuery;
import com.qyp.chat.exception.BusinessException;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface IUserService extends IService<User> {

    void register(UserRegisterQuery user) ;

    R login(UserRegisterQuery user) ;


    void removeContact(String contactId, ContactStatusEnum del);
}
