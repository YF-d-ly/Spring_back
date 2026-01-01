package com.yf.service;

import com.yf.entity.Menu;
import com.yf.entity.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface PermissionService {
    /**
     * 分配角色菜单权限
     */
    boolean assignMenuPermissionsToRole(String roleId, List<String> menuIds);

    /**
     * 分配用户仓库权限
     */
    boolean assignWarehousePermissions(String userId, List<String> warehouseIds);

    /**
     * 获取用户拥有的菜单权限
     */
    List<Menu> getUserMenus(String userId);

    /**
     * 获取用户拥有的仓库权限
     */
    List<Warehouse> getUserWarehouses(String userId);

    /**
     * 获取角色拥有的菜单权限
     */
    List<Menu> getRoleMenus(String roleId);

    /**
     * 获取所有菜单
     */
    List<Menu> getAllMenus();

    /**
     * 获取所有仓库
     */
    List<Warehouse> getAllWarehouses();

    /**
     * 为用户分配角色
     */
    boolean assignRoleToUser(String userId, String roleId);

    /**
     * 移除用户的角色
     */
    boolean removeRoleFromUser(String userId, String roleId);

    /**
     * 获取用户拥有的角色
     */
    List<String> getUserRoles(String userId);
}