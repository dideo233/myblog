//1.적재된 토큰을 찾아서 반환한다.
//2.헤더에 세팅해 요청을 보낸다.  httpOnlyFalse 설정 필요
function getCookie(str) {
    const value = document.cookie.match('(^|;) ?' + str + '=([^;]*)(;|$)');
    return value ? value[2] : null;
}

function getCsrfToken() {
    let token = getCookie("XSRF-TOKEN");
    console.log(token);
    return token;
}
