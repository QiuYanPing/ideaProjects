package com.qyp.chat.websocket;


import com.qyp.chat.domain.entity.Group;
import com.qyp.chat.util.RedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChannelContextUtils {
    @Autowired
    RedisUtils redisUtils;

    private static final ConcurrentHashMap<String,Channel> USER_CONTEXT_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ChannelGroup> GROUP_CONTEXT_MAP = new ConcurrentHashMap<>();
    public  void addContext(String userId, Channel channel){
        String channelId = channel.id().toString();
        AttributeKey attributeKey = null;
        if(!AttributeKey.exists(channelId)){
            attributeKey = AttributeKey.newInstance(channelId);
        }else{
            attributeKey = AttributeKey.valueOf(channelId);
        }
        channel.attr(attributeKey).set(userId);

        //创建心跳
        redisUtils.saveUserHeartBeat(userId);
        //设置userId和channel的关系
        USER_CONTEXT_MAP.put(userId,channel);
        //Todo 遍历用户的联系人，若为群组，则将用户channel加入组中
        String groupId = "";
        addUser2Group(userId,groupId);
    }
    private void addUser2Group(String userId,String groupId){
        Channel user = USER_CONTEXT_MAP.get(userId);
        ChannelGroup group = GROUP_CONTEXT_MAP.get(groupId);
        if(group == null){
            group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            GROUP_CONTEXT_MAP.put(groupId,group);
        }
        group.add(user);
    }
}
