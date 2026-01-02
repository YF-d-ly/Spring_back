package com.yf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.entity.Category;
import com.yf.entity.User;
import com.yf.entity.dto.page.UserQueryDTO;
import com.yf.entity.vo.select.CategoryVO;
import com.yf.entity.vo.select.UserNamesVO;
import com.yf.service.UserService;
import com.yf.util.PageResult;
import com.yf.util.Result;
import com.yf.util.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户列表（仅超级管理员可访问）
     */
    @GetMapping("/list")
    @Operation(summary = "用户列表")
    public Result<IPage<User>> getUserList(
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String roleId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        // 检查当前用户是否为超级管理员
        if (!"ROLE_001".equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        IPage<User> userList = userService.getUserList(account, username, roleId, page, size);
        return Result.success(userList, "获取用户列表成功");
    }

    /**
     * 获取用户姓名（用于权限分配界面）
     */
    @GetMapping("/names")
    @Operation(summary = "获取用户列表")
    public Result<List<UserNamesVO>> getUserName() {
        List<User> userNames = userService.list();
        log.info("用户列表：{}", userNames);
        List<UserNamesVO> userNamesVOList = userNames.stream()
                .map(user -> UserNamesVO.builder()
                        .id(user.getId())
                        .nickname(user.getNickname())
                        .build())
                .collect(Collectors.toList());

        return Result.success(userNamesVOList, "获取用户列表成功");
    }

    /**
     * 获取用户分页
     */
    @GetMapping("/Page" )
    public Result<PageResult<User>> PageQury( ) {
        PageResult<User> pageResult = userService.query(new UserQueryDTO());
        return null;
//        return Result.success(pageResult, "获取用户分页成功");
        
    }

    /**
     * 添加用户（仅超级管理员可访问）
     */
    @PostMapping("/add")
    @Operation(summary = "添加用户")
    public Result<Boolean> addUser(@RequestBody User user) {
        // 检查当前用户是否为超级管理员
        if (!"ROLE_001".equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = userService.addUser(user);
        return Result.success(result, result ? "添加用户成功" : "添加用户失败");
    }

    /**
     * 更新用户（仅超级管理员可访问）
     */
    @PutMapping("/update")
    @Operation(summary = "更新用户")
    public Result<Boolean> updateUser(@RequestBody User user) {
        // 检查当前用户是否为超级管理员
        if (!"ROLE_001".equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = userService.updateUser(user);
        return Result.success(result, result ? "更新用户成功" : "更新用户失败");
    }

    /**
     * 删除用户（仅超级管理员可访问）
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户")
    public Result<Boolean> deleteUser(@PathVariable String id) {
        // 检查当前用户是否为超级管理员
        if (!"ROLE_001".equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = userService.deleteUser(id);
        return Result.success(result, result ? "删除用户成功" : "删除用户失败");
    }

    /**
     * 重置密码（仅超级管理员可访问）
     */
    @PutMapping("/reset-password/{id}")
    @Operation(summary = "重置密码")
    public Result<Boolean> resetPassword(@PathVariable String id) {
        // 检查当前用户是否为超级管理员
        if (!"ROLE_001".equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = userService.resetPassword(id);
        return Result.success(result, result ? "重置密码成功" : "重置密码失败");
    }

    /**
     * 修改用户状态（启用/禁用）
     */
    @PutMapping("/status/{id}")
    @Operation(summary = "修改用户状态")
    public Result<Boolean> updateStatus(@PathVariable String id, @RequestParam Integer status) {
        // 检查当前用户是否为超级管理员
        if (!"ROLE_001".equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = userService.updateStatus(id, status);
        return Result.success(result, result ? "修改用户状态成功" : "修改用户状态失败");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息")
    public Result<User> getUserById(@PathVariable String id) {
        // 检查当前用户是否为超级管理员或访问自己的信息
        if (!"ROLE_001".equals(UserHolder.getUser().getRoleId()) && 
            !id.equals(UserHolder.getUser().getId())) {
            return Result.error("权限不足，只能查看自己的信息或超级管理员可查看所有用户");
        }
        
        User user = userService.getUserById(id);
        return Result.success(user, "获取用户信息成功");
    }

    /**
     * 修改个人信息
     */
    @PutMapping("/profile")
    @Operation(summary = "修改个人信息")
    public Result<Boolean> updateProfile(@RequestBody User user) {
        // 只能修改自己的信息
        if (!user.getId().equals(UserHolder.getUser().getId())) {
            return Result.error("权限不足，只能修改自己的信息");
        }
        
        // 不能修改角色和状态
        User existingUser = userService.getUserById(user.getId());
        user.setRoleId(existingUser.getRoleId());  // 修正：使用setRoleId而不是setRole
        user.setStatus(existingUser.getStatus());
        
        boolean result = userService.updateUser(user);
        return Result.success(result, result ? "修改个人信息成功" : "修改个人信息失败");
    }

    /**
     * 修改密码
     */
    @PutMapping("/change-password")
    @Operation(summary = "修改密码")
    public Result<Boolean> changePassword(@RequestParam String oldPassword, 
                                          @RequestParam String newPassword) {
        String userId = UserHolder.getUser().getId();
        boolean result = userService.changePassword(userId, oldPassword, newPassword);
        return Result.success(result, result ? "修改密码成功" : "修改密码失败");
    }
}