package com.tpson.kuluagent.controller;

import com.tpson.kuluagent.util.CookieUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Zhangka in 2018/05/09
 */
@Controller
public class LoginContrller {
    public static final String SUPER_ADMIN = "admin";
    public static final String SUPER_ADMIN_PASSWORD = "admin123";
    public static final String ADMIN = "kulu";
    public static final String ADMIN_PASSWORD = "kulu123";
    @RequestMapping("/login.html")
    public String html() {
        return "login";
    }

    @RequestMapping("/login.do")
    public String login(String username, String password, HttpServletResponse resp) {
        if ((SUPER_ADMIN.equalsIgnoreCase(username) && SUPER_ADMIN_PASSWORD.equalsIgnoreCase(password))
                || (ADMIN.equalsIgnoreCase(username) && ADMIN_PASSWORD.equalsIgnoreCase(password))) {
            CookieUtils.setCookie(resp, CookieUtils.TOKENNAME, username);
            return "redirect:/";
        }

        throw new RuntimeException("用户名或密码错误!");
    }

    @RequestMapping("/logout.do")
    public String logout(HttpServletResponse resp) {
        CookieUtils.setCookie(resp, CookieUtils.TOKENNAME, null, 0);
        return "redirect:login.html";
    }
}
