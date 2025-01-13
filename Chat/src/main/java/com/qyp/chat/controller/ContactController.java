package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.ContactSearchDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.service.IContactService;
import com.qyp.chat.service.IUserService;
import com.qyp.chat.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 联系人 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@RestController
@RequestMapping("/contact")
@Slf4j
public class ContactController {
    @Autowired
    IContactService contactService;
    @Autowired
    UserUtils userUtils;
    @Autowired
    IUserService userService;


    @PostMapping("/search")
    public R search(String contactId){
        UserInfoDTO userInfoDTO = userUtils.get();
        String userId = userInfoDTO.getUserId();
        ContactSearchDTO contactSearchDTO = contactService.search(userId,contactId);
        return R.success(contactSearchDTO);
    }


    @PostMapping("/loadContact")
    public R loadContact(String contactType){
        ContactTypeEnum type = ContactTypeEnum.getByName(contactType);
        List<Contact> contactList = contactService.loadContact(type);
        return R.success(contactList);
    }

    @PostMapping("/delContactUser")
    public R delContactUser(String contactId){
        userService.removeContact(contactId, ContactStatusEnum.DEL);
        return R.success(null);
    }

    @PostMapping("/blackListContact")
    public R blackListContact(String contactId){
        userService.removeContact(contactId,ContactStatusEnum.BLACKlIST);
        return R.success(null);
    }

}
