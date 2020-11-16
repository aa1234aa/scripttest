#! /bin/bash

#======================================================================
# 项目重启shell脚本
# 先调用shutdown.sh停服
# 然后调用startup.sh启动服务
#
# author: zhaogd
# date: 2019-01-22
#======================================================================

# 项目名称
APPLICATION="toolkit-kafka-sender"

# bin目录绝对路径
BIN_PATH=$(cd `dirname $0`; pwd)

# 停服
echo stop ${APPLICATION} Application...
sh ${BIN_PATH}/shutdown.sh

sleep 2

# 启动服务
echo start ${APPLICATION} Application...
sh ${BIN_PATH}/startup.sh