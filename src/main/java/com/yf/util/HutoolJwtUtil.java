package com.yf.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HutoolJwtUtil {

    @Value("${jwt.secret:default-secret-key-change-me}")
    private String secretKey;

    @Value("${jwt.expiration:86400}")
    private Integer expiration; // 过期时间，秒

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    /**
     * 生成JWT令牌（最简方式）
     */
    public String generateToken(String userId, String username) {
        Map<String, Object> payload = new HashMap<>();

        // 只放必要的信息
        payload.put("userId", userId);
        payload.put("username", username);
        payload.put("exp", DateTime.now().offset(DateField.SECOND, expiration).getTime() / 1000); // 过期时间戳

        // 一行代码生成JWT
        return JWTUtil.createToken(payload, secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 验证JWT令牌
     */
    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        // 检查JWT是否在黑名单中
        if (isTokenBlacklisted(token)) {
            return false;
        }

        try {
            // 验证签名和过期时间
            boolean verify = JWTUtil.verify(token, secretKey.getBytes(StandardCharsets.UTF_8));
            if (!verify) return false;

            // 验证是否过期
            JWT jwt = JWTUtil.parseToken(token);
            JWTValidator.of(jwt).validateDate(DateTime.now());

            return true;
        } catch (Exception e) {
            log.warn("JWT验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 将JWT加入黑名单
     */
    public void blacklistToken(String token, long expirationTime) {
        try {
            // 将token加入黑名单，设置过期时间为JWT的剩余过期时间
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "1", expirationTime, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn("添加JWT到黑名单失败: {}", e.getMessage());
        }
    }

    /**
     * 检查JWT是否在黑名单中
     */
    private boolean isTokenBlacklisted(String token) {
        try {
            String result = redisTemplate.opsForValue().get(BLACKLIST_PREFIX + token);
            return "1".equals(result);
        } catch (Exception e) {
            log.warn("检查JWT黑名单失败: {}", e.getMessage());
            return false; // 如果无法检查黑名单，默认为不在黑名单中
        }
    }

    /**
     * 解析JWT中的用户ID
     */
    public String getUserId(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Object userIdObj = jwt.getPayloads().get("userId");
            if (userIdObj instanceof String) {
                return (String) userIdObj;
            } else if (userIdObj instanceof Integer) {
                return String.valueOf((Integer) userIdObj);
            } else if (userIdObj instanceof Long) {
                return String.valueOf((Long) userIdObj);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析JWT中的用户名
     */
    public String getUsername(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            return jwt.getPayloads().getStr("username");
        } catch (Exception e) {
            return null;
        }
    }
}