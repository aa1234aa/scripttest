#! /bin/bash

#======================================================================
# 项目启动shell脚本
# boot目录: spring boot jar包
# config目录: 配置文件目录
# logs目录: 项目运行日志目录
# logs/spring-boot-assembly_startup.log: 记录启动日志
# logs/back目录: 项目运行日志备份目录
# nohup后台运行
#
# author: zhaogd
# date: 2019-01-22
#======================================================================

# 项目名称
APPLICATION="toolkit-kafka-sender"

# 项目启动jar包名称
APPLICATION_JAR="${APPLICATION}.jar"

# bin目录绝对路径
BIN_PATH=$(cd `dirname $0`; pwd)
# 进入bin目录
cd `dirname $0`
# 返回到上一级项目根目录路径
cd ..
# 打印项目根目录绝对路径
# `pwd` 执行系统命令并获得结果
BASE_PATH=`pwd`

# 外部配置文件绝对目录,如果是目录需要/结尾，也可以直接指定文件
# 如果指定的是目录,spring则会读取目录中的所有配置文件
CONFIG_DIR=${BASE_PATH}"/config/"

# 日志配置文件路径
LOG_CONFIG_FILE="logback-spring.xml"
LOG_CONFIG_PATH=${CONFIG_DIR}/${LOG_CONFIG_FILE}

# 项目日志输出绝对路径
LOG_DIR=${BASE_PATH}"/logs"
JVM_LOG_DIR=${BASE_PATH}"/jvm_logs"
LOG_FILE="service.log"
LOG_PATH="${LOG_DIR}/${LOG_FILE}"

# 项目启动日志输出绝对路径
LOG_STARTUP_PATH="${LOG_DIR}/${APPLICATION}_startup.log"

# 当前时间
NOW=`date +'%Y-%m-%d-%H-%M-%S'`
NOW_PRETTY=`date +'%Y-%m-%d %H:%M:%S'`

# 启动日志
STARTUP_LOG="================================================ ${NOW_PRETTY} ================================================\n"

# 如果logs文件夹不存在,则创建文件夹
if [[ ! -d "${LOG_DIR}" ]]; then
  mkdir "${LOG_DIR}"
fi

# 如果jvm_logs文件夹不存在,则创建文件夹
if [[ ! -d "${JVM_LOG_DIR}" ]]; then
  mkdir "${JVM_LOG_DIR}"
fi

# 如果项目运行日志不存在,则新建
if [[ ! -f "${LOG_PATH}" ]]; then
	echo "" > ${LOG_PATH}
fi

#==========================================================================================
# JVM Configuration
# -Xmx256m:设置JVM最大可用内存为256m,根据项目实际情况而定，建议最小和最大设置成一样。
# -Xms256m:设置JVM初始内存。此值可以设置与-Xmx相同,以避免每次垃圾回收完成后JVM重新分配内存
# -Xmn512m:设置年轻代大小为512m。整个JVM内存大小=年轻代大小 + 年老代大小 + 持久代大小。
#          持久代一般固定大小为64m,所以增大年轻代,将会减小年老代大小。此值对系统性能影响较大,Sun官方推荐配置为整个堆的3/8
# -XX:MetaspaceSize=64m:存储class的内存大小,该值越大触发Metaspace GC的时机就越晚
# -XX:MaxMetaspaceSize=320m:限制Metaspace增长的上限，防止因为某些情况导致Metaspace无限的使用本地内存，影响到其他程序
# -XX:-OmitStackTraceInFastThrow:解决重复异常不打印堆栈信息问题
#==========================================================================================
JAVA_OPT="-Xmx2048M -Xms2048M"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow"
JAVA_OPT="${JAVA_OPT} -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"
JAVA_OPT="${JAVA_OPT} -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark"
JAVA_OPT="${JAVA_OPT} -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+HeapDumpOnOutOfMemoryError"
JAVA_OPT="${JAVA_OPT} -XX:+PrintClassHistogramBeforeFullGC -XX:+PrintClassHistogramAfterFullGC"
JAVA_OPT="${JAVA_OPT} -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC"
JAVA_OPT="${JAVA_OPT} -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
JAVA_OPT="${JAVA_OPT} -XX:ErrorFile=${JVM_LOG_DIR}/hs_err_pid%p.log  -XX:HeapDumpPath=${JVM_LOG_DIR}"
JAVA_OPT="${JAVA_OPT} -Xloggc:${JVM_LOG_DIR}/gc.log"

#=======================================================
# 将命令启动相关日志追加到日志文件
#=======================================================

# 输出项目名称
STARTUP_LOG="${STARTUP_LOG}application name: ${APPLICATION}\n"
# 输出jar包名称
STARTUP_LOG="${STARTUP_LOG}application jar name: ${APPLICATION_JAR}\n"
# 输出项目bin路径
STARTUP_LOG="${STARTUP_LOG}application bin  path: ${BIN_PATH}\n"
# 输出项目根目录
STARTUP_LOG="${STARTUP_LOG}application root path: ${BASE_PATH}\n"
# 打印日志路径
STARTUP_LOG="${STARTUP_LOG}application log  path: ${LOG_PATH}\n"
# 打印JVM配置
STARTUP_LOG="${STARTUP_LOG}application JAVA_OPT : ${JAVA_OPT}\n"


# 打印启动命令
STARTUP_LOG="${STARTUP_LOG}application background startup command: nohup java ${JAVA_OPT} -jar ${BASE_PATH}/boot/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} > ${LOG_PATH} 2>&1 &\n"


#======================================================================
# 执行启动命令：后台启动项目,并将日志输出到项目根目录下的logs文件夹下
#======================================================================
nohup java ${JAVA_OPT} -jar ${BASE_PATH}/boot/${APPLICATION_JAR} --spring.config.location=${CONFIG_DIR} --logging.config=${LOG_CONFIG_PATH} >/dev/null 2>&1 &


# 进程ID
PID=$(ps -ef | grep "${APPLICATION_JAR}" | grep -v grep | awk '{ print $2 }')
STARTUP_LOG="${STARTUP_LOG}application pid: ${PID}\n"

# 启动日志追加到启动日志文件中
echo -e ${STARTUP_LOG} >> ${LOG_STARTUP_PATH}
# 打印启动日志
echo -e ${STARTUP_LOG}

# 打印项目日志
tail -F ${LOG_PATH}
