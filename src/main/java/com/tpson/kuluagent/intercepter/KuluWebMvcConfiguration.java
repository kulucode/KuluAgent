package com.tpson.kuluagent.intercepter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Zhangka in 2018/05/09
 */
@Configuration
public class KuluWebMvcConfiguration extends WebMvcConfigurerAdapter /*WebMvcConfigurationSupport */{
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/", "/**/*.do", "/**/*.html").excludePathPatterns("/login.html", "/login.do", "/logout.do");
        super.addInterceptors(registry);
    }
}
