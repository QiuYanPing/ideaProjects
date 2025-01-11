package com.qyp.chat.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
import com.qyp.chat.util.RedisUtils;
import com.qyp.chat.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UserUtils userUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取token
        String token = request.getHeader("token");
        UserInfoDTO userInfoDTO = redisUtils.getUserInfoDTO(token, UserInfoDTO.class);
        if(userInfoDTO ==null){
            String uri = request.getRequestURI();
            log.error(uri);
            R error = R.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            //设置返回信息
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(notLogin);
            return false;

        }
        //保存到threadLocal中，下次需要时，直接获取用户信息
        userUtils.set(userInfoDTO);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
