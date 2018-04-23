#!/bin/bash

cd `dirname $0`/..;

git reset --hard;
git pull origin master
chmod a+x bin/*.sh

bin/deploy.sh