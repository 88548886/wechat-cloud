#!/bin/sh

cd `dirname $0`/..;
APP_NAME=wechat-cloud-server-discovery-0.0.1
PIDS=`ps -f | grep java | grep $APP_NAME | awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The application already started!"
    echo "PID: $PIDS"
    exit 1
fi

if [ ! -d "logs" ]; then
  mkdir logs
fi

if [ ! -d "logs/wechat-cloud-server-discovery" ]; then
  mkdir logs/wechat-cloud-server-discovery
fi

nohup java -jar  wechat-cloud-server-discovery/target/wechat-cloud-server-discovery-0.0.1.jar  > logs/wechat-cloud-server-discovery/startup-`date +%Y-%m-%d`.log 2>&1 &
echo wechat-cloud-server-discovery start successed.