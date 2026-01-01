package com.yf.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    SUPER_ADMIN(1, "超级管理员"),
    INFO_ADMIN(2, "信息管理员");

    private final Integer code;
    private final String description;

    UserRoleEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserRoleEnum fromCode(Integer code) {
        for (UserRoleEnum role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return null;
    }
}