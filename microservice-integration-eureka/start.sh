#!bin/bash
set -e

echo $*

if [[ -z "${JAVA_HOME}" ]]; then
    echo "请配置JAVA_HOME环境变量"
    exit 1
fi

export JAVA_HOME
export JAVA="$JAVA_HOME/bin/java"

## app name
APP_NAME=${APP_NAME}

APP_PORT=${APP_PORT}

## java jar包路径
APP_HOME=${APP_HOME}

# echo "APP_HOME====${APP_HOME}"

[[ ! -d ${APP_HOME} ]] && mkdir -p ${APP_HOME}

## 日志路径
APP_LOG=${APP_HOME}/log
[[ ! -d ${APP_LOG} ]] && mkdir -p ${APP_LOG}

## 服务名
SERVICE_NAME=microservice-integration-${APP_NAME}

## jar包的名字
JAR_NAME=`find ${APP_HOME} -name ${SERVICE_NAME}*\.jar`

## 生成进程文件
PID=${APP_HOME}/${SERVICE_NAME}\.pid

## 进入服务存放的文件夹

#java虚拟机启动参数
JAVA_OPTS=" -server -Xms512m -Xmx512m -Xmn256m -Djava.awt.headless=true
            -XX:MaxPermSize=128m -XX:+UseConcMarkSweepGC
            -XX:+PrintGC -Xloggc:${APP_LOG}/${APP_NAME}_GC.log"

function health_check(){
  time=0
  while :
     do
        sleep 5
        echo ${time};
        if [[ time -gt 12 ]]; then
            return 1
        else
            echo "健康检查===="
            status_code=$(curl -m 1 -s -o /dev/null -w %{http_code} localhost:3000)
            if [[ "${status_code}" -eq "200" ]]; then
              echo "健康检查成功======"
              return 0
              break;
            else
              let ++time;
            fi
        fi
     done
}

case "$1" in

    'start')
        if [[ -f "${PID}" ]]; then
            echo "程序已启动"
        fi
        nohup ${JAVA} ${JAVA_OPTS} -jar ${JAR_NAME} >${APP_LOG}/${APP_NAME}.log 2>&1 &
        PID_CODE=$!
        echo "pid_code = ==== ${PID_CODE}"
        echo "${SERVICE_NAME} is starting "
        # 健康检查
        CHECK_CODE=$(echo health_check)
        if [[ "${CHECK_CODE}" -eq "0" ]]; then
            echo "success ${SERVICE_NAME} 启动成功 "
            echo ${PID_CODE} > ${PID}
        else
            echo "false ${SERVICE_NAME}启动失败"
        fi
    ;;

    'stop')
        if [[ -f "${PID}" ]]; then
            echo "${PID}将被关闭"
            # 杀死进程id即edu-service-user.pid
            kill -15 `cat ${PID}`
            # 删除进程pid的文件
            rm -rf ${PID}
        else
            echo "程序未启动"
        fi
    ;;

    'restart')
        stop
        sleep 1
        start
    ;;

    'status')
        echo "PID=====${PID}"
         if [[ -f "${PID}" ]]; then
            echo "程序运行中 pid========`cat ${PID}`"
        else
            echo "程序未启动"
        fi
    ;;

     *)

    echo "Usage $0 { start | stop | status | restart  }"
    exit
esac

exit 0