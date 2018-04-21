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

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class Client {
    private Channel channel;
    private String terminalUid;
    private String protrait;
    private final AtomicInteger retryTimes = new AtomicInteger();
    private int maxRetryTimes;
    private EventLoopGroup eventLoop;
    private String serverIp;
    private int serverPort;

    public Client start(String serverIp, int serverPort, String terminalUid, String protrait, int maxRetryTimes) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.terminalUid = terminalUid;
        this.protrait = protrait;
        this.maxRetryTimes = maxRetryTimes;
        this.createBootstrap(new Bootstrap(), new NioEventLoopGroup());
        return this;
    }

    public String getTerminalUid() {
        return terminalUid;
    }

    public String getProtrait() {
        return protrait;
    }


    public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {
        if (bootstrap != null) {
            try {
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
                bootstrap.remoteAddress(serverIp, serverPort);
                ChannelFuture channelFuture = bootstrap.connect();
                channelFuture.addListener(new ConnectionListener(this, maxRetryTimes, retryTimes));
                this.channel = channelFuture.channel();
                this.eventLoop = eventLoop;
            } catch (Exception e) {

            }
        }
        return bootstrap;
    }

    public void stop() {
        if (null != eventLoop) {
            eventLoop.shutdownGracefully();
        }
    }

    public Client send(Message msg) {
        channel.writeAndFlush(msg);
        return this;
    }
}
