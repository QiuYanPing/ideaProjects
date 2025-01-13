package com.qyp.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.qyp.chat.domain.dto.ContactSearchDTO;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.entity.Group;
import com.qyp.chat.domain.entity.User;
import com.qyp.chat.domain.enums.ContactStatusEnum;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.mapper.ContactMapper;
import com.qyp.chat.mapper.GroupMapper;
import com.qyp.chat.mapper.UserMapper;
import com.qyp.chat.service.IContactService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qyp.chat.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    UserMapper userMapper;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    ContactMapper contactMapper;
    @Autowired
    UserUtils userUtils;

    @Override
    public ContactSearchDTO search(String userId, String contactId) {
        //查看联系人的类型
        ContactTypeEnum typeEnum = ContactTypeEnum.getByPrefix(contactId);
        if (typeEnum == null)
            return null;
        ContactSearchDTO contactSearchDTO = new ContactSearchDTO();
        switch (typeEnum) {
            case USER:
                //为用户
                User user = userMapper.selectById(contactId);
                if (user == null) {
                    return null;
                }
                BeanUtil.copyProperties(user, contactSearchDTO);
                break;
            case GROUP:
                //为群聊
                Group group = groupMapper.selectById(contactId);
                if (group == null) {
                    return null;
                }
                contactSearchDTO.setNickName(group.getGroupName());
                break;
        }
        //填充信息
        contactSearchDTO.setContactId(contactId);
        contactSearchDTO.setContactType(typeEnum.toString());
        //查看联系的状态
        if (contactId.equals(userId)) {
            //查询的联系人为自己，关系则为好友
            contactSearchDTO.setStatus(ContactStatusEnum.FRIEND.getStatus());
            return contactSearchDTO;
        }
        Contact contact = lambdaQuery().eq(Contact::getUserId, userId)
                .eq(Contact::getContactId, contactId)
                .one();
        contactSearchDTO.setStatus(contact == null ? null : contact.getStatus());
        return contactSearchDTO;
    }

    @Override
    public List<Contact> loadContact(ContactTypeEnum type) {
        UserInfoDTO userInfoDTO = userUtils.get();
        String userId = userInfoDTO.getUserId();
        List<Contact> contactList = new ArrayList<>();
        Integer[] array = new Integer[]{ContactStatusEnum.FRIEND.getStatus(),
                ContactStatusEnum.DEL_BE.getStatus(),
                ContactStatusEnum.BLACKlIST_BE.getStatus()};
        if (ContactTypeEnum.USER == type) {
            //可显示的好友,根据更新时间降序
            contactList = contactMapper.loadUserContact(userId,array);
        } else {
            //可显示的群聊,根据更新时间降序
            contactList = contactMapper.loadGroupContact(userId,array);
        }
        return contactList;
    }
}
