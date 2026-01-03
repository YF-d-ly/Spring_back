package com.yf.controller;


import com.yf.entity.dto.Login.LoginDTO;
import com.yf.entity.dto.Login.RegisterFormDTO;
import com.yf.entity.vo.Login.UserPermissionVO;
import com.yf.service.UserService;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "登录管理", description = "用户登录相关接口")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 发送邮箱验证码
     */
    @PostMapping("/code")
    public Result sendCode(@RequestParam @Email String email) {
        userService.sendCode(email);
        return Result.success("验证码发送成功");
    }

    /**
     * 邮箱验证码注册
     */
    @PostMapping("/register")
    @Operation(summary = "邮箱验证码注册")
    public Result<UserPermissionVO> register(@RequestBody RegisterFormDTO registerFormDTO) {
        log.info("用户注册：{}", registerFormDTO);
        UserPermissionVO userPermissionVO = userService.registerByEmail(registerFormDTO);
        return Result.success(userPermissionVO, "用户注册成功");
    }
    
    /**
     * 登录
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserPermissionVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO);
        UserPermissionVO userPermissionVO = userService.login(loginDTO);
        return Result.success(userPermissionVO, "用户登录成功");
    }
    
    /**
     * 邮箱验证码登录
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login/email")
    @Operation(summary = "邮箱验证码登录")
    public Result<UserPermissionVO> loginByEmail(@RequestBody LoginDTO loginDTO) {
        log.info("用户邮箱验证码登录：{}", loginDTO);
        UserPermissionVO userPermissionVO = userService.loginByEmailCode(loginDTO);
        return Result.success(userPermissionVO, "用户登录成功");
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        log.info("用户退出");
        
        // 去除 "Bearer " 前缀（如果存在）
        String actualToken = token;
        if (token != null && token.startsWith("Bearer ")) {
            actualToken = token.substring(7);
        }
        
        userService.logout(actualToken);
        return Result.success("用户退出成功");
    }
}