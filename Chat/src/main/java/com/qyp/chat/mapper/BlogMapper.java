package com.qyp.chat.mapper;

import com.qyp.chat.domain.entity.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 博文表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
public interface BlogMapper extends BaseMapper<Blog> {

    List<Blog> selectAllBlogs(Integer pageNo, Integer pageSize);
}
