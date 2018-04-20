package com.mjoys;

import com.mjoys.netty.decoder.MjoysLengthFieldBasedFrameDecoder;
import com.mjoys.netty.encoder.MjoysOutboundEncoder;
import com.mjoys.netty.handler.MjoysServerInboundHandler;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class Server {

    public void start(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
                    .childOption(ChannelOption.SO_RCVBUF, 4096)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new IdleStateHandler(Constant.CHANNLE_IDLE_IIME, 0, 0,
                                    Constant.CHANNLE_IDLE_TIME_UNIT));
                            ch.pipeline().addLast(new MjoysOutboundEncoder());
                            ch.pipeline().addLast(new MjoysLengthFieldBasedFrameDecoder(
                                    Constant.MAX_FRAME_LENGTH,
                                    Constant.LENGTH_FIELD_LENGTH,
                                    Constant.LENGTH_FIELD_OFFSET,
                                    Constant.LENGTH_ADJUSTMENT,
                                    Constant.INITIAL_BYTES_TO_STRIP,
                                    false));
                            ch.pipeline().addLast(new MjoysServerInboundHandler());
                        }

                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = sbs.bind(port).sync();
            log.info("Server start listen at " + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
