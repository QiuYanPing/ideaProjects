package com.qyp.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.SysSettingDTO;
import com.qyp.chat.domain.entity.Group;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.service.IGroupService;
import com.qyp.chat.service.IUserService;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.UserUtils;
import net.bytebuddy.utility.nullability.AlwaysNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    IUserService userService;

    @Autowired
    IGroupService groupService;

    @Autowired
    UserUtils userUtils;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    AppConfig appConfig;

    @PostMapping("/loadUser")
    public R loadUser(){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(User::getCreateTime);
        List<User> list = userService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/updateUserStatus")
    public R updateUserStatus(String status,String userId){
        userService.updateUserStatus(status,userId);
        return  R.success(null);
    }


    @PostMapping("/forceOffLine")
    public R forceOffLine (String userId){
        userService.forceOffLine(userId);
        return R.success(null);
    }


    @PostMapping("/loadGroup")
    public R loadGroup(){
        List<Group> groupList = groupService.loadGroup();
        return R.success(groupList);
    }


    @PostMapping("/dissolutionGroup")
    public R dissolutionGroup(String groupId){
        Group group = groupService.getById(groupId);
        if(group == null)
            throw new BusinessException(ExceptionEnum.OTHERS);

        groupService.dissolutionGroup(group.getGroupOwnerId(),groupId);
        return R.success(null);
    }


    @PostMapping("/getSysSetting")
    public R getSysSetting (){
        SysSettingDTO sysSetting = userUtils.getSysSetting();
        return R.success(sysSetting);
    }

    @PostMapping("/saveSysSetting")
    public R saveSysSetting(@RequestPart SysSettingDTO sysSettingDTO,
                            @RequestPart MultipartFile avatarFile ,
                            @RequestPart MultipartFile avatarCover) throws IOException {

        if(avatarFile != null){
            String basePath = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE + SysConstant.FILE_FOLDER_AVATAR;
            File file = new File(basePath);
            if(!file.exists())
                file.mkdirs();

            String filePath = file.getPath()+"/"+sysSettingDTO.getRobotUid()+SysConstant.IMAGE_SUFFIX;
            String coverPath = file.getPath() + "/" + sysSettingDTO.getRobotUid() + SysConstant.COVER_IMAGE_SUFFIX;
            avatarFile.transferTo(new File(filePath));
            avatarCover.transferTo(new File(coverPath));
        }

        redisUtils.setSysSetting(sysSettingDTO);
        return R.success(null);
    }
}
