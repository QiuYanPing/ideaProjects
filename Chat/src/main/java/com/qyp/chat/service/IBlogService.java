package com.qyp.chat.service;

import com.qyp.chat.domain.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 博文表 服务类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
public interface IBlogService extends IService<Blog> {

    void saveBlog(Blog blog, MultipartFile[] files);
}
