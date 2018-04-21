package com.mjoys.netty.handler;

import com.alibaba.fastjson.JSON;
import com.mjoys.SystemConstant;

import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.command.SendSmsMsgCommand;
import com.mjoys.protocol.message.command.WechatAddFriendCommand;
import com.mjoys.protocol.message.report.SendSmsMsgReport;
import com.mjoys.protocol.message.report.WechatAddFriendReport;
import com.mjoys.protocol.message.system.CommandExecutedAck;
import com.mjoys.protocol.message.system.CommandReceivedAck;
import com.mjoys.protocol.message.system.Heartbeat;
import com.mjoys.protocol.message.system.Task;
import com.mjoys.service.IAccountService;
import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
import com.mjoys.service.impl.AccountServiceImpl;
import com.mjoys.service.impl.RedisServiceImpl;
import com.mjoys.service.impl.TaskServiceImpl;
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

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;


@Slf4j
public class MjoysServerInboundHandler extends SimpleChannelInboundHandler<Message> {

    private IRedisService redisService = SpringBeanUtil.getBean(RedisServiceImpl.class);
    private ITaskService taskService = SpringBeanUtil.getBean(TaskServiceImpl.class);

    public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {



        switch (MessageFlag.build(msg.getFlag())) {
            case MESSAGE_FLAG_SYS:
                processSysMsg(ctx, msg);
                break;
            case MESSAGE_FLAG_REP:
                processBusRep(ctx, msg);
                break;
            case MESSAGE_FLAG_COM:
                break;
            default:
                break;
        }
    }

    private void processBusRep(ChannelHandlerContext ctx, Message msg) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        switch (MessageType.build(msg.getType())) {
            case REP_ADD_WECHAT_FRIEND:
                log.info("[ Server ] received : " + remoteAddress.getHostName() + remoteAddress.getPort() + " -> (" +
                        MessageFlag.build(msg.getFlag()) + "\t" + MessageType.build(msg.getType()) + "\t" + msg
                        .getBody());
                WechatAddFriendReport wechatAddFriendReport = JSON.parseObject(msg.getBody(), WechatAddFriendReport
                        .class);
                taskService.updateTaskResult(wechatAddFriendReport.getCommandId(), wechatAddFriendReport.getResult());
                break;
            case REP_SEND_MSG:
                log.info("[ Server ] received : " + remoteAddress.getHostName() + remoteAddress.getPort() + " -> (" +
                        MessageFlag.build(msg.getFlag()) + "\t" + MessageType.build(msg.getType()) + "\t" + msg
                        .getBody());
                SendSmsMsgReport sendSmsMsgReport = JSON.parseObject(msg.getBody(), SendSmsMsgReport.class);
                taskService.updateTaskResult(sendSmsMsgReport.getCommandId(), sendSmsMsgReport.getResult());
                break;
            default:
                break;
        }
    }

    private void processSysMsg(ChannelHandlerContext ctx, Message msg) {
        //TODO 是心跳包,在缓存中刷新心跳时间
        switch (MessageType.build(msg.getType())) {
            case SYS_HEARTBEAT:
                Heartbeat heartbeat = JSON.parseObject(msg.getBody(), Heartbeat.class);
                InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                redisService.hset(String.format("wechat-cloud:client:addr"), heartbeat.getUid(),
                        String.format("%s:%s:%d", heartbeat.getUid(), remoteAddress.getHostName(), remoteAddress.getPort()), 1,
                        TimeUnit.DAYS);
                redisService.hset(String.format("wechat-cloud:client:server"), heartbeat.getUid(),
                        String.format("%s:%d", SystemConstant.ip, SystemConstant.port), 1,
                        TimeUnit.DAYS);
                System.out.println();
                redisService.set(String.format("heartbeat%s%d",
                        remoteAddress.getHostName(), remoteAddress.getPort()), String.valueOf(System
                        .currentTimeMillis()), 30, TimeUnit.MILLISECONDS);
                break;
            case SYS_TASK:
                processTask(msg);
                break;
            case SYS_COMMAND_RECEIVED_ACK:
                CommandReceivedAck commandReceivedAck = JSON.parseObject(msg.getBody(), CommandReceivedAck.class);
                taskService.markTaskAsSubmitted(commandReceivedAck.getCommandId());
                break;
            case SYS_COMMAND_EXECUTED_ACK:
                CommandExecutedAck commandExecutedAck = JSON.parseObject(msg.getBody(), CommandExecutedAck.class);
                taskService.markTaskAsExecuted(commandExecutedAck.getCommandId());
                break;
            default:
                break;
        }
    }

    private void processTask(Message msg) {
        log.info("dist task :" + msg.getBody());
        Task task = JSON.parseObject(msg.getBody(), Task.class);
        //TODO 处理任务的分发
        //TODO 读在线用户的缓存,随机选择C端(wx/mobile)
        Message command=null;
        switch (MessageType.build(task.getCommandType())) {
            case COM_ADD_WECHAT_FRIEND:
                command = new Message(MessageFlag.MESSAGE_FLAG_COM.getCode(),
                        MessageType.COM_ADD_WECHAT_FRIEND.getCode(),
                        JSON.toJSONString(new WechatAddFriendCommand(task.getId(),
                                task.getExecuteTime(),
                                task.getReceiver(),
                                task.getMessage())));
                break;
            case COM_SEND_MSG:
                command = new Message(MessageFlag.MESSAGE_FLAG_COM.getCode(),
                        MessageType.COM_SEND_MSG.getCode(),
                        JSON.toJSONString(new SendSmsMsgCommand(task.getId(),
                                task.getExecuteTime(),
                                task.getReceiver(),
                                task.getMessage())));
                break;
            default:
                break;
        }
        System.out.println("转发指令" + command.getFlag() + "\t" + command.getType() + "\t" + command.getBody());
        channelGroup.writeAndFlush(command, new MjoysChannelMatcher(task.getTerminalAddr()));
    }

    class MjoysChannelMatcher implements ChannelMatcher {
        private String addr;

        public MjoysChannelMatcher(String addr) {
            this.addr = addr;
        }

        @Override
        public boolean matches(Channel channel) {
            log.info("dist task match:");
            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            if (addr.equals(String.format("%s:%d", remoteAddress.getHostName(), remoteAddress.getPort()))) {
                log.info("dist task match true");
                return true;
            }
            return false;
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                log.debug(SystemConstant.CHANNLE_IDLE_IIME + "secend not receive message from ");
                InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                String leastHeartBeartTimestamp = redisService.get(String.format("heartbeat%s%d",
                        remoteAddress.getHostName(), remoteAddress.getPort()));
                if ((System.currentTimeMillis() - Long.valueOf(leastHeartBeartTimestamp))
                        > (SystemConstant.CHANNLE_IDLE_TIME_UNIT.toMillis(SystemConstant.CHANNLE_IDLE_IIME) * 5)) {
                    log.debug("close " + remoteAddress.getHostName() + ":" + remoteAddress.getPort());
                    ctx.channel().close();
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.println("handlerAdded ->" + remoteAddress.getHostName() + "\t" + remoteAddress.getPort());
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
        ctx.close();
    }
}