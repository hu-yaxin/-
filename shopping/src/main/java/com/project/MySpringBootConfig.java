package com.project;

import com.google.common.collect.Lists;
import com.project.interceptors.AdminAuthorityInterceptor;
import com.project.interceptors.PortalAutorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class MySpringBootConfig implements WebMvcConfigurer {
    @Autowired
    AdminAuthorityInterceptor adminAuthorityInterceptor;
    @Autowired
    PortalAutorityInterceptor portalAutorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(adminAuthorityInterceptor).addPathPatterns("/manager/**")
        .excludePathPatterns("/manager/login.do/**");


        List<String> addPaterns= Lists.newArrayList();
        addPaterns.add("/user/**");
        addPaterns.add("/cart/**");
        addPaterns.add("/shipping/**");
        addPaterns.add("/order/**");

        List<String> excludePathPaterns=Lists.newArrayList();
        excludePathPaterns.add("/user/register.do");
        excludePathPaterns.add("/user/login.do/**");
        excludePathPaterns.add("/user/forget_get_question.do/**");
        excludePathPaterns.add("/user/forget_check_answer.do/**");
        excludePathPaterns.add("/order/callback.do");
        registry.addInterceptor(portalAutorityInterceptor).addPathPatterns(addPaterns)
                .excludePathPatterns(excludePathPaterns);
    }
}
