package com.qyp.chat.service;

import com.qyp.chat.domain.entity.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 博文表 服务类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
public interface IBlogService extends IService<Blog> {

    void saveBlog(Blog blog, MultipartFile[] files) throws IOException;

    void removeBlog(Integer id);

    List<Blog> getBlogs(String userId, Integer pageNo, Integer pageSize);

    List<Blog> loadMyBlogs();

    void likeBlog(String userId, Integer blogId);
}
