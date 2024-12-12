package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Group;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/saveGroup")
    public R saveGroup(@RequestBody Group group){
        UserInfoDTO userInfoDTO = userUtils.get();
        return R.success(null);
    }


}
