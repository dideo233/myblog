#배포할 신규 버전 스프링 부트를 stop.sh로 종료한 'profile' 실행
#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app3/myblog/
PROJECT_NAME=myblog

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 어플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*jar | tail -n 1)

echo "> JAR_NAME: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다"

nohup java -jar \
        -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties,/home/ec2-user/app3/myblog/application-oauth.properties,/home/ec2-user/app3/myblog/application-db.properties \
        -Dspring.profiles.active=$IDLE_PROFILE \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
#deploy.sh와의 차이점 : IDLE_PROFILE을 통해 properties를 가지고 오며 active profile을 지정한다.