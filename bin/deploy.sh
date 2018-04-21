#!/bin/sh
cd `dirname $0`/..;
# 检验参数 -- 必须传入 dev | prod | test 来选择运行环境
if [ ! -n "$1" ] ;then
     echo "you must choose a environment,please enter a choose [dev | prod | test] "
     exit 1;
else
     echo "the environment you choose is [$1]"
fi

git reset --hard;
git pull origin master
chmod a+x bin/*.sh

mvn clean package -Dmaven.test.skip=true -P $1


