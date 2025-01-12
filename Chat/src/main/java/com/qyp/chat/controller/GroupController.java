package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Group;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.vo.GroupVO;
import com.qyp.chat.service.IGroupService;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 群聊 前端控制器
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    UserUtils userUtils;

    @Autowired
    IGroupService groupService;

    @PostMapping("/saveGroup")
    public R saveGroup(@RequestPart Group group,
                       @RequestPart MultipartFile avatarFile,
                       @RequestPart MultipartFile avatarCover) throws IOException {
        UserInfoDTO userInfoDTO = userUtils.get();
        //当前用户为群主
        group.setGroupOwnerId(userInfoDTO.getUserId());
        groupService.saveGroup(group,avatarFile,avatarCover);
        return R.success(null);
    }


    @PostMapping("/loadMyGroup")
    public R loadMyGroup(){
        UserInfoDTO userInfoDTO = userUtils.get();
        List<Group> groupList = groupService.loadMyGroup(userInfoDTO.getUserId());
        return R.success(groupList);
    }

    @PostMapping("/getGroupInfo")
    public R getGroupInfo(String groupId){
        UserInfoDTO userInfoDTO = userUtils.get();
        Group group = groupService.getGroupInfo(userInfoDTO.getUserId(),groupId);
        return R.success(group);
    }

    @PostMapping("/getGroupInfoDetail")
    public R getGroupInfoDetail(String groupId){
        UserInfoDTO userInfoDTO = userUtils.get();
        GroupVO groupVO = groupService.getGroupInfoDetail(userInfoDTO.getUserId(),groupId);
        return R.success(groupVO);
    }

}
