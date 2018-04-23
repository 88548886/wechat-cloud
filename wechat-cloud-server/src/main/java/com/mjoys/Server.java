package com.mjoys;

import com.mjoys.netty.decoder.MjoysLengthFieldBasedFrameDecoder;
import com.mjoys.netty.encoder.MjoysOutboundEncoder;
import com.mjoys.netty.handler.MjoysServerInboundHandler;
import com.mjoys.service.IRedisService;
import com.mjoys.service.impl.RedisServiceImpl;
import com.mjoys.utils.SpringBeanUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Server {
    private IRedisService redisService = SpringBeanUtil.getBean(RedisServiceImpl.class);

    public void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ScheduledExecutorService es = Executors.newScheduledThreadPool(2);
        TaskManager taskManager = new TaskManager();
        MjoysServerInboundHandler serverInboundHandler = new MjoysServerInboundHandler(taskManager);
        try {
            ServerBootstrap sbs = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(SystemConstant.CHANNLE_IDLE_IIME, 0, 0,
                                    SystemConstant.CHANNLE_IDLE_TIME_UNIT));
                            ch.pipeline().addLast(new MjoysOutboundEncoder());
                            ch.pipeline().addLast(new MjoysLengthFieldBasedFrameDecoder(
                                    SystemConstant.MAX_FRAME_LENGTH,
                                    SystemConstant.LENGTH_FIELD_LENGTH,
                                    SystemConstant.LENGTH_FIELD_OFFSET,
                                    SystemConstant.LENGTH_ADJUSTMENT,
                                    SystemConstant.INITIAL_BYTES_TO_STRIP,
                                    false));
                            ch.pipeline().addLast(serverInboundHandler);
                        }

                    });
            ChannelFuture future = sbs.bind(port).sync();
            es.scheduleAtFixedRate(new Register(), 0, 1, TimeUnit.SECONDS);
            es.scheduleAtFixedRate(taskManager, 0, 30, TimeUnit.SECONDS);
            log.info("服务启动成功,监听端口" + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                es.shutdown();
                taskManager.stop();
                serverInboundHandler.stop();
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
