package com.tpson.kuluagent.intercepter;

import com.tpson.kuluagent.controller.LoginContrller;
import com.tpson.kuluagent.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Zhangka in 2018/05/09
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String username = CookieUtils.getCookie(request, CookieUtils.TOKENNAME);
        if (StringUtils.isBlank(username)) {
            response.sendRedirect("/login.html");
            return false;
        }

        if (("DELETE".equalsIgnoreCase(request.getMethod()) || "POST".equalsIgnoreCase(request.getMethod()))
                && LoginContrller.ADMIN.equalsIgnoreCase(username)) {
            throw new RuntimeException("无权操作!");
        }

        return true;
    }
}
