function getCookie(str) {
    const value = document.cookie.match('(^|;) ?' + str + '=([^;]*)(;|$)');
    return value ? value[2] : null;
}

function getCsrfToken() {
    let token = getCookie("XSRF-TOKEN");
    console.log(token);
    return token;
}