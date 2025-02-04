package com.qyp.chat.controller;


import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.BlogComments;
import com.qyp.chat.service.IBlogCommentsService;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 博文评论表 前端控制器
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@RestController
@RequestMapping("/blog-comments")
public class BlogCommentsController {

    @Autowired
    UserUtils userUtils;

    @Autowired
    IBlogCommentsService blogCommentsService;
    @PostMapping("/comment")
    public R comment(Integer blogId,String comment){
        UserInfoDTO userInfoDTO = userUtils.get();
        blogCommentsService.comment(userInfoDTO.getUserId(),blogId,comment);
        return R.success(null);
    }


    @PostMapping("/removeComment")
    public R removeComment(Long commentId){
        UserInfoDTO userInfoDTO = userUtils.get();
        blogCommentsService.removeComment(userInfoDTO.getUserId(),commentId);
        return R.success(null);
    }


    @PostMapping("/loadComments")
    public R loadComments(Integer blogId){
        List<BlogComments> blogCommentsList= blogCommentsService.loadComments(blogId);
        return R.success(blogCommentsList);
    }

    @PostMapping("/like")
    public R likeComment(Long commentId){
        UserInfoDTO userInfoDTO = userUtils.get();
        blogCommentsService.likeComment(userInfoDTO.getUserId(),commentId);
        return R.success(null);
    }


}
