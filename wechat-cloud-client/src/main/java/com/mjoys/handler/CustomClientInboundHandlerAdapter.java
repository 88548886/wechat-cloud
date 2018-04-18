package com.mjoys.handler;

import com.mjoys.Client;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomClientInboundHandlerAdapter extends SimpleChannelInboundHandler<Message> {

    private Client client;

    public CustomClientInboundHandlerAdapter(Client client) {
        this.client = client;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("激活时间是：" + new Date());
        Message customMsg = new Message(MessageFlag.MESSAGE_FLAG_BUS,
                MessageType.MESSAGE_TYPE_BUS_LOGIN_REQUEST.getCode(),
                "{wx:11111}".length(), "{wx:11111}");
        ctx.writeAndFlush(customMsg);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("循环触发时间：" + new Date());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                Message customMsg = new Message(MessageFlag.MESSAGE_FLAG_BUS,
                        MessageType.MESSAGE_TYPE_SYS_HEARTBEAT.getCode(),
                        "Heartbeat".length(), "Heartbeat");
                ctx.channel().writeAndFlush(customMsg);

            }
        }
    }


    @Override

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                client.createBootstrap(new Bootstrap(), eventLoop);
            }

        }, 5L, TimeUnit.SECONDS);

        super.channelInactive(ctx);

    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        System.out.println("[ Client ] received : " + ctx.channel().remoteAddress() + " send -> " + message.getBody());
    }
}