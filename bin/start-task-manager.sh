#!/bin/sh

cd `dirname $0`/..;
APP_NAME=wechat-cloud-task-manager-0.0.1
PIDS=`ps -f | grep java | grep $APP_NAME | awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The application already started!"
    echo "PID: $PIDS"
    exit 1
fi

if [ ! -d "logs" ]; then
  mkdir logs
fi

if [ ! -d "logs/wechat-cloud-task-manager" ]; then
  mkdir logs/wechat-cloud-task-manager
fi

nohup java -jar  wechat-cloud-task-manager/target/wechat-cloud-task-manager-0.0.1.jar  > logs/wechat-cloud-task-manager/startup-`date +%Y-%m-%d`.log 2>&1 &
echo wechat-cloud-task-manager start successed.