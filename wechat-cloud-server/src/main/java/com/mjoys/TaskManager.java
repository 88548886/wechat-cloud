package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.po.Account;
import com.mjoys.po.Task;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.protocol.message.command.WechatAddFriendCommand;
import com.mjoys.service.IAccountService;
import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
import com.mjoys.service.impl.AccountServiceImpl;
import com.mjoys.service.impl.RedisServiceImpl;
import com.mjoys.service.impl.TaskServiceImpl;
import com.mjoys.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
public class TaskManager implements Runnable {
    private IRedisService redisService = SpringBeanUtil.getBean(RedisServiceImpl.class);
    private ITaskService taskService = SpringBeanUtil.getBean(TaskServiceImpl.class);
    private IAccountService accountService = SpringBeanUtil.getBean(AccountServiceImpl.class);
    private ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private final ConcurrentLinkedQueue<MessageWarp> messageTaskQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        log.info("读取任务" + new Date());
        List<Task> tasks = taskService.findAllByActionSubmitStatus(Task
                .ACTION_SUBMIT_STATUS_NOT_SUBMIT);
        for (Task task : tasks) {
            es.submit(new Worker(task));
        }
    }

    private class Worker implements Runnable {
        private Task task;

        public Worker(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            //获取该任务绑定的所有帐户
            try {
                List<Account> userBindingAccount = accountService.findByUserId(task.getUserId());
                List<String> wechatTerminalHashKeys = new ArrayList<>();
                if (MessageType.COM_ADD_WECHAT_FRIEND.getCode() == task.getOperate().intValue()) {
                    userBindingAccount.forEach(i -> wechatTerminalHashKeys.add(String.format("addr-%s", i
                            .getWechatAccountId())));
                    List<String> wechatTerminalAddrs = redisService.multiGet("wechat-cloud:client",
                            wechatTerminalHashKeys);
                    Terminal.Addr addr = selectWechatTerminal(wechatTerminalAddrs);
                    MessageWarp messageWarp = new MessageWarp();
                    Message msgCommand = new Message(MessageFlag.MESSAGE_FLAG_COM.getCode(),
                            MessageType.COM_ADD_WECHAT_FRIEND.getCode(),
                            JSON.toJSONString(new WechatAddFriendCommand(task.getId(),
                                    -1,
                                    task.getTargerAccount(),
                                    task.getMessage())));

                    messageWarp.setMsg(msgCommand).setTerminalAddr(addr);
                    messageTaskQueue.add(messageWarp);

                } else if (MessageType.COM_ADD_WECHAT_FRIEND.getCode() == task.getOperate().intValue()) {
                    Map<String, String> terminals = redisService.hgetAll("wechat-cloud:client");
                    Terminal.Addr addr = selectMobileTerminal(terminals);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private Terminal.Addr selectMobileTerminal(Map<String, String> terminalAddrs) throws Exception {
        if (null != terminalAddrs && terminalAddrs.size() > 0) {
            for (Map.Entry<String, String> entry : terminalAddrs.entrySet()) {
                if(Pattern.matches("^addr-1\\d{10}",entry.getKey())){
                    String[] addrDetail = entry.getValue().split(":");
                    //eg.  wxid1:192.168.1.28:40378:1524460570696
                    String terminalUid = addrDetail[0];
                    String ip = addrDetail[1];
                    int port = Integer.parseInt(addrDetail[2]);
                    long heartbeatTs = Long.parseLong(addrDetail[3]);
                    //如果超过30s
                    if (System.currentTimeMillis() - heartbeatTs < TimeUnit.SECONDS.toMillis(30)) {
                        return new Terminal.Addr().setIp(ip).setPort(port);
                    }
                }
            }
        }
        throw new Exception("cat not find available terminal");
    }

    private Terminal.Addr selectWechatTerminal(List<String> wechatTerminalAddrs) throws Exception {
        if (null != wechatTerminalAddrs && wechatTerminalAddrs.size() > 0) {
            Random rand = new Random();
            for (String addr : wechatTerminalAddrs) {
                String targetTerminal = wechatTerminalAddrs.get(rand.nextInt(wechatTerminalAddrs.size()));
                String[] addrDetail = targetTerminal.split(":");
                //eg.  wxid1:192.168.1.28:40378:1524460570696
                String terminalUid = addrDetail[0];
                String ip = addrDetail[1];
                int port = Integer.parseInt(addrDetail[2]);
                long heartbeatTs = Long.parseLong(addrDetail[3]);
                //如果超过30s
                if (System.currentTimeMillis() - heartbeatTs < TimeUnit.SECONDS.toMillis(30)) {
                    return new Terminal.Addr().setIp(ip).setPort(port);
                }
            }
        }
        throw new Exception("cat not find available terminal");
    }

    public MessageWarp getTask() {
        return messageTaskQueue.poll();
    }

    public void stop(){
        if(null != es){
            es.shutdown();
        }
    }
}
