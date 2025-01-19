package com.qyp.chat.service.impl;

import com.qyp.chat.domain.entity.Message;
import com.qyp.chat.mapper.MessageMapper;
import com.qyp.chat.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息 服务实现类
 * </p>
 *
 * @author 
 * @since 2024-12-06
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
