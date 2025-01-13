package com.qyp.chat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.vo.UserVO;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.ContactMapper;
import com.qyp.chat.service.IUserService;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;
    @Autowired
    ContactMapper contactMapper;
    @Autowired
    UserUtils userUtils;

    @PostMapping("/getUserInfo")
    public R getUserInfo(String userId){
        UserInfoDTO userInfoDTO = userUtils.get();
        String me = userInfoDTO.getUserId();
        //陌生人也可以获取
        User user = userService.getById(userId);
        UserVO userVO = BeanUtil.toBean(user, UserVO.class);
        userVO.setStatus(ContactStatusEnum.FRIEND.getStatus());

        LambdaQueryChainWrapper<Contact> queryChainWrapper = new LambdaQueryChainWrapper<>(contactMapper);
        Contact contact = queryChainWrapper.eq(Contact::getUserId, me)
                .eq(Contact::getContactId, userId).one();


        if(contact == null || !ArrayUtil.contains(new Integer[]{
                ContactStatusEnum.FRIEND.getStatus(),ContactStatusEnum.DEL_BE.getStatus(),ContactStatusEnum.BLACKlIST_BE.getStatus()
        },contact.getStatus())){
            //不是好友
            userVO.setStatus(ContactStatusEnum.NOT_FRIEND.getStatus());
        }
        return R.success(userVO);
    }


    @PostMapping("/getContactUserInfo")
    public R getContactUserInfo(String userId){
        UserInfoDTO userInfoDTO = userUtils.get();
        String me = userInfoDTO.getUserId();


        LambdaQueryChainWrapper<Contact> queryChainWrapper = new LambdaQueryChainWrapper<>(contactMapper);
        Contact contact = queryChainWrapper.eq(Contact::getUserId, me)
                .eq(Contact::getContactId, userId).one();


        if(contact == null || !ArrayUtil.contains(new Integer[]{
                ContactStatusEnum.FRIEND.getStatus(),ContactStatusEnum.DEL_BE.getStatus(),ContactStatusEnum.BLACKlIST_BE.getStatus()
        },contact.getStatus())){
            //不是好友
            throw new BusinessException(ExceptionEnum.OTHERS);
        }

        User user = userService.getById(userId);
        UserVO userVO = BeanUtil.toBean(user, UserVO.class);
        userVO.setStatus(ContactStatusEnum.FRIEND.getStatus());
        return R.success(userVO);

    }



}
