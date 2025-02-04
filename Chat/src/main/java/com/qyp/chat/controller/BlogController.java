package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Blog;
import com.qyp.chat.service.IBlogService;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public R saveBlog(String content, MultipartFile[] files) throws IOException {
        UserInfoDTO userInfoDTO = userUtils.get();
        Blog blog = new Blog();
        blog.setUserId(userInfoDTO.getUserId());
        blog.setContent(content);
        blogService.saveBlog(blog,files);
        return R.success(null);
    }

    @PostMapping("/removeBlog")
    public R removeBlog(Integer id){
        blogService.removeBlog(id);
        return R.success(null);
    }


    @PostMapping("/getBlogs")
    public R getBlogs(String userId, @RequestParam(defaultValue = "1") Integer pageNo ,
                      @RequestParam(defaultValue = "10") Integer pageSize){
        List<Blog> blogList = blogService.getBlogs(userId,pageNo,pageSize);
        return R.success(blogList);
    }

    @PostMapping("/loadMyBlogs")
    public R loadMyBlogs (){
        List<Blog> blogList = blogService.loadMyBlogs();
        return R.success(blogList);
    }


    @PostMapping("/likeBlog")
    public R likeBlog(Integer blogId){
        UserInfoDTO userInfoDTO = userUtils.get();
        blogService.likeBlog(userInfoDTO.getUserId(),blogId);
        return R.success(null);
    }
}
