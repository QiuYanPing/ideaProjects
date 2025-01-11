package com.qyp.chat.mapper;

import com.qyp.chat.domain.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 联系人 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
public interface ContactMapper extends BaseMapper<Contact> {

    List<Contact> selectGroupMember(String groupId);
}
