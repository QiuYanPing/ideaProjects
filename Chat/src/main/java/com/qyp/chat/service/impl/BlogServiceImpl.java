package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.Blog;
import com.qyp.chat.mapper.BlogMapper;
import com.qyp.chat.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
