package com.qyp.chat.domain.vo;

import com.qyp.chat.domain.entity.Contact;
import com.qyp.chat.domain.entity.Group;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Data
public class GroupVO {
    private Group group;
    private List<Contact> contactList;
}
