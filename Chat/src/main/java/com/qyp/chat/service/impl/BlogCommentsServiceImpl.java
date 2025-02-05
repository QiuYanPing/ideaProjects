package com.qyp.chat.service.impl;

import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Blog;
import com.qyp.chat.domain.entity.BlogComments;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.domain.enums.MessageStatusEnum;
import com.qyp.chat.domain.enums.MessageTypeEnum;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.mapper.BlogCommentsMapper;
import com.qyp.chat.mapper.BlogMapper;
import com.qyp.chat.mapper.UserMapper;
import com.qyp.chat.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.UserUtils;
import com.qyp.chat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * <p>
 * 博文评论表 服务实现类
 * </p>
 *
 * @author
 * @since 2025-02-01
 */
@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogCommentsMapper blogCommentsMapper;
    @Autowired
    MessageHandler messageHandler;
    @Autowired
    UserUtils userUtils;
    @Autowired
    RedisUtils redisUtils;

    @Override
    public void comment(String userId, Integer blogId, String comment) {
        LocalDateTime time = LocalDateTime.now();
        if (comment == null)
            throw new BusinessException("评论不能为空");
        BlogComments blogComments = new BlogComments();
        blogComments.setBlogId(blogId);
        blogComments.setUserId(userId);
        blogComments.setComment(comment);
        blogComments.setLikes(0);
        blogComments.setCreateTime(time);
        save(blogComments);

        //通知发表博文的作者,有用户对其博文进行评价
        User user = userMapper.selectById(userId);
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null)
            throw new BusinessException("博文不存在！");

        MessageDTO<BlogComments> blogCommentsMessageDTO = new MessageDTO<>();
        blogCommentsMessageDTO.setSendUserId(userId);
        blogCommentsMessageDTO.setSendUserNickName(user.getNickName());
        blogCommentsMessageDTO.setSendTime(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        blogCommentsMessageDTO.setContactId(blog.getUserId());
        blogCommentsMessageDTO.setContactType(ContactTypeEnum.USER.getType());
        blogCommentsMessageDTO.setMessageType(MessageTypeEnum.BLOG_COMMENT.getType());
        blogCommentsMessageDTO.setStatus(MessageStatusEnum.SENDED.getStatus());
        blogCommentsMessageDTO.setExtendData(blogComments);
        messageHandler.sendMessage(blogCommentsMessageDTO);

    }

    @Override
    public void removeComment(String userId, Long commentId) {
        BlogComments comment = getById(commentId);
        if (comment == null)
            throw new BusinessException("评论不存在");
        //判断用户是否有权限删除该评论
        Blog blog = blogMapper.selectById(comment.getBlogId());
        if (!userId.equals(comment.getUserId()) && !userId.equals(blog.getUserId()))
            throw new BusinessException("没有权限删除评论");

        //删除
        removeById(commentId);

        if (!userId.equals(comment.getUserId())) {
            //通知发表评论的人，评论被删除
            User user = userMapper.selectById(userId);
            MessageDTO<BlogComments> blogCommentsMessageDTO = new MessageDTO<>();
            blogCommentsMessageDTO.setSendTime(System.currentTimeMillis());
            blogCommentsMessageDTO.setSendUserId(userId);
            blogCommentsMessageDTO.setSendUserNickName(user.getNickName());
            blogCommentsMessageDTO.setContactId(comment.getUserId());
            blogCommentsMessageDTO.setContactType(ContactTypeEnum.USER.getType());
            blogCommentsMessageDTO.setMessageType(MessageTypeEnum.COMMENT_REMOVE.getType());
            blogCommentsMessageDTO.setStatus(MessageStatusEnum.SENDED.getStatus());
            blogCommentsMessageDTO.setExtendData(comment);
            messageHandler.sendMessage(blogCommentsMessageDTO);
        }
    }

    @Override
    public List<BlogComments> loadComments(Integer blogId) {
        UserInfoDTO userInfoDTO = userUtils.get();
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null)
            throw new BusinessException("博文不存在");

        //查询博文的所有评论，以及发表评论的用户昵称
        List<BlogComments> blogCommentsList = blogCommentsMapper.selectAllComments(blogId);
        //判断用户是否点赞了该博文
        isLike(blogCommentsList,userInfoDTO.getUserId());

        return blogCommentsList;
    }

    private void isLike(List<BlogComments> blogCommentsList, String userId) {
        for (BlogComments comments : blogCommentsList) {
            Long commentId = comments.getCommentId();
            boolean like = redisUtils.isLike(commentId, userId);
            if(like)
                comments.setIsLike(true);
            else
                comments.setIsLike(false);
        }
    }

    @Override
    public void likeComment(String userId, Long commentId) {
        BlogComments comment = getById(commentId);
        if (comment == null)
            throw new BusinessException("评论不存在");

        //判断是否点赞过
        boolean like = redisUtils.isLike(commentId, userId);
        if (like) {
            //取消点赞
            lambdaUpdate().setSql("likes = likes - 1").eq(BlogComments::getCommentId, commentId).update();
            redisUtils.removeCommentLike(commentId, userId);
        } else {
            //点赞
            lambdaUpdate().setSql("likes = likes + 1").eq(BlogComments::getCommentId, commentId).update();
            redisUtils.setCommentLike(commentId, userId);
        }

    }
}
