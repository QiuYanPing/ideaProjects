package com.qyp.chat.exception;

import com.qyp.chat.domain.R;
import com.qyp.chat.exception.enums.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public R globalExceptionHandler(HttpServletRequest request,Exception e){
        String uri = request.getRequestURI();
        log.error("请求地址:{}错误",uri);
        e.printStackTrace();
        R r = R.error("对不起，操作错误，请联系管理员！");
        if(e instanceof BusinessException){
            r.setCode(600);
            r.setMsg(((BusinessException) e).getMsg());
        }else if(e instanceof UnauthException){
            r.setCode(ExceptionEnum.UNAUTH.getCode());
            r.setMsg(ExceptionEnum.UNAUTH.getMsg());
        }
        return r;
    }
}
