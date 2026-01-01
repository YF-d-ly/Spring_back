package com.yf.controller;

import com.yf.entity.Menu;
import com.yf.entity.Role;
import com.yf.entity.User;
import com.yf.entity.Warehouse;
import com.yf.service.PermissionService;
import com.yf.service.RoleService;
import com.yf.service.UserService;
import com.yf.util.Result;
import com.yf.util.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@Tag(name = "权限管理", description = "权限分配相关接口")
@Slf4j
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;

    /**
     * 分配角色菜单权限
     */
    @PostMapping("/assign-menu")
    @Operation(summary = "分配角色菜单权限")
    public Result<Boolean> assignMenuPermissions(@RequestParam String roleId, 
                                                @RequestParam List<String> menuIds) {
        // 检查当前用户是否为超级管理员
        if (UserHolder.getUser().getRoleId() == null || !UserHolder.getUser().getRoleId().equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = permissionService.assignMenuPermissionsToRole(roleId, menuIds);
        return Result.success(result, result ? "分配菜单权限成功" : "分配菜单权限失败");
    }

    /**
     * 分配用户仓库权限
     */
    @PostMapping("/assign-warehouse")
    @Operation(summary = "分配用户仓库权限")
    public Result<Boolean> assignWarehousePermissions(@RequestParam String userId, 
                                                     @RequestParam List<String> warehouseIds) {
        // 检查当前用户是否为超级管理员
        if (UserHolder.getUser().getRoleId() == null || !UserHolder.getUser().getRoleId().equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = permissionService.assignWarehousePermissions(userId, warehouseIds);
        return Result.success(result, result ? "分配仓库权限成功" : "分配仓库权限失败");
    }

    /**
     * 获取用户拥有的菜单权限
     */
    @GetMapping("/user-menus/{userId}")
    @Operation(summary = "获取用户菜单权限")
    public Result<List<Menu>> getUserMenuPermissions(@PathVariable String userId) {
        // 检查当前用户是否为超级管理员或访问自己的权限
        String currentUserRoleId = UserHolder.getUser().getRoleId();
        String currentUserId = UserHolder.getUser().getId();
        
        if ((currentUserRoleId == null || !currentUserRoleId.equals("ROLE_001")) && 
            !userId.equals(currentUserId)) {
            return Result.error("权限不足，只能查看自己的权限或超级管理员可查看所有用户权限");
        }
        
        List<Menu> menus = permissionService.getUserMenus(userId);
        return Result.success(menus, "获取用户菜单权限成功");
    }

    /**
     * 获取用户拥有的仓库权限
     */
    @GetMapping("/user-warehouses/{userId}")
    @Operation(summary = "获取用户仓库权限")
    public Result<List<Warehouse>> getUserWarehousePermissions(@PathVariable String userId) {
        // 检查当前用户是否为超级管理员或访问自己的权限
        String currentUserRoleId = UserHolder.getUser().getRoleId();
        String currentUserId = UserHolder.getUser().getId();
        
        if ((currentUserRoleId == null || !currentUserRoleId.equals("ROLE_001")) && 
            !userId.equals(currentUserId)) {
            return Result.error("权限不足，只能查看自己的权限或超级管理员可查看所有用户权限");
        }
        
        List<Warehouse> warehouses = permissionService.getUserWarehouses(userId);
        return Result.success(warehouses, "获取用户仓库权限成功");
    }

    /**
     * 获取当前登录用户的权限（菜单和仓库）
     */
    @GetMapping("/my-permissions")
    @Operation(summary = "获取当前用户权限")
    public Result<Object> getCurrentUserPermissions() {
        String userId = UserHolder.getUser().getId();
        String userRoleId = UserHolder.getUser().getRoleId();
        
        // 检查是否为超级管理员
        boolean isSuperAdmin = userRoleId != null && userRoleId.equals("ROLE_001");
        
        if (isSuperAdmin) {
            // 超级管理员拥有所有权限
            List<Menu> allMenus = permissionService.getAllMenus();
            List<Warehouse> allWarehouses = permissionService.getAllWarehouses();
            
            // 构建权限对象
            var permissions = new Object() {
                public List<Menu> menus = allMenus;
                public List<Warehouse> warehouses = allWarehouses;
                public Boolean isSuperAdmin = true;
            };
            
            return Result.success(permissions, "获取当前用户权限成功");
        } else {
            // 普通用户只拥有分配的权限
            List<Menu> userMenus = permissionService.getUserMenus(userId);
            List<Warehouse> userWarehouses = permissionService.getUserWarehouses(userId);
            
            // 构建权限对象
            var permissions = new Object() {
                public List<Menu> menus = userMenus;
                public List<Warehouse> warehouses = userWarehouses;
                public Boolean isSuperAdmin = false;
            };
            
            return Result.success(permissions, "获取当前用户权限成功");
        }
    }

    /**
     * 获取角色拥有的菜单权限
     */
    @GetMapping("/role-menus/{roleId}")
    @Operation(summary = "获取角色菜单权限")
    public Result<List<Menu>> getRoleMenuPermissions(@PathVariable String roleId) {
        // 检查当前用户是否为超级管理员
        if (UserHolder.getUser().getRoleId() == null || !UserHolder.getUser().getRoleId().equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        List<Menu> menus = permissionService.getRoleMenus(roleId);
        return Result.success(menus, "获取角色菜单权限成功");
    }

    /**
     * 获取所有菜单
     */
    @GetMapping("/all-menus")
    @Operation(summary = "获取所有菜单")
    public Result<List<Menu>> getAllMenus() {
        // 检查当前用户是否为超级管理员
        if (UserHolder.getUser().getRoleId() == null || !UserHolder.getUser().getRoleId().equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        List<Menu> menus = permissionService.getAllMenus();
        return Result.success(menus, "获取所有菜单成功");
    }

    /**
     * 获取所有仓库
     */
    @GetMapping("/all-warehouses")
    @Operation(summary = "获取所有仓库")
    public Result<List<Warehouse>> getAllWarehouses() {
        // 检查当前用户是否为超级管理员
        if (UserHolder.getUser().getRoleId() == null || !UserHolder.getUser().getRoleId().equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        List<Warehouse> warehouses = permissionService.getAllWarehouses();
        return Result.success(warehouses, "获取所有仓库成功");
    }

    /**
     * 获取用户列表（用于权限分配界面）
     */
    @GetMapping("/users")
    @Operation(summary = "获取用户列表")
    public Result<List<User>> getUserList() {
        // 检查当前用户是否为超级管理员
        if (UserHolder.getUser().getRoleId() == null || !UserHolder.getUser().getRoleId().equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        // 使用UserService接口中定义的getUserList方法获取所有用户
        // 通过传递空参数获取所有用户
        var userListPage = userService.getUserList(null, null, null, 1, Integer.MAX_VALUE);
        List<User> users = userListPage.getRecords();
        return Result.success(users, "获取用户列表成功");
    }
    
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