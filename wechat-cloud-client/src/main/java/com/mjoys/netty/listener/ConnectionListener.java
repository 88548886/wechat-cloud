package com.mjoys.netty.listener;

import com.mjoys.Client;
import com.mjoys.Server;
import com.mjoys.ServerNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionListener implements ChannelFutureListener {
    private Client client;
    private int maxRetryTimes;
    private AtomicInteger retryTimes;

    public ConnectionListener(Client client, int maxRetryTimes, AtomicInteger retryTimes) {
        this.client = client;
        this.maxRetryTimes = maxRetryTimes;
        this.retryTimes = retryTimes;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        System.out.println("operationComplete");
        if (!channelFuture.isSuccess()) {
            if (retryTimes.incrementAndGet() <= maxRetryTimes) {
                final EventLoop loop = channelFuture.channel().eventLoop();
                loop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        client.createBootstrap(new Bootstrap(), loop);
                    }
                }, 1, TimeUnit.SECONDS);
            }

        }
    }
}
