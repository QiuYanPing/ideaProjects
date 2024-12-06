package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.Group;
import com.qyp.chat.mapper.GroupMapper;
import com.qyp.chat.service.IGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 群聊 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

}
