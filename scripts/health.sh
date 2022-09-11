#'start.sh'로 실행시킨 프로젝트가 정상적으로 실행되는지 체크
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10
for RETRY_COUNT in {1..10}
do
RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
UP_C0UNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

if [ ${UP_C0UNT} -ge 1 ]
then # $up_count >= 1 ("real" 문자열이 있는지 검증)
  echo "> Health check 성공"
  switch_proxy #엔진엑스와 연결되지 않은 포트로 잘 동작되면 프록시 설정을 변경.
  break
else
  echo "> Health check의 응답을 알 수 없거나 혹은 실행상태가 아닙니다 "
  echo "> Health check: ${RESPONSE}"
fi

if [ ${RETRY_COUNT} -eq 10 ]
then
  echo "> Health check 실패 "
  echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다 "
  exit 1
fi

  echo "> Health check 연결실패 재시도..."
  sleep 10
done
