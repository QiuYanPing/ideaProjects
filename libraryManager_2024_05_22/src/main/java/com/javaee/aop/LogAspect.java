package com.javaee.aop;

import com.alibaba.fastjson.JSONObject;
import com.javaee.mapper.OperateLogMapper;
import com.javaee.pojo.OperateLog;
import com.javaee.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {
    @Autowired
    HttpServletRequest request;
    @Autowired
    OperateLogMapper operateLogMapper;
    @Around("@annotation(com.javaee.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        /*String jwt = request.getHeader("token");
        Claims claims = JwtUtils.parseJwt(jwt);
        Integer operateUser = (Integer) claims.get("id");*/
        Integer operateUser =1;

        LocalDateTime operateTime = LocalDateTime.now();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.toString(args);
        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        String resultValue = JSONObject.toJSONString(result);
        Long costTime = end - begin;
        OperateLog operateLog = new OperateLog(null,operateUser,operateTime,className,methodName,methodParams,resultValue,costTime);
        operateLogMapper.insert(operateLog);
        log.info("AOP记录操作日志：{}",operateLog);
        return result;
    }
}
