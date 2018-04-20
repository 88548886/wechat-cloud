package com.mjoys;

import com.mjoys.netty.decoder.CustomLengthFieldBasedFrameDecoder;
import com.mjoys.netty.encoder.CustomOutboundEncoder;
import com.mjoys.netty.handler.CustomClientInboundHandlerAdapter;
import com.mjoys.protocol.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import com.mjoys.netty.listener.ConnectionListener;

import java.util.concurrent.atomic.AtomicInteger;


public class Client {
    private String host;
    private int port;
    private Channel channel;
    private final AtomicInteger retryTimes = new AtomicInteger();
    private int maxRetryTimes;

    public Client start(String host, int port,int maxRetryTimes) {
        this.host = host;
        this.port = port;
        this.maxRetryTimes = maxRetryTimes;
        this.createBootstrap(new Bootstrap(), new NioEventLoopGroup());
        return this;
    }

    public Client setHost(String host) {
        this.host = host;
        return this;
    }

    public Client setPort(int port) {
        this.port = port;
        return this;
    }

    public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {
        if (bootstrap != null) {
            CustomClientInboundHandlerAdapter handler = new CustomClientInboundHandlerAdapter(this);
            bootstrap.group(eventLoop);

            bootstrap.channel(NioSocketChannel.class);

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {

                    socketChannel.pipeline().addLast(new IdleStateHandler(0, Constant.CHANNLE_IDLE_IIME, 0, Constant
                            .CHANNLE_IDLE_TIME_UNIT));
                    socketChannel.pipeline().addLast(new CustomOutboundEncoder());
                    socketChannel.pipeline().addLast(new CustomLengthFieldBasedFrameDecoder(
                            Constant.MAX_FRAME_LENGTH,
                            Constant.LENGTH_FIELD_LENGTH,
                            Constant.LENGTH_FIELD_OFFSET,
                            Constant.LENGTH_ADJUSTMENT,
                            Constant.INITIAL_BYTES_TO_STRIP,
                            false));
                    socketChannel.pipeline().addLast(handler);
                }
            });
            bootstrap.remoteAddress(host, port);
            ChannelFuture channelFuture = bootstrap.connect();
            channelFuture.addListener(new ConnectionListener(this, maxRetryTimes,retryTimes));
            this.channel = channelFuture.channel();
        }
        return bootstrap;
    }

    public Client send(Message msg) {
        channel.writeAndFlush(msg);
        return this;
    }
}
