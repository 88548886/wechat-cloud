package com.mjoys.netty.handler;

import com.alibaba.fastjson.JSON;
import com.mjoys.Client;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.system.Heartbeat;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.TimeUnit;

public class CustomClientInboundHandlerAdapter extends SimpleChannelInboundHandler<Message> {

    private Client client;

    public CustomClientInboundHandlerAdapter(Client client) {
        this.client = client;

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("userEventTriggered");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                Message heartbeatMsg = new Message(MessageFlag.MESSAGE_FLAG_SYS.getCode(),
                        MessageType.SYS_HEARTBEAT.getCode(),
                        JSON.toJSONString(new Heartbeat(client.getTerminalUid(), client.getProtrait())));
                ctx.channel().writeAndFlush(heartbeatMsg);
            }
        }
    }


    @Override

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
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
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        System.out.println("[ Client ] received : " + ctx.channel().remoteAddress() + " send -> " + message.getBody());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        ctx.close();
        cause.printStackTrace();
    }
}