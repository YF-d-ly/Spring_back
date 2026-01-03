package com.yf.util;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static boolean isEmailInvalid(String email) {
        return mismatch(email, EMAIL_REGEX);
    }
    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex) {
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }


}