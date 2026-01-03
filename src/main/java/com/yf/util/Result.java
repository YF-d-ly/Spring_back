package com.yf.util;

import lombok.Data;

@Data
public class Result<T> {
    private int code; // 状态码
    private String message; // 消息
    private T data; // 数据

    // 无参构造
    public Result() {
    }

    // 带参构造
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功返回
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(200, message, data);
    }

    // 失败返回
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    // 自定义返回
    public static <T> Result<T> build(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    public static Result<String> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    public static Result fail(String message) {
        return new Result<>(500, message, null);
    }
}