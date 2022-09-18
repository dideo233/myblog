# myblog Project

개인 커스텀 블로그 개발 프로젝트 (개발기간 : 8.31 ~ 9.16) <br><br>
https://dideoblog.com 

## 개요
>- 개인 블로그를 제작하며 기본적인 CRUD 및 벡앤드 서버 개발 능력의 향상시킨다. 
>- AWS를 이용하여 CI/CD 무중단 배포까지 진행한다.  
>- 최종적으로 개발부터 배포, 유지/보수에 이르기까지 모든 과정을 경험한다.

<br>

## 개발 기술
### ▶︎백엔드
- java 11
- SpringBoot 
- SpringSecurity
- SpringData JPA

### ▶︎빌드 도구
- Gradle

### ▶︎데이터베이스
- MySQL

### ▶︎프론트엔드
- HTML/CSS/JS
- BootStrap5
- Thymeleaf

### ▶︎배포 환경
- AWS EC2
- AWS Route53
- AWS S3 
- AWS CodeDeploy
- Travis CI

<br>

## 시스템 구성도

![a drawio](https://user-images.githubusercontent.com/61315671/190233922-04824e4a-50e0-4a42-8da8-2f85ca788de9.svg)

## E-R 다이어그램

![vectorpaint](https://user-images.githubusercontent.com/61315671/190232995-26bf4453-d6d4-4b1c-abb5-4674e33760b9.svg)

회원, 게시글, 카테고리, 댓글, 임시저장 게시글, 로그인 유저 세션 저장용 테이블로 구성됨

## 구현 기능
### ▶︎소셜 로그인

![소셜 로그인](https://user-images.githubusercontent.com/61315671/190469700-35abf87c-803e-4039-a50e-3f7f7be4a404.gif)

>스프링 시큐리티 기반 OAuth2 소셜 로그인 (카카오, 구글) 구현했습니다. **인터페이스로 추상화하여 어떤 소셜 서비스로 로그인을 진행해도 통일된 유저 객체를 생성**할 수 있도록 했습니다.

<br>

### ▶︎관리자 권한 지닌 유저의 마크다운 에디터를 이용한 게시글 작성, 수정, 삭제

![마크다운 게시글 작성 조회 삭제](https://user-images.githubusercontent.com/61315671/190470211-8f173d72-f494-40d5-9578-a83013218ded.gif)

>마크다운 에디터는 기본적으로 이미지 업로드 기능을 제공하지만 base64 방식으로 바이너리 데이터를 통째로 DB에 저장합니다. 이를 마크다운 에디터의 **콜백 기능으로 별개 이미지 서버에 이미지 파일을 저장**했고, URL만 DB에 저장하는 식으로 변경했습니다. 

>마크다운 에디터로 작성한 컨텐츠는 마크다운 뷰어에서 CSR 방식으로 렌더링됩니다. 뷰어를 통하지 않은 컨텐츠는 마크다운 기호를 포함하여 사용자 화면에 드러나게 됩니다. 이런 지저분한 부분을 정리하기 위해 **SSR 방식으로 서버에서 렌더링하도록 변경**했습니다. 

<br>

### ▶︎셀프 조인을 이용한 댓글 및 대댓글 작성 

![댓글 테스트](https://user-images.githubusercontent.com/61315671/190472251-4f3fbbd6-8527-421f-9d75-6d6c152f2b17.gif)

>JPA 셀프 조인을 이용하여 계층형 댓글 시스템을 구현했습니다. 

<br>

### ▶︎무한 스크롤 및 일반 페이징

![페이징-min](https://user-images.githubusercontent.com/61315671/190473939-00d5a6ca-1234-4c22-9c32-9e66748f8e54.gif)

>메인 화면에서는 일정 길이 스크롤을 내렸을 때 서버로 다음 페이지를 요청하고, 이를 응답받으면 곧바로 렌더링이 진행됩니다. 사용자 입장에서는 스크롤을 내리면 계속해서 게시글이 나타나게 됩니다. 이러한 **무한 스크롤 기능을 자바스크립트의 비동기 기능으로 구현**했습니다.

>카테고리별 페이지에서는 **JPA Pageable을 이용하여 일반적인 오프셋 페이징을 구현**했습니다.

<br>

### ▶︎게시글 조회, 조회수 카운트 

![조회수 중복 카운트 방지용 쿠키 확인](https://user-images.githubusercontent.com/61315671/190472581-f76a48f2-3452-448e-9794-89794c71d305.gif)

>게시글을 조회하면 조회수가 카운트되며, 조회수 값대로 정렬하여 인기 게시글을 표시합니다. 이때 조회한 **게시글의 번호를 쿠키로 보관하도록 하여 중복 조회를 방지**했습니다. 

<br>

### ▶︎1분 단위 작성글 임시 저장

![게시글 임시 저장 테스트](https://user-images.githubusercontent.com/61315671/190474817-cbed2a29-a876-4a1f-b6e6-a7269aff58e7.gif)

>자바스크립트의 비동기 기능을 이용하여 **작성 중 게시글을 1분 간격으로 저장**하도록 했습니다. 이때 게시글 작성을 완료하지 않고 작성 화면을 벗어났을 경우, 다음 게시글을 작성할 때 임시저장된 게시글을 불러올 수 있도록 했습니다. 

<br>

### ▶︎커스텀 에러 페이지

![커스텀 에러 페이지](https://user-images.githubusercontent.com/61315671/190472639-ff9e7191-49e3-450e-83d8-0a6a5d11c1bc.PNG)

>에러가 발생했을 시 커스텀 에러 페이지로 리다이렉트 되도록 변경했습니다.

<br>

### ▶︎카카오 공유하기

![카톡 공유하기](https://user-images.githubusercontent.com/61315671/190472619-9a9e1dab-5ac7-4efd-a31c-163df5a21c52.PNG)

>작성된 게시글은 공유하기 기능을 통해서 공유할 수 있습니다. 

<br>

### ▶︎travis ci, aws, nginx를 이용한 무중단 배포 
>깃허브 Push를 시도하면 Traivs CI AWS S3, AWS CodeDeploy를 통해 EC2에 자동 빌드하도록 배포 환경을 구축했습니다. 또한 배포하는 중간에 서비스 중인 웹이 중단되었는데, 이를 Nginx Reverse Proxy 기능을 이용하여 무중단 배포가 이루어지도록 했습니다. 

### ▶︎Route53 도메인 연동 ( + HTTPS)
>Route53에서 도메인을 구매했으며 SSL 인증서를 발급받아 HTTPS를 적용할 수 있게끔 했습니다. 그리고 ELB를 생성 및 설정하여 구매한 도메인을 통해 EC2 내부의 웹 서비스까지 접속하게 했습니다. 

### ▶︎SWAP 메모리 설정을 통해 서버 중단 FIX 시도
![SWAP 메모리 확인](https://user-images.githubusercontent.com/61315671/190472661-794089e3-6625-479a-86e7-c8b171c6a051.PNG)

>무중단 배포에 성공하고 하루에도 1~2번 간격으로 중단되는 문제가 발생했고, 이를 해결하기 위해서 우선 SWAP 메모리 설정을 시도했습니다. 이후 약 5일 모니터링 과정에서 서버 중단 없이 정상 동작하는 것을 확인할 수 있었습니다.

<br>

## 소감
여태까지 스프링, 장고, 리액트 같은 프레임워크나 라이브러리를 학습하여 작은 프로젝트를 진행한 적이 있지만 이를 마무리하여 배포까지 한 경험은 없었습니다. 


<br>

일단 1차적으로 마무리하게 되었지만, 이번 프로젝트는 **개발부터 배포까지 전반적인 과정을 실습**했다는 점에서 의미가 깊습니다. 나만의 도메인 주소를 유료로 구매하여 SSL 인증서를 발급받고, 끝내 도메인 주소를 통해 내가 만든 웹 서비스에 접속했던 것에는 큰 성취감을 느꼈습니다.

<br>

그러나 배포 이후에도 끊임없이 문제를 맞닥뜨렸는데, 이런 문제는 쉬지 않고 잡아나갈 수밖에 없었습니다. 이 과정에서 저는 **에러 로그 추적과 트러블 슈팅**의 중요성까지 체감하게 되었습니다.

<br>

개발자는 끊임없이 학습해야 한다, 문제 해결 능력이 필요하다, 개발자가 되기 위해 공부를 하다 보면 교재나 강사의 입을 통해 머릿속에 담게 되는 말일 것입니다. 이걸 단순히 알고 있는 게 아니라 경험으로 체감할 수 있었던 것이 가장 큰 성과라고 생각합니다. 


<br>

## To Do List
차후 진행할 예정 사항

### 구현
- 비밀 댓글
- 게시글 연관글 
- 검색
  -  구글(or 네이버) 검색 엔진
  -  블로그 게시글 내에서 검색
- 현재 카테고리를 velog 태그 시스템처럼 개선
- 오프셋 페이징을 커서 페이징으로
- 로그 추적기 (+ 에러 부분 손보기)
- SEO 최적화 
- 코드 리펙토링
