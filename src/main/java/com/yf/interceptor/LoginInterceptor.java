package com.yf.interceptor;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.yf.constant.RedisConstants;
import com.yf.entity.dto.UserDTO;
import com.yf.util.HutoolJwtUtil;
import com.yf.util.UserHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;




@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private HutoolJwtUtil hutoolJwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            log.warn("Missing or empty Authorization header");
            response.setStatus(401);
            return false;
        }

        // 去除 "Bearer " 前缀（如果存在）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token
        if (!hutoolJwtUtil.validateToken(token)) {
            log.warn("Invalid token: {}", token);
            response.setStatus(401);
            return false;
        }

        // 从token中解析用户ID
        String userId = hutoolJwtUtil.getUserId(token);
        if (userId == null) {
            log.warn("Cannot extract userId from token");
            response.setStatus(401);
            return false;
        }

        String key = RedisConstants.LOGIN_USER_KEY + userId;

        // 尝试从Redis中获取完整用户信息
        Object userObj = redisTemplate.opsForHash().get(key, "userDTO");
        UserDTO userDTO;

        if (userObj != null) {
            userDTO = (UserDTO) userObj;
        } else {
            // 如果Redis中没有，尝试从token中恢复基础信息
            JWT jwt = JWTUtil.parseToken(token);
            String username = jwt.getPayload("username").toString();

            userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setUsername(username);

            // 补充角色信息（如果存在于Redis）
            Object roleObj = redisTemplate.opsForHash().get(key, "role");
            if (roleObj != null) {
                userDTO.setRoleId((String) roleObj);
            } else {
                userDTO.setRoleId("ROLE_003"); // 默认角色
            }
        }

        // 保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理ThreadLocal，防止内存泄漏
        UserHolder.removeUser();
    }
}