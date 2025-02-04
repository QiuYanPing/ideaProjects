package com.qyp.chat.service;

import com.qyp.chat.domain.entity.BlogComments;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 博文评论表 服务类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
public interface IBlogCommentsService extends IService<BlogComments> {

    void comment(String userId, Integer blogId, String comment);

    void removeComment(String userId, Long commentId);

    List<BlogComments> loadComments(Integer blogId);

    void likeComment(String userId, Long commentId);
}
