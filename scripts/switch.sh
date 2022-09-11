#엔진엑스가 바라보는 스프링 부트를 최신 버전으로 변경
#! /usn/bin/env bash
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
  IDLE_PORT=$(find_idle_port)

  echo "> 전환할 Port: $IDLE_PORT"
  echo "> Port 전환"
  echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

  echo " > 엔진엑스 Reload"
  sudo service nginx reload
  #restart는 끊김이 있으나 reload는 끊김이 없음. 중요 설정은 반영되지 않으나 외부 설정 파일을 불러오는 것이라 reolad 사용 가능한 것
}
