#!/bin/sh

#说明
show_usage="args: [-ip , -port] [--local ip=, --binding port=]"

#本地ip
ip=""

#绑定端口
port=""


#获取参数
while [ -n "$1" ]
do
        case "$1" in
                -ip) ip=$2; shift 2;;
                -port) port=$2; shift 2;;
                --) break ;;
                *) echo $1,$2,$show_usage; break ;;
        esac
done

if [ -z $ip ] || [ -z $port ]; then
        echo $show_usage
        exit 0
fi

cd `dirname $0`/..;
APP_NAME=wechat-cloud-server-0.0.1
PIDS=`ps -f | grep java | grep $APP_NAME | awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The application already started!"
    echo "PID: $PIDS"
    exit 1
fi

if [ ! -d "logs" ]; then
  mkdir logs
fi

if [ ! -d "logs/wechat-cloud-server" ]; then
  mkdir logs/wechat-cloud-server
fi

nohup java -jar -Dip=$ip -Dport=$port  wechat-cloud-server/target/wechat-cloud-server-0.0.1.jar  > logs/wechat-cloud-server/startup-`date +%Y-%m-%d`.log 2>&1 &
echo wechat-cloud-server start successed.