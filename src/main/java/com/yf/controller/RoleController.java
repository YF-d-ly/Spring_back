package com.yf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.entity.Role;
import com.yf.service.RoleService;
import com.yf.util.Result;
import com.yf.util.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取角色列表（用于权限分配界面）
     */
    @GetMapping("/roles")
    @Operation(summary = "获取角色列表")
    public Result<List<Role>> getRoleList() {
        // 检查当前用户是否为超级管理员
        if (UserHolder.getUser().getRoleId() == null || !UserHolder.getUser().getRoleId().equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }

        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles, "获取角色列表成功");
    }

}