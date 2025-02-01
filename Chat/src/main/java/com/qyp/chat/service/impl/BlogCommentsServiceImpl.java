package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.BlogComments;
import com.qyp.chat.mapper.BlogCommentsMapper;
import com.qyp.chat.service.IBlogCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
