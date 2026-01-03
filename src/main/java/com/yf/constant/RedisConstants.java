package com.yf.constant;

public class RedisConstants {
    // 登录用户
    public static final String LOGIN_USER_KEY = "login:user:";
    public static final Long LOGIN_USER_TTL = 30L;

    // 登录验证码
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
}