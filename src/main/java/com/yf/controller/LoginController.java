package com.yf.controller;

import com.yf.dto.LoginDTO;
import com.yf.service.UserService;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Tag(name = "登录管理", description = "用户登录相关接口")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO);
        String token = userService.login(loginDTO);
        return Result.success(token, "用户登录成功");
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        log.info("用户退出");
        userService.logout(token);
        return Result.success("用户退出成功");
    }
}