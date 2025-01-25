package com.qyp.chat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.vo.AppUpdateVO;
import com.qyp.chat.domain.vo.UserVO;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.ContactMapper;
import com.qyp.chat.service.IAppUpdateService;
import com.qyp.chat.service.IUserService;
import com.qyp.chat.util.UserUtils;
import com.qyp.chat.websocket.ChannelContextUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Autowired
    IAppUpdateService appUpdateService;
    @Autowired
    ChannelContextUtils channelContextUtils;

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


    @PostMapping("/getMyself")
    public R getMyself(){
        UserInfoDTO userInfoDTO = userUtils.get();
        String userId = userInfoDTO.getUserId();
        User me = userService.getById(userId);
        UserVO userVO = BeanUtil.toBean(me, UserVO.class);
        userVO.setAdmin(userInfoDTO.getAdmin());
        return R.success(userVO);
    }

    @PostMapping("/saveMyself")
    public R saveMyself(@RequestPart User user, @RequestPart MultipartFile avatarFile ,@RequestPart MultipartFile avatarCover) throws IOException {
        UserInfoDTO userInfoDTO = userUtils.get();
        //一些禁止修改的字段可以置空
        user.setUserId(userInfoDTO.getUserId());
        user.setPassword(null);
        user.setCreateTime(null);
        userService.saveMyself(user,avatarFile,avatarCover);
        return R.success(user);
    }


    @PostMapping("/updatePassword")
    public R udpatePassword(String password){
        String userId = userUtils.get().getUserId();
        User user = new User();
        user.setUserId(userId);
        user.setPassword(DigestUtils.md5Hex(password));
        userService.updateById(user);
        //todo 强制退出，关闭ws连接
        channelContextUtils.closeContact(userId);
        return R.success(null);
    }

    @PostMapping("/logout")
    public R logout(){
        UserInfoDTO userInfoDTO = userUtils.get();
        //todo 强制退出，关闭ws连接
        channelContextUtils.closeContact(userInfoDTO.getUserId());
        return R.success(null);
    }


    @PostMapping("/checkUpdate")
    public R checkUpdate(String appVersion){
        String userId = userUtils.get().getUserId();
        AppUpdateVO appUpdateVO = appUpdateService.checkUpdate(appVersion,userId);
        return R.success(appUpdateVO);
    }




}
