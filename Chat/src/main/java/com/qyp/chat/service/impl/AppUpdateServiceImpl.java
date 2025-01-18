package com.qyp.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.entity.AppUpdate;
import com.qyp.chat.domain.enums.AppUpdateStatusEnum;
import com.qyp.chat.domain.enums.AppUpdateTypeEnum;
import com.qyp.chat.domain.vo.AppUpdateVO;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.AppUpdateMapper;
import com.qyp.chat.service.IAppUpdateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 版本管理 服务实现类
 * </p>
 *
 * @author
 * @since 2025-01-16
 */
@Service
public class AppUpdateServiceImpl extends ServiceImpl<AppUpdateMapper, AppUpdate> implements IAppUpdateService {

    @Autowired
    AppConfig appConfig;

    @Autowired
    AppUpdateMapper appUpdateMapper;
    @Override
    public void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        //查询数据库中最大的版本号
        List<AppUpdate> list = lambdaQuery().orderByDesc(AppUpdate::getVersion).list();
        AppUpdate latestUpdate = null;
        Long maxVersion = null;
        if(list.isEmpty()) {
            maxVersion = Long.MIN_VALUE;
        }else{
            latestUpdate = list.get(0);
            maxVersion= Long.parseLong(latestUpdate.getVersion().replace(".", ""));
        }
        long currentVersion = Long.parseLong(appUpdate.getVersion().replace(".", ""));
        if (appUpdate.getId() == null) {
            //新增：版本号要大于最大版本号
            if (currentVersion <= maxVersion)
                throw new BusinessException("当前版本号必须大于最大历史版本号");

            appUpdate.setCreateTime(now);
            appUpdate.setStatus(AppUpdateStatusEnum.INIT.getStatus());
            save(appUpdate);
        } else {
            //修改：修改的版本号要小于最大版本号 且 该版本号不存在
            AppUpdate currentUpdate = getById(appUpdate.getId());
            if (ArrayUtil.contains(new Integer[]{
                    AppUpdateStatusEnum.GRAYSCALE.getStatus(), AppUpdateStatusEnum.ALL.getStatus()
            }, currentUpdate.getStatus())) {
                throw new BusinessException("版本已发布，不可修改！");
            }

            if(currentVersion >= maxVersion && !appUpdate.getId().equals(latestUpdate.getId()) )
                throw new BusinessException("当前版本必须低于最大历史版本号");
            AppUpdate one = lambdaQuery().eq(AppUpdate::getVersion, appUpdate.getVersion()).one();
            if(one != null)
                throw new BusinessException("版本号已存在");

            updateById(appUpdate);
        }

        if(file!=null){
            //上传版本
            String basePath = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_APP;
            File baseFolder = new File(basePath);
            if(!baseFolder.exists())
                baseFolder.mkdirs();
            String fileName = baseFolder.getPath() + "/" + appUpdate.getId() + SysConstant.APP_UPDATE_SUFFIX;
            file.transferTo(new File(fileName));
        }
    }

    @Override
    public void postUpdate(Integer id, Integer status, String grayScaleUid) {
        AppUpdate appUpdate = new AppUpdate();
        appUpdate.setId(id);
        appUpdate.setStatus(status);
        appUpdate.setGrayscaleUid(grayScaleUid);

        AppUpdateStatusEnum statusEnum = AppUpdateStatusEnum.getByStatus(status);
        if(statusEnum == null) {
            throw new BusinessException(ExceptionEnum.OTHERS);
        }

        if(AppUpdateStatusEnum.GRAYSCALE == statusEnum && grayScaleUid == null) {
            throw new BusinessException("未设置灰度用户id");
        }

        if(AppUpdateStatusEnum.GRAYSCALE != statusEnum) {
            appUpdate.setGrayscaleUid("");
        }

        updateById(appUpdate);
    }

    @Override
    public AppUpdateVO checkUpdate(String appVersion, String userId) {
        if(appVersion == null)
            return null;

        AppUpdate appUpdate = appUpdateMapper.getLasterUpdate(appVersion,userId);
        if(appUpdate == null)
            return null;

        AppUpdateVO appUpdateVO = BeanUtil.toBean(appUpdate, AppUpdateVO.class);
        String fileName = SysConstant.CHAT_SETUP + appUpdateVO.getVersion() + SysConstant.APP_UPDATE_SUFFIX;
        appUpdateVO.setFileName(fileName);
        appUpdateVO.setUpdateDescArray(appUpdate.getUpdateDescArray());

        if(AppUpdateTypeEnum.LOCAL.getType().equals(appUpdate.getFileType())){
            File file = new File(appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_APP + appUpdate.getId() + SysConstant.APP_UPDATE_SUFFIX);
            appUpdateVO.setSize(file.length());
        }else{
            //外链
            appUpdateVO.setSize(0L);
        }
        return appUpdateVO;

    }
}
