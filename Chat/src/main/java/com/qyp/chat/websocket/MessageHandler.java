package com.qyp.chat.websocket;

import cn.hutool.json.JSONUtil;
import com.qyp.chat.domain.dto.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MessageHandler {
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ChannelContextUtils channelContextUtils;
    private static final String MESSAGE_TOPIC = "message.topic";

    @PostConstruct
    public void listenerMessage(){
        RTopic topic = redissonClient.getTopic(MESSAGE_TOPIC);
        topic.addListener(MessageDTO.class, (MessageDTO,messageDTO) ->{
                log.info("收到广播消息：{}", JSONUtil.toJsonStr(messageDTO));
                channelContextUtils.sendMessage(messageDTO);
            });
    }

    public void sendMessage(MessageDTO messageDTO){
        RTopic topic = redissonClient.getTopic(MESSAGE_TOPIC);
        topic.publish(messageDTO);
    }

}
