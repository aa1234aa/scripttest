#!/bin/sh

# 进入bin目录
cd `dirname $0`
# 返回到上一级项目根目录路径
cd ..
# 打印项目根目录绝对路径
# `pwd` 执行系统命令并获得结果
BASE_PATH=`pwd`

CONFIG_DIR=${BASE_PATH}"/config/"

cat "$CONFIG_DIR"/META-INF/build-info.properties

