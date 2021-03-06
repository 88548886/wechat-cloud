package com.mjoys.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mjoys.MessageWarp;
import com.mjoys.SystemConstant;
import com.mjoys.TaskManager;
import com.mjoys.Terminal;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.report.SendSmsMsgReport;
import com.mjoys.protocol.message.report.WechatAddFriendReport;
import com.mjoys.protocol.message.system.CommandExecutedAck;
import com.mjoys.protocol.message.system.CommandReceivedAck;
import com.mjoys.protocol.message.system.Heartbeat;
import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
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
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class MjoysServerInboundHandler extends SimpleChannelInboundHandler<Message> {

    private IRedisService redisService = SpringBeanUtil.getBean(RedisServiceImpl.class);
    private ITaskService taskService = SpringBeanUtil.getBean(TaskServiceImpl.class);
    private TaskManager taskMananger = new TaskManager();
    public static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public MjoysServerInboundHandler(TaskManager taskMananger) {
        this.taskMananger = taskMananger;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        log.debug("[ Server ] received : " + remoteAddress.getHostName() + "\t" + remoteAddress.getPort() +
                " " + "( " + MessageFlag.build(msg.getFlag()) + "\t" + MessageType.build(msg.getType()) + "\t" + msg
                .getBody() + " )");
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
        //每次有读事件就检查任务中有没有待发的消息
        MessageWarp messageWarpWaitingProcess = taskMananger.getTask();
        if (null != messageWarpWaitingProcess) {
            log.info("执行消息任务:\n{}",JSON.toJSONString(messageWarpWaitingProcess, SerializerFeature.PrettyFormat));
            Terminal.Addr terminalAddr = messageWarpWaitingProcess.getTerminalAddr();
            channelGroup.writeAndFlush(messageWarpWaitingProcess.getMsg(),
                    new MjoysChannelMatcher(String.format("%s:%d",
                            terminalAddr.getIp(),
                            terminalAddr.getPort())));
        }

    }

    private void processBusRep(ChannelHandlerContext ctx, Message msg) {
        switch (MessageType.build(msg.getType())) {
            case REP_ADD_WECHAT_FRIEND:
                WechatAddFriendReport wechatAddFriendReport = JSON.parseObject(msg.getBody(), WechatAddFriendReport
                        .class);
                taskService.updateTaskResult(wechatAddFriendReport.getCommandId(), wechatAddFriendReport.getResult());
                break;
            case REP_SEND_MSG:
                SendSmsMsgReport sendSmsMsgReport = JSON.parseObject(msg.getBody(), SendSmsMsgReport.class);
                taskService.updateTaskResult(sendSmsMsgReport.getCommandId(), sendSmsMsgReport.getResult());
                break;
            default:
                break;
        }
    }

    private void processSysMsg(ChannelHandlerContext ctx, Message msg) {
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        switch (MessageType.build(msg.getType())) {
            case SYS_HEARTBEAT:
                Heartbeat heartbeat = JSON.parseObject(msg.getBody(), Heartbeat.class);

                //TODO 更新client的地址
                Map<String, String> keyValuePairs = new HashMap<>();
                keyValuePairs.put(String.format("addr-%s", heartbeat.getUid()),
                        String.format("%s:%s:%d:%d", heartbeat.getUid(), remoteAddress.getHostName(),
                                remoteAddress.getPort(), System.currentTimeMillis()));
                //TODO 更新client最后的心跳
                keyValuePairs.put(String.format("heartbeat-%s-%d", remoteAddress.getHostName(), remoteAddress.getPort
                                ()),
                        String.valueOf(System.currentTimeMillis()));
                //TODO 更新client在哪个server
                keyValuePairs.put(String.format("server-%s", heartbeat.getUid()), String.format("%s:%d",
                        SystemConstant.ip, SystemConstant.port));

                redisService.hsetAll(String.format("wechat-cloud:client"), keyValuePairs);
                break;
            case SYS_COMMAND_RECEIVED_ACK:
                CommandReceivedAck commandReceivedAck = JSON.parseObject(msg.getBody(), CommandReceivedAck.class);
                taskService.markTaskAsSubmitted(commandReceivedAck.getCommandId());
                break;
            case SYS_COMMAND_EXECUTED_ACK:
                CommandExecutedAck commandExecutedAck = JSON.parseObject(msg.getBody(), CommandExecutedAck.class);
                taskService.markTaskAsExecuted(commandExecutedAck);
                break;
            default:
                break;
        }
    }


    class MjoysChannelMatcher implements ChannelMatcher {
        private String addr;

        public MjoysChannelMatcher(String addr) {
            this.addr = addr;
        }

        @Override
        public boolean matches(Channel channel) {
            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            if (addr.equals(String.format("%s:%d", remoteAddress.getHostName(), remoteAddress.getPort()))) {
                log.debug("forward command to " + String.format("%s:%d", remoteAddress.getHostName(), remoteAddress
                        .getPort()) + " successed.");
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
                InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                String leastHeartBeartTimestamp = redisService.hget("wechat-cloud:client",
                        String.format("heartbeat-%s-%d", remoteAddress.getHostName(), remoteAddress.getPort()));
                if ((System.currentTimeMillis() - Long.valueOf(leastHeartBeartTimestamp))
                        > (SystemConstant.CHANNLE_IDLE_TIME_UNIT.toMillis(SystemConstant.CHANNLE_IDLE_IIME) * 5)) {
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
        InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        redisService.hset(String.format("wechat-cloud:client"), String.format("heartbeat-%s-%d", remoteAddress
                .getHostName(), remoteAddress.getPort
                ()), String.valueOf(System.currentTimeMillis()));

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
        ctx.close().sync();
        cause.printStackTrace();
    }

}