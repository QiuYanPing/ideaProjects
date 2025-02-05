package com.qyp.chat.mapper;

import com.qyp.chat.domain.entity.BlogComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 博文评论表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2025-02-01
 */
public interface BlogCommentsMapper extends BaseMapper<BlogComments> {

    List<BlogComments> selectAllComments(Integer blogId);
}
