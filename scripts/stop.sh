#기존 엔진엑스에 연결되어 있지 않지만, 실행 중인 스프링 부트 종료
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH) #현재 stop.sh가 속한 경로를 탐색
source ${ABSDIR}/profile.sh #일종의 import 구문. profile.sh의 함수 사용 가능

IDLE_PORT=$(find_idle_port) #함수 사용

echo "> $IDLE_PORT 에서 구동중인 앱 PID 확인"

IDLE_PID=$(lsof -ti tcp:${IDLE_PORT})

if [ -z ${IDLE_PID} ]
then
  echo "> 현재 구동중인 앱이 없으므로 종료하지 않습니다"
else
  echo "> kill -15 $IDLE_PID"
  kill -15 ${IDLE_PID}
  sleep 5
fi