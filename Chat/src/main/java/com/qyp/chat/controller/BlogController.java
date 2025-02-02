package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Blog;
import com.qyp.chat.service.IBlogService;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 博文表 前端控制器
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    IBlogService blogService;
    @Autowired
    UserUtils userUtils;


    @PostMapping("/saveBlog")
    public R saveBlog(String content, MultipartFile[] files){
        UserInfoDTO userInfoDTO = userUtils.get();
        Blog blog = new Blog();
        blog.setUserId(userInfoDTO.getUserId());
        blog.setContent(content);
        blogService.saveBlog(blog,files);
        return R.success(null);
    }

}
