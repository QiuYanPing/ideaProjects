package com.qyp.chat.mapper;

import com.qyp.chat.domain.entity.Apply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 申请 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface ApplyMapper extends BaseMapper<Apply> {

    List<Apply> loadApply(String userId, Integer pageNo, Integer pageSize);
}
