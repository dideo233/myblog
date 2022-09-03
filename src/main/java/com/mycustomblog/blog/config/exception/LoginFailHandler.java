package com.mycustomblog.blog.config.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginFailHandler extends SimpleUrlAuthenticationFailureHandler{
    //중복 이메일 에러 시 에러 메시지와 함께 다시 login 페이지로
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String msg = "error";

        if(exception instanceof OAuth2AuthenticationException) {
            msg = "duplicatedEmail";
            request.setAttribute("errMsg", msg);
        }

        setDefaultFailureUrl("/login?err="+msg);
        super.onAuthenticationFailure(request, response, exception);
    }
}
