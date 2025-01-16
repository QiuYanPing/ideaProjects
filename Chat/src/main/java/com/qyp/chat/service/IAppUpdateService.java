package com.qyp.chat.service;

import com.qyp.chat.domain.entity.AppUpdate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 * 版本管理 服务类
 * </p>
 *
 * @author 
 * @since 2025-01-16
 */
public interface IAppUpdateService extends IService<AppUpdate> {

    void saveUpdate(AppUpdate appUpdate, MultipartFile file) throws IOException;

    void postUpdate(Integer id, Integer status, String grayScaleUid);
}
