# wechat-cloud-client

+ 通迅协议栈通用依赖

# wechat-cloud-server

+ 处理client的连接请求及报文
+ 推送微信操作指令
+ 接收指令执行反馈(放到消息队列)

# wechat-cloud-task-manager

+ 读取数据库表的操作/动作
+ 根据缓存中的某个client(微信id)的位置信息通知被注册的service来推送指令
+ 更新数据库信息


# wechat-cloud-client

+ 测试server通迅,报文解析,RSA签名鉴权

