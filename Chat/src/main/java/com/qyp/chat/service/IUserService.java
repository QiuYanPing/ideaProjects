package com.qyp.chat.service;

import com.qyp.chat.domain.R;
import com.qyp.chat.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.query.UserRegisterQuery;
import com.qyp.chat.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    void saveMyself(User user, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException;

    void updateUserStatus(String status, String userId);

    void forceOffLine(String userId);

    void sign(String userId);

    Integer continueDays(String userId);
}
