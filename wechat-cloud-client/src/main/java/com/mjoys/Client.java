package com.mjoys;

import com.mjoys.decoder.CustomLengthFieldBasedFrameDecoder;
import com.mjoys.encoder.CustomOutboundEncoder;
import com.mjoys.handler.CustomClientInboundHandlerAdapter;
import com.mjoys.listener.ConnectionListener;
import com.mjoys.protocol.Constant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class Client {
    private String host;
    private int port;

    public void start(String host, int port) {
        this.host = host;
        this.port = port;
        createBootstrap(new Bootstrap(), new NioEventLoopGroup());
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

                    socketChannel.pipeline().addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
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
            bootstrap.connect().addListener(new ConnectionListener(this));
        }
        return bootstrap;
    }
}
