#!/bin/sh

# 检验参数 -- 必须传入 dev | prod | test 来选择运行环境
if [ ! -n "$1" ] ;then
     echo "you must choose a environment,please enter a choose [dev | prod | test] "
     exit 1;
else
     echo "the environment you choose is [$1]"
fi

cd `dirname $0`/..;
APP_NAME=mall
PIDS=`ps -f | grep java | grep $APP_NAME | awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The application already started!"
    echo "PID: $PIDS"
    exit 1
fi
rm -f tpid

if [ ! -d "logs" ]; then
  mkdir logs
fi

nohup java -jar -Dspring.profiles.active=$1 target/mall-0.0.1-SNAPSHOT.jar  > logs/startup-`date +%Y-%m-%d`.log 2>&1 &
echo $! > tpid
echo Start Success !
sleep 2
PIDS=`ps -f | grep java | grep $APP_NAME | awk '{print $2}'`
echo "PID: $PIDS"