//package com.yf.interceptor;
//
//import com.example.demo.utils.UserHolder;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//
//@Component
//public class LoginInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private UserHolder userHolder;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        // 1. 判断是否需要拦截（检查当前是否有用户）
//        if (userHolder.getUser() == null) {
//            // 2. 没有，需要拦截，设置状态码
//            response.setStatus(401);
//            // 3. 拦截
//            return false;
//        }
//        // 4. 有用户，则放行
//        return true;
//    }
//}