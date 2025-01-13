package com.qyp.chat.service;

import com.qyp.chat.domain.dto.ContactSearchDTO;
import com.qyp.chat.domain.entity.Contact;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qyp.chat.domain.enums.ContactTypeEnum;

import java.util.List;

/**
 * <p>
 * 联系人 服务类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface IContactService extends IService<Contact> {

    ContactSearchDTO search(String userId, String contactId);


    List<Contact> loadContact(ContactTypeEnum type);
}
