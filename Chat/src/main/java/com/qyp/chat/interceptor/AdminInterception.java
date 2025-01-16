package com.qyp.chat.interceptor;

import cn.hutool.json.JSONUtil;
import com.qyp.chat.domain.R;
import com.qyp.chat.domain.dto.UserInfoDTO;
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
public class AdminInterception implements HandlerInterceptor {

    @Autowired
    UserUtils userUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoDTO userInfoDTO = userUtils.get();
        Boolean admin = userInfoDTO.getAdmin();
        if(!admin){
            //不是管理员
            String uri = request.getRequestURI();
            log.info("不是管理员不可访问：{}",uri);
            R error = R.error("非法访问！");
            String s = JSONUtil.toJsonStr(error);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(s);
            return false;
        }
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
