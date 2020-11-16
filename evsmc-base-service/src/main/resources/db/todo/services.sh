#!/bin/bash

APP_NAME=evsmc-base-service-2.3.1.1.jar

case $1 in 
    start)   
        nohup java -Dfile.encoding=UTF-8 -jar ${APP_NAME} --spring.config.location=application.yml --spring.profiles.active=none >/dev/null 2>&1 &
        echo "$!"
        echo "$!" > pid
        echo ${APP_NAME} start!
        ;;
    stop)    
        if [ -f "pid" ]; then
		kill -9 `cat pid`
                rm -f pid 
        	echo ${APP_NAME} stop!
        else
		echo ${APP_NAME} not running!
        fi
        ;;
    restart)
        "$0" stop
        sleep 3
        "$0" start
        ;;
    status)  ps -aux | grep ${APP_NAME} | grep -v 'grep'
        ;;
    log)
    	case $2 in
	debug)
		tail -f -n ${3-400} logs/debug.log
		;;
	error)
		tail -f -n ${3-400} logs/error.log
		;;
	*)
		echo "Example: services.sh log {debug|error}" ;;
	esac
        ;;
    *)       
        echo "Example: services.sh [start|stop|restart|status]" ;;
esac

