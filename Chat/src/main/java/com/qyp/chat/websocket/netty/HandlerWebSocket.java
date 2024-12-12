package com.qyp.chat.websocket.netty;

import cn.hutool.core.util.StrUtil;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HandlerWebSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    RedisUtils redisUtils;
    @Autowired
    ChannelContextUtils channelContextUtils;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("建立连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("断开连接");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        String userId = (String) channel.attr(AttributeKey.valueOf(channel.id().toString())).get();
        log.info("收到信息{}：{}",userId,msg.text());
        //更新心跳
        redisUtils.saveUserHeartBeat(userId);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            WebSocketServerProtocolHandler.HandshakeComplete complete= (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String uri = complete.requestUri();
            String token = getToken(uri);
            if(StrUtil.isEmpty(token)){
                ctx.channel().close();
                return;
            }
            UserInfoDTO userInfoDTO = redisUtils.getUserInfoDTO(token, UserInfoDTO.class);
            if(userInfoDTO == null){
                //未登录
                ctx.channel().close();
                return;
            }
            log.info("token:{}",token);
            String userId = userInfoDTO.getUserId();
            channelContextUtils.addContext(userId,ctx.channel());
        }
    }

    private String getToken(String uri) {
        // /ws?token=xxx
        if(uri.isEmpty() || !uri.contains("?")){
            return null;
        }
        String[] queryParams = uri.split("\\?");
        String[] params = queryParams[1].split("=");
        return params[1];
    }
}
