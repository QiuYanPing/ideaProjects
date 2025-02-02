package com.qyp.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.entity.Blog;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.domain.enums.MessageStatusEnum;
import com.qyp.chat.domain.enums.MessageTypeEnum;
import com.qyp.chat.exception.BusinessException;
import com.qyp.chat.exception.enums.ExceptionEnum;
import com.qyp.chat.mapper.BlogMapper;
import com.qyp.chat.mapper.UserMapper;
import com.qyp.chat.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * <p>
 * 博文表 服务实现类
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MessageHandler messageHandler;

    @Override
    public void saveBlog(Blog blog, MultipartFile[] files) {
        LocalDateTime time = LocalDateTime.now();
        User user = userMapper.selectById(blog.getUserId());
        if(user == null){
            throw new BusinessException(ExceptionEnum.OTHERS);
        }
        if(blog.getContent().length() > SysConstant.BLOG_CONTENT_SIZE)
            throw new BusinessException("发表内容太长！");


        if(files != null){
            String[] allFiles = new String[files.length];

            //处理files
            for (int i = 0; i < files.length; i++) {
                allFiles[i] = files[i].getOriginalFilename();
            }
            String join = StrUtil.join("|", allFiles);
            blog.setFile(join);
            //上传文件
        }

        //保存blog
        blog.setCreateTime(time);
        save(blog);

        //推送blog给好友
        List<String> contactList = redisUtils.getContactList(blog.getUserId());
        MessageDTO<Blog> messageDTO = new MessageDTO<>();
        messageDTO.setSendTime(time.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        messageDTO.setSendUserId(user.getUserId());
        messageDTO.setSendUserNickName(user.getNickName());
        messageDTO.setContactType(ContactTypeEnum.USER.getType());
        messageDTO.setMessageType(MessageTypeEnum.BLOG_PUBLISH.getType());
        messageDTO.setStatus(MessageStatusEnum.SENDED.getStatus());
        messageDTO.setExtendData(blog);

        for (String contactId : contactList) {
            messageDTO.setContactId(contactId);
            messageHandler.sendMessage(messageDTO);
        }

    }
}
