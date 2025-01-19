package com.qyp.chat.websocket;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qyp.chat.constant.SysConstant;
import com.qyp.chat.domain.dto.MessageDTO;
import com.qyp.chat.domain.dto.WSInitDTO;
import com.qyp.chat.domain.entity.*;
import com.qyp.chat.domain.enums.ApplyStatusEnum;
import com.qyp.chat.domain.enums.ContactTypeEnum;
import com.qyp.chat.mapper.ApplyMapper;
import com.qyp.chat.mapper.MessageMapper;
import com.qyp.chat.mapper.UserMapper;
import com.qyp.chat.mapper.UserSessionMapper;
import com.qyp.chat.util.RedisUtils;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ChannelContextUtils {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserSessionMapper userSessionMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    ApplyMapper applyMapper;

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

        //设置userId和channel的关系
        USER_CONTEXT_MAP.put(userId,channel);
        //遍历用户的联系人，若为群组，则将用户channel加入组中
        List<String> contactList = redisUtils.getContactList(userId);
        List<String> groupContact = contactList.stream().filter(item -> ContactTypeEnum.GROUP.getPrefix().equals(item.substring(0, 1))).collect(Collectors.toList());
        for (String groupId : groupContact) {
            addUser2Group(userId,groupId);
        }

        //创建心跳
        redisUtils.saveUserHeartBeat(userId);
        //更新用户登录时间
        User user = new User();
        user.setUserId(userId);
        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        //发送信息
        User me = userMapper.selectById(userId);
        Long lastOffTime = me.getLastOffTime();
        Long time = System.currentTimeMillis() - SysConstant.MESSAGE_STORE_TIME;
        if(lastOffTime!= null &&  time > lastOffTime)
            lastOffTime = time;
        //查询会话信息
        List<UserSession> userSessionList = userSessionMapper.selectAllUserSession(userId);
        //查询聊天信息,缓存到本地，之后不再请求
        groupContact.add(userId);
        LambdaQueryChainWrapper<Message> wrapper = new LambdaQueryChainWrapper<>(messageMapper);
        List<Message> messageList = wrapper.in(Message::getContactId, groupContact)
                .gt(Message::getSendTime, lastOffTime).list();
        //查询申请条数
        LambdaQueryChainWrapper<Apply> applyLambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(applyMapper);
        Integer count = applyLambdaQueryChainWrapper.eq(Apply::getReceiveUserId, userId)
                .eq(Apply::getStatus, ApplyStatusEnum.INIT.getStatus())
                .gt(Apply::getLastApplyTime, lastOffTime).count();

        WSInitDTO wsInitDTO = new WSInitDTO();
        wsInitDTO.setUserSessionList(userSessionList);
        wsInitDTO.setMessageList(messageList);
        wsInitDTO.setApplyCount(count);


        //发送离线后的信息
        MessageDTO<WSInitDTO> messageDTO = new MessageDTO<>();
        messageDTO.setContactId(userId);
        messageDTO.setContactName(me.getNickName());
        messageDTO.setExtendData(wsInitDTO);
        sendMsg(messageDTO,userId);

    }

    private void sendMsg(MessageDTO messageDTO, String userId) {
        if(userId == null)
            return;
        Channel channel = USER_CONTEXT_MAP.get(userId);
        //联系人装换
        messageDTO.setContactId(messageDTO.getSendUserId());
        messageDTO.setContactName(messageDTO.getSendUserNickName());
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(messageDTO)));
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

    public void removeContext(Channel channel) {
        String userId = (String) channel.attr(AttributeKey.valueOf(channel.id().toString())).get();
        //移除用户通道
        USER_CONTEXT_MAP.remove(userId);
        //删除心跳
        redisUtils.removeUserHeartBeat(userId);
        //更新离线时间
        User user = new User();
        user.setUserId(userId);
        user.setLastOffTime(System.currentTimeMillis());
        userMapper.updateById(user);
    }
}
