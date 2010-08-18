#!/bin/bash
#set -xv
clear
if [ "x$JAVA_HOME" != "x" ]; then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA="java"
fi


var=`$JAVA -classpath ../server/default/deploy/KiWi.ear/KiWi.jar kiwi.JavaVersionChecker 1.6`

if [ $var != "true" ]
then
    echo "Wrong java"
    exit
fi

/bin/sh run.sh

