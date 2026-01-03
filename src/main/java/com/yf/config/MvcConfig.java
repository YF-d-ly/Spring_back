package com.yf.config;


import com.yf.interceptor.LoginInterceptor;
import com.yf.interceptor.RefreshTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {


    @Autowired
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // token刷新的拦截器
        registry.addInterceptor(refreshTokenInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .order(0);

        // 登录拦截器
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/register",
                        "/auth/code",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/doc.html/**")
                .order(1); // 排除不需要登录的路径
    }
}