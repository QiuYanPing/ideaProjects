package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.ContactSearchDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.service.IContactService;
import com.qyp.chat.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping("/search")
    public R search(String contactId){
        UserInfoDTO userInfoDTO = userUtils.get();
        String userId = userInfoDTO.getUserId();
        ContactSearchDTO contactSearchDTO = contactService.search(userId,contactId);
        return R.success(contactSearchDTO);
    }

}
