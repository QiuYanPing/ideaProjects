package com.qyp.chat.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qyp.chat.config.AppConfig;
import com.qyp.chat.constant.RedisConstant;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
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
import com.qyp.chat.util.FileUtils;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.StringUtils;
import com.qyp.chat.util.UserUtils;
import com.qyp.chat.websocket.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    UserUtils userUtils;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    MessageHandler messageHandler;
    @Autowired
    AppConfig appConfig;
    @Autowired
    StringUtils stringUtils;
    @Autowired
    FileUtils fileUtils;

    @Override
    @Transactional
    public void saveBlog(Blog blog, MultipartFile[] files) throws IOException {
        LocalDateTime time = LocalDateTime.now();
        User user = userMapper.selectById(blog.getUserId());
        if(user == null){
            throw new BusinessException(ExceptionEnum.OTHERS);
        }
        if(blog.getContent().length() > SysConstant.BLOG_CONTENT_SIZE)
            throw new BusinessException("发表内容太长！");

        //保存blog
        blog.setLikes(0);
        blog.setCreateTime(time);
        save(blog);

        if(files != null){
            String[] allFiles = new String[files.length];
            //上传文件
            String path = appConfig.getProjectFolder() + SysConstant.FILE_FOLDER_FILE+SysConstant.FILE_FOLDER_BLOG;
            for (int i = 0; i < files.length; i++) {
                String suffix = stringUtils.getSuffix(files[i].getOriginalFilename());
                String fileName = blog.getBlogId()+"-"+i+suffix;
                fileUtils.uplodFile(files[i],path,fileName);
                allFiles[i] = "/"+SysConstant.FILE_FOLDER_BLOG+fileName;
            }
            //处理files
            String join = StrUtil.join("|", allFiles);
            blog.setFile(join);
            lambdaUpdate().set(Blog::getFile,join)
                    .eq(Blog::getBlogId,blog.getBlogId()).update();
        }



        //推送blog给好友
        List<String> contactList = redisUtils.getContactList(blog.getUserId());
        contactList = contactList.stream().filter(item -> ContactTypeEnum.USER == ContactTypeEnum.getByPrefix(item))
                .collect(Collectors.toList());
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

    @Override
    public void removeBlog(Integer id) {
        //判断当前用户是否为发表者
        UserInfoDTO userInfoDTO = userUtils.get();
        Blog blog = getById(id);
        if(blog == null)
            throw new BusinessException("博文不存在");
        if(!userInfoDTO.getUserId().equals(blog.getUserId()))
            throw new BusinessException("没有权限删除");

        removeById(id);
    }

    @Override
    public List<Blog> getBlogs(String userId, Integer pageNo, Integer pageSize) {
        UserInfoDTO userInfoDTO = userUtils.get();
        List<Blog> blogList = new ArrayList<>();
        if(userId == null){
            //查询所有
            pageNo = (pageNo - 1) * pageSize;
            blogList = blogMapper.selectAllBlogs(pageNo,pageSize);
            //查询当前用户是否点赞过
            isLike(blogList,userInfoDTO.getUserId());
            return blogList;
        }

        //查询博文以及发表博文的用户信息
        Page<Blog> page = lambdaQuery().eq(Blog::getUserId, userId).page(new Page<>(pageNo, pageSize));
        blogList = page.getRecords();
        //获取用户头像
        User user = userMapper.selectById(userId);
        for (Blog blog : blogList) {
            blog.setNickName(user.getNickName());
        }
        //查询当前用户是否点赞过
        isLike(blogList,userInfoDTO.getUserId());

        return null;
    }

    private void isLike(List<Blog> blogList, String userId) {
        for (Blog blog : blogList) {
            if(redisUtils.isLike(blog.getBlogId(),userId)){
                blog.setIsLike(true);
            }else{
                blog.setIsLike(false);
            }
        }
    }

    @Override
    public List<Blog> loadMyBlogs() {
        UserInfoDTO userInfoDTO = userUtils.get();
        String userId = userInfoDTO.getUserId();
        List<Blog> list = lambdaQuery().eq(Blog::getUserId, userId).list();
        isLike(list,userId);
        return list;
    }

    @Override
    public void likeBlog(String userId, Integer blogId) {
        Blog blog = getById(blogId);
        if(blog == null){
            throw new BusinessException(ExceptionEnum.OTHERS);
        }
        //判读用户是否点赞过
        boolean like = redisUtils.isLike(blogId, userId);
        if(like){
            //取消点赞
            lambdaUpdate().setSql("likes = likes -1").eq(Blog::getBlogId,blogId).update();
            redisUtils.removeBlogLike(blogId,userId);
        }else{
            //点赞
            lambdaUpdate().setSql("likes = likes + 1").eq(Blog::getBlogId,blogId).update();
            redisUtils.setBlogLike(blogId,userId);
        }
    }


}
