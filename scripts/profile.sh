#스크립트 파일에서 공용으로 사용할 'profile'과 포트 체크 로직
#!/usr/bin/env bash

#쉬고 있는 profile 찾기. real1이 사용 중이면 real2이 idle, 반대면 real1이 idle
function find_idle_profile() {
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

    if [ ${RESPONSE_CODE} -ge 400 ] #400보다 크면 에러. (40x/50x 에러 모두 포함)
    then
      CURRENT_PROFILE=real2
    else
      CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
      IDLE_PROFILE=real2
    else
      IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}"
}

#쉬고 있는 PROFILE PORT 찾기
function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile)
    if [ ${IDLE_PROFILE} == real1 ];
    then
      echo "8081"
    else
      echo "8082"
    fi
}
#bash는 값을 반환하는 기능이 없으므로 echo로 출력하고, 클라이언트에서 그 값을 잡아서 사용한다.