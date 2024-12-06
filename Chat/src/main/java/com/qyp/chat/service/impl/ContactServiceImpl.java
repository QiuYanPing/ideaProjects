package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.mapper.ContactMapper;
import com.qyp.chat.service.IContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 联系人 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class ContactServiceImpl extends ServiceImpl<ContactMapper, Contact> implements IContactService {

}
