#!/bin/sh
APP_NAME=wechat-cloud-server-0.0.1
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'stop process...'
    kill -15 $tpid
fi
sleep 5
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo kikk process!
    kill -9 $tpid
else
    echo stop successed!
fi