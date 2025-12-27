package com.yf.controller;

import com.yf.entity.User;
import com.yf.service.UserService;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    // 获取用户列表
    @GetMapping("/list")
    public Result<List<User>> getUserList(
            @RequestParam(value = "account", required = false) String account,
            @RequestParam(value = "nickname", required = false) String nickname,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        // 实现获取用户列表逻辑
        List<User> userList = userService.list(); // 临时实现
        return Result.success(userList, "获取用户列表成功");
    }

    // 添加用户
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody User user) {
        boolean result = userService.save(user);
        if (result) {
            return Result.success("添加用户成功");
        } else {
            return Result.error("添加用户失败");
        }
    }

    // 更新用户
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        boolean result = userService.updateById(user);
        if (result) {
            return Result.success("更新用户成功");
        } else {
            return Result.error("更新用户失败");
        }
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable String id) {
        boolean result = userService.removeById(id);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    // 重置密码
    @PostMapping("/reset-password/{id}")
    public Result<String> resetPassword(@PathVariable String id) {
        // 实现重置密码逻辑
        return Result.success("重置密码成功");
    }

    // 修改密码
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestBody Object data) {
        // 实现修改密码逻辑
        return Result.success("修改密码成功");
        }

    // 更新个人信息
    @PutMapping("/profile")
    public Result<String> updateProfile(@RequestBody User user) {
        boolean result = userService.updateById(user);
        if (result) {
            return Result.success("更新个人信息成功");
        } else {
            return Result.error("更新个人信息失败");
        }
    }

    // 获取个人信息
    @GetMapping("/profile")
    public Result<User> getProfile() {
        // 实现获取个人信息逻辑
        return Result.success(null, "获取个人信息成功");
    }
}