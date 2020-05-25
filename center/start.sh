##!/bin/sh
#echo 'start'
#echo $$
#echo $!
#echo $-
#echo $0
PRO_DIR=`dirname $0`;
PRO_DIR=`cd $PRO_DIR/..;pwd`;
TIME_SUFFIX=`date +%Y%m%d%H`;
#echo $PRO_DIR

case $1 in
	start)
		nohup java -jar  -Xms2048M -Xmx2048M -XX:PermSize=128M -XX:MaxPermSize=256M  center_server-1.0-SNAPSHOT.jar >> /dev/null 2>>error.log &
		#nohup free -m >> /dev/null 2>&1 &
		#echo $!;
		echo $! > pid;
		echo -e "\033[32;31;1;2m start center server success!! \033[m";
		;;	

	restart)
		pid=`cat ./pid`;
		process=`ps -ef | grep $pid | grep -v "grep"`;
		echo $process;
		if [ "$process" == "" ]; then
			./start.sh start;
			exit;
		else
			./start.sh stop;
		fi

		while true
		do
			process=`ps -ef | grep $pid | grep -v "grep"`;
			if [ "$process" == "" ]; then
				./start.sh start;
			break;
			else
				sleep 1;
				echo "process exsits"
			fi
		done
		;;

	stop)
		#echo `cat pid`;
		kill `cat ./pid`;
		echo -e "\033[32;31;1;2m stop center server success!! \033[m";
		;;

	check)
		ps -ef | grep `cat ./pid` | grep center_server;	
		;;
	
esac

