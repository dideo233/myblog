package com.mycustomblog.blog.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

//예외 로그 처리용
//모든 컨트롤러에서 발생하는 예외를 한 곳에서 처리하기 위한 용도 (404 같은 건 따로 처리 필요)
@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public String handleException(Principal principal, HttpServletRequest req, RuntimeException e) {
        if (principal != null) {
            log.info("'{}' requested '{}'", principal.getName(), req.getRequestURI());
        } else {
            log.info("requested '{}'", req.getRequestURI());
        }
        log.error("bad request", e.getMessage());
        return "redirect:/error";
    }
}