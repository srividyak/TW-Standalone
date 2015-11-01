#!/bin/bash
if [ $# -ne 1 ]; then
    echo "usage: standalone.sh start | stop | restart | status"
fi
cmd=$1
process="com.tapwisdom.core.Main"
start () {
    echo "Starting process ..."
    java -cp lib/*:config/ $process
    echo "Started!"
}

stop() {
    kill -9 `ps -ef | grep $process | grep -v "grep" | awk '{print $2}'`
}

restart() {
    stop
    start
}

status() {
    pid=`ps -ef | grep $process | grep -v "grep" | awk '{print $2}'`
    if [ -z $pid ]
    then
        echo "process is stopped"
    else
        echo "process is running"
    fi
}

cd TW-Standalone-1.0
if [ "$cmd" = "start" ]
then
    pid=`ps -ef | grep $process | grep -v "grep" | awk '{print $2}'`
    if [ -z $pid ]
    then
        start
    else
        echo "process already started"
    fi
elif [ "$cmd" = "restart" ]
then
    restart
elif [ "$cmd" = "stop" ]
then
    stop
elif [ "$cmd" = "status" ]
then
    status
else
    echo "invalid command. usage: start | stop | restart | status"
fi