package com.qyp.chat;

import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.websocket.netty.NettyWebSocketStarter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
@Slf4j
public class InitRun implements ApplicationRunner {
    @Autowired
    private NettyWebSocketStarter nettyWebSocketStarter;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RedisUtils redisUtils;




    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{
            dataSource.getConnection();
            redisUtils.test();
            new Thread(nettyWebSocketStarter).start();
            log.info("服务启动成功。");
        }catch (SQLException e){
            log.error("数据库配置错误，请检查配置。");
        }catch (RedisConnectionFailureException e){
            log.error("redis配置错误，请检查redis配置。");
        }catch (Exception e){
            log.error("服务启动失败",e);
        }
    }
}
