package com.qyp.chat.service;

import com.qyp.chat.domain.entity.Group;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 群聊 服务类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface IGroupService extends IService<Group> {

    void saveGroup(Group group, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException;

    List<Group> loadMyGroup(String userId);
}
