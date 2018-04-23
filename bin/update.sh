#!/bin/bash

cd `dirname $0`/..;

git reset --hard;
git pull origin master
chmod a+x bin/*.sh

mvn clean package -Dmaven.test.skip=true