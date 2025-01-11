package com.qyp.chat.websocket.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HandlerHeartBeat extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent e = (IdleStateEvent) evt;
            if(e.state() == IdleState.READER_IDLE){
                log.info("读心跳超时");
                ctx.close();
            }else  if(e.state() == IdleState.WRITER_IDLE){
                //TODO 正确吗
                log.info("写心跳超时");
                ctx.writeAndFlush("heart");
            }
        }
    }
}
