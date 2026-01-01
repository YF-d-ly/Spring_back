package com.yf.interceptor;


import cn.hutool.core.bean.BeanUtil;

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


import java.time.Duration;
import java.util.Map;

//在拦截器中设置用户信息
@Slf4j
@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private HutoolJwtUtil hutoolJwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 获取token
        String token = request.getHeader("authorization");
        log.info("token: {}", token);
        if (token == null || token.trim().isEmpty()) {
            return true;
        }

        // 2. 验证JWT是否有效（包括检查Redis黑名单）
        if (!hutoolJwtUtil.validateToken(token)) {
            log.warn("JWT令牌无效或已过期或在黑名单中: {}", token);
            return true;
        }

        // 3. 解析JWT获取用户ID
        cn.hutool.jwt.JWT jwt = cn.hutool.jwt.JWTUtil.parseToken(token);
        String userId = hutoolJwtUtil.getUserId(token);
        
        // 4. 基于用户ID获取redis中的会话信息
        String key = RedisConstants.LOGIN_USER_KEY + userId;
        Map<Object, Object> userMap = redisTemplate.opsForHash().entries(key);

        // 5. 判断用户会话是否存在
        if (userMap.isEmpty()) {
            return true;
        }

        // 6. 从Redis获取存储的token是否与当前token一致（可选验证）
        String storedToken = (String) userMap.get("token");
        if (storedToken == null || !storedToken.equals(token)) {
            log.warn("Token不匹配，可能已被其他设备登录");
            return true;
        }

        // 7. 从JWT解析用户信息创建UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername(jwt.getPayloads().getStr("username"));

        // 8. 保存用户信息到ThreadLocal
        UserHolder.saveUser(userDTO);

        // 9. 刷新token在Redis中的有效期
        redisTemplate.expire(key, Duration.ofMinutes(RedisConstants.LOGIN_USER_TTL));

        return true;
    }

    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理ThreadLocal，防止内存泄漏
        UserHolder.removeUser();
    }
}