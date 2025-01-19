package com.qyp.chat.websocket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
@Slf4j
@Component
public class NettyWebSocketStarter implements Runnable{
    @Autowired
    HandlerWebSocket handlerWebSocket;

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workGroup = new NioEventLoopGroup();

    @PreDestroy
    public void close(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    @Override
    public void run() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workGroup);
        try {
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG)).childHandler(new ChannelInitializer() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    //添加http编解码器
                    pipeline.addLast(new HttpServerCodec());
                    //添加聚合器，将HttpMessage聚合到FullRequest，保证接受的http请求的完整性
                    pipeline.addLast(new HttpObjectAggregator(64*1024));
                    //设置心跳，一定时间内检查不到心跳，就断开连接
                    //TODO 修改读超时时间，方便调试
                    pipeline.addLast(new IdleStateHandler(0,0,0, TimeUnit.SECONDS));
                    pipeline.addLast(new HandlerHeartBeat());

                    pipeline.addLast(new WebSocketServerProtocolHandler("/ws",null,true,64*1024,true,true,10000L));
                    pipeline.addLast(handlerWebSocket);
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(5051).sync();
            log.info("netty启动");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("netty启动失败",e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
