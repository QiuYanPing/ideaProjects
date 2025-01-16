package com.qyp.chat.controller;


import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.entity.AppUpdate;
import com.qyp.chat.domain.enums.AppUpdateStatusEnum;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.service.IAppUpdateService;
import com.qyp.chat.service.IApplyService;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 版本管理 前端控制器
 * 条件：
 * 1.新增的版本号要大于已有的最大版本号
 * 2.修改的版本号要小于已有的最大版本号 且 该版本号不存在
 * </p>
 *
 * @author 
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/app-update")
public class AppUpdateController {

    @Autowired
    IAppUpdateService appUpdateService;

    @PostMapping("/loadUpdate")
    public R loadUpdate(){
        List<AppUpdate> list = appUpdateService.lambdaQuery().orderByDesc(AppUpdate::getId).list();
        return R.success(list);
    }


    @PostMapping("/saveUpdate")
    public R saveUpate(@RequestPart AppUpdate appUpdate, @RequestPart MultipartFile file) throws IOException {
        //只有发布时才可以修改，将其置空，防止非法篡改
        appUpdate.setStatus(null);
        appUpdate.setGrayscaleUid(null);

        appUpdateService.saveUpdate(appUpdate,file);
        return R.success(null);
    }

    @PostMapping("/delUpdate")
    public R delUpdate(String id){
        AppUpdate version = appUpdateService.getById(id);
        AppUpdateStatusEnum statusEnum = AppUpdateStatusEnum.getByStatus(version.getStatus());
        if(statusEnum == null )
            throw new BusinessException(ExceptionEnum.OTHERS);

        if(ArrayUtil.contains(new Integer[]{
                AppUpdateStatusEnum.GRAYSCALE.getStatus(),AppUpdateStatusEnum.ALL.getStatus()
        },version.getStatus()))
            throw new BusinessException("版本已发布，不可删除！");

        appUpdateService.removeById(id);
        return R.success(null);
    }

    @PostMapping("/postUpdate")
    public R postUpdate(Integer id,Integer status,String grayScaleUid){
        appUpdateService.postUpdate(id,status,grayScaleUid);
        return R.success(null);
    }

}
