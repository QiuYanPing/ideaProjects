package com.qyp.chat.mapper;

import com.qyp.chat.domain.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 群聊 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface GroupMapper extends BaseMapper<Group> {

    List<Group> loadGroup();
}
