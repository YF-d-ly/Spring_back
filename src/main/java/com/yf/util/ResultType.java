package com.yf.util;

public enum ResultType {
    SUCCESS(200, "操作成功"),
    ERROR(400, "操作错误"),
    FAIL(400, "操作失败"),
    UNAUTHORIZED(401, "未授权"),
    NOT_FOUND(404, "未找到"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    AGAIN_LOGIN(600, "重新登录"), NOT_LOGIN( 600, "重新登录");


    private String code;
    private String name;

    ResultType(int code, String name) {
        this.code = String.valueOf(code);
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

}
