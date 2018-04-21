package com.mjoys;

import com.alibaba.fastjson.JSON;
import com.mjoys.po.Account;
import com.mjoys.po.Task;
import com.mjoys.protocol.Message;
import com.mjoys.protocol.MessageFlag;
import com.mjoys.protocol.MessageType;
import com.mjoys.service.IAccountService;
import com.mjoys.service.IRedisService;
import com.mjoys.service.ITaskService;
import com.mjoys.service.impl.AccountServiceImpl;
import com.mjoys.service.impl.RedisServiceImpl;
import com.mjoys.service.impl.TaskServiceImpl;
import com.mjoys.utils.SpringBeanUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class TaskManager implements Runnable {
    private Pattern p = Pattern.compile("\\d{11}");
    private IRedisService redisService = SpringBeanUtil.getBean(RedisServiceImpl.class);
    private ITaskService taskService = SpringBeanUtil.getBean(TaskServiceImpl.class);
    private IAccountService accountService = SpringBeanUtil.getBean(AccountServiceImpl.class);
//    private Map<String, Client> clientGroup = new HashMap<>();

    public TaskManager() {
/*        Map<String, String> serversCacheMap = redisService.hgetAll("wechat-cloud:servers");
        if (null != serversCacheMap && serversCacheMap.size() > 0) {
            for (Map.Entry<String, String> entry : serversCacheMap.entrySet()) {
                long latest = Long.parseLong(entry.getValue());
//                if ((System.currentTimeMillis() - latest) < TimeUnit.SECONDS.toMillis(10)) {
                    String[] hostPort = entry.getKey().split(":");
                    Client client = new Client().start(hostPort[0], Integer.parseInt(hostPort[1]), "System",
                            "default", 300);
                    clientGroup.put(entry.getKey(), client);
//                }
//                }
            }
        }*/
    }

    @Override
    public void run() {
        List<Task> tasks = taskService.findAllByActionSubmitStatus(Task
                .ACTION_SUBMIT_STATUS_NOT_SUBMIT);
        for (Task task : tasks) {
            try {
                System.out.println("任务" + task.getId() + "\t" + task.getOperate());
                List<Account> wechatTermianls = accountService.findByUserId(task.getUserId());
                List<String> hashKeys = new ArrayList<>();
                if (MessageType.COM_ADD_WECHAT_FRIEND.getCode() == task.getOperate().intValue()) {
                    wechatTermianls.forEach(i -> hashKeys.add(i.getWechatAccountId()));
                    List<String> wechatTermianlAddr = redisService.multiGet("wechat-cloud:client:addr", hashKeys);

                    String terminalAddr = wechatTermianlAddr.get(0);
                    System.out.println("terminalAddr:" + terminalAddr);
                    String[] terminalAddrSplit = terminalAddr.split(":");
                    String terminalUid = terminalAddrSplit[0];
                    String terminalIp = terminalAddrSplit[1];
                    String terminalPort = terminalAddrSplit[2];

                    String serverIpPort = redisService.hget("wechat-cloud:client:server", terminalUid);
                    System.out.println("serverIpPort:" + serverIpPort);
    //                System.out.println(clientGroup.containsKey(serverIpPort));
                    Client client = new Client().start(serverIpPort.split(":")[0], Integer.parseInt(serverIpPort.split(":")[1]), "System",
                            "default", 300);
                    Message message = new Message(MessageFlag.MESSAGE_FLAG_SYS.getCode(), MessageType.SYS_TASK
                            .getCode(),
                            JSON.toJSONString(new com.mjoys.protocol.message.system.Task().
                                    setCommandType(MessageType.COM_ADD_WECHAT_FRIEND.getCode()).
                                    setExecuteTime(-1).
                                    setId(task.getId()).
                                    setReceiver(task.getTargerAccount()).
                                    setTerminalAddr(String.format("%s:%s", terminalIp, terminalPort)).
                                    setBussinessId(0).setMessage(task.getMessage())));
                    client.send(message);
                    client.stop();
                    System.out.println("send" + message);
                    System.out.println("send" + "微信");
                } else if (MessageType.COM_SEND_MSG.getCode() == task.getOperate()) {
                    Map<String, String> terminals = redisService.hgetAll("wechat-cloud:client:addr");
                    Set<Map.Entry<String, String>> entries = terminals.entrySet();
                    Iterator<Map.Entry<String, String>> iterator = entries.iterator();
                    List<String> msgTermianlAddr = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> next = iterator.next();
                        if (Pattern.matches("\\d{11}", next.getKey())) {
                            msgTermianlAddr.add(next.getValue());
                        }
                    }
                    String terminalAddr = msgTermianlAddr.get(0);
                    String[] terminalAddrSplit = terminalAddr.split(":");
                    String terminalUid = terminalAddrSplit[0];
                    String terminalIp = terminalAddrSplit[1];
                    String terminalPort = terminalAddrSplit[2];

                    String serverIpPort = redisService.hget("wechat-cloud:client:server", terminalUid);

    //                Client client = clientGroup.get(serverIpPort);
                    Client client = new Client().start(serverIpPort.split(":")[0], Integer.parseInt(serverIpPort.split(":")[1]), "System",
                            "default", 300);
                    client.send(new Message(MessageFlag.MESSAGE_FLAG_SYS.getCode(), MessageType.SYS_TASK.getCode(),
                            JSON.toJSONString(new com.mjoys.protocol.message.system.Task().
                                    setCommandType(MessageType.COM_SEND_MSG.getCode()).
                                    setExecuteTime(-1).
                                    setId(task.getId()).
                                    setReceiver(task.getTargerAccount()).
                                    setTerminalAddr(String.format("%s:%s", terminalIp, terminalPort)).
                                    setBussinessId(0).setMessage(task.getMessage())
                            )));
                    System.out.println("send" + "短信");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
