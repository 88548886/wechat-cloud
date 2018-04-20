package com.mjoys.netty.handler;

import com.alibaba.fastjson.JSON;
import com.mjoys.Constant;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.business.TaskRequest;
import com.mjoys.protocol.message.business.WehcatAddFriendRequest;
import com.mjoys.service.IAccountService;
import com.mjoys.service.IRedisService;
import com.mjoys.service.impl.AccountServiceImpl;
import com.mjoys.service.impl.RedisServiceImpl;
import com.mjoys.utils.SpringBeanUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class MjoysServerInboundHandler extends SimpleChannelInboundHandler<Message> {

    private IRedisService redisService = SpringBeanUtil.getBean(RedisServiceImpl.class);
    private IAccountService accountService = SpringBeanUtil.getBean(AccountServiceImpl.class);

    public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final String HEARTBEAT_CACHE_FORMATTER = "heartbeat:%s";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        log.info("[ Server ] " + ctx.channel().localAddress() + " received : " + ctx.channel()
                .remoteAddress() + " -> " + MessageFlag.build(msg.getFlag()) + MessageType.build(msg.getType()) + msg
                .getBody());
        switch (MessageFlag.build(msg.getFlag())) {
            case MESSAGE_FLAG_SYS:
                processSysMsg(ctx, msg);
                break;
            case MESSAGE_FLAG_REP:
                break;
            case MESSAGE_FLAG_COM:
                break;
            default:
                break;
        }
    }

    private void processSysMsg(ChannelHandlerContext ctx, Message msg) {
        //TODO 是心跳包,在缓存中刷新心跳时间
        switch (MessageType.build(msg.getType())) {
            case SYS_HEARTBEAT:
                redisService.set(String.format(HEARTBEAT_CACHE_FORMATTER, ctx.channel().hashCode()),
                        String.valueOf(System.currentTimeMillis()), 30, TimeUnit.MINUTES);
                channelGroup.writeAndFlush(new Message(MessageFlag.MESSAGE_FLAG_COM.getCode(),
                        MessageType.COM_ADD_WECHAT_FRIEND.getCode(),
                        JSON.toJSONString(new WehcatAddFriendRequest(1,
                                "wxfriend",
                                "我是上海证券通的小刘,可以加好友吗?"))));
                break;
            case SYS_TASK:
                TaskRequest taskRequest = JSON.parseObject(msg.getBody(), TaskRequest.class);
                //TODO 处理任务的分发
                //TODO 读在线用户的缓存,随机选择C端(wx/mobile)
                channelGroup.writeAndFlush(new Message(MessageFlag.MESSAGE_FLAG_COM.getCode(),
                                MessageType.COM_ADD_WECHAT_FRIEND.getCode(),
                                JSON.toJSONString(new WehcatAddFriendRequest(taskRequest.getId(),
                                        taskRequest.getTarget(),
                                        taskRequest.getMessage()))),
                        new MjoysChannelMatcher(taskRequest.getChannelHashCode()));
                break;
            default:
                break;
        }
    }

    class MjoysChannelMatcher implements ChannelMatcher {
        private int hashCode;

        public MjoysChannelMatcher(int hashCode) {
            this.hashCode = hashCode;
        }

        @Override
        public boolean matches(Channel channel) {
            return channel.hashCode() == this.hashCode;
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.info(Constant.CHANNLE_IDLE_IIME + "秒没有接收到客户端的信息了");
                String leastHeartBeartTimestamp = redisService.get(String.format(HEARTBEAT_CACHE_FORMATTER, ctx
                        .channel().hashCode()));
                //TODO 连续闲置CHANNLE_IDLE_IIME * 5则断开连接
                if ((System.currentTimeMillis() - Long.valueOf(leastHeartBeartTimestamp))
                        > (Constant.CHANNLE_IDLE_TIME_UNIT.toMillis(Constant.CHANNLE_IDLE_IIME) * 5)) {
                    log.info("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.remove(ctx.channel());
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        log.error("exceptionCaught");
        ctx.close();
    }
}