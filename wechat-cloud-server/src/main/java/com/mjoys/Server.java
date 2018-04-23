package com.mjoys;

import com.mjoys.netty.decoder.MjoysLengthFieldBasedFrameDecoder;
import com.mjoys.netty.encoder.MjoysOutboundEncoder;
import com.mjoys.netty.handler.MjoysServerInboundHandler;
import com.mjoys.service.IRedisService;
import com.mjoys.service.impl.RedisServiceImpl;
import com.mjoys.utils.SpringBeanUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Server {
    private IRedisService redisService = SpringBeanUtil.getBean(RedisServiceImpl.class);
    public void start(int port) throws InterruptedException {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(2);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        TaskManager taskMananger = new TaskManager();
        try {
            ServerBootstrap sbs = new ServerBootstrap();
            sbs.option(ChannelOption.SO_BACKLOG, 1024)
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(SystemConstant.CHANNLE_IDLE_IIME,
                                    0, 0,
                                    SystemConstant.CHANNLE_IDLE_TIME_UNIT));
                            pipeline.addLast(new MjoysLengthFieldBasedFrameDecoder(
                                    SystemConstant.MAX_FRAME_LENGTH,
                                    SystemConstant.LENGTH_FIELD_LENGTH,
                                    SystemConstant.LENGTH_FIELD_OFFSET,
                                    SystemConstant.LENGTH_ADJUSTMENT,
                                    SystemConstant.INITIAL_BYTES_TO_STRIP,
                                    false));
                            pipeline.addLast(new MjoysOutboundEncoder());
                            pipeline.addLast(new MjoysServerInboundHandler(taskMananger));
                        }
                    });
            Channel channel = sbs.bind(port).sync().channel();
            es.scheduleAtFixedRate(new Register(), 0, 1, TimeUnit.SECONDS);
            es.scheduleAtFixedRate(taskMananger, 0, 30, TimeUnit.SECONDS);
            taskMananger.start();
            log.info("服务启动成功,监听端口" + port);
            channel.closeFuture().sync();
        } catch (Exception e) {

        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                taskMananger.interrupt();
                es.shutdown();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        });
    }

    class Register implements Runnable {
        @Override
        public void run() {
            redisService.hset("wechat-cloud:servers",
                    String.format("%s:%d", SystemConstant.ip, SystemConstant.port),
                    String.valueOf(System.currentTimeMillis()),
                    1,
                    TimeUnit.DAYS);
        }
    }
}
