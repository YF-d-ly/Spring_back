package com.yf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yf.entity.Menu;
import com.yf.entity.RoleMenu;
import com.yf.entity.UserMenu;
import com.yf.entity.UserWarehouse;
import com.yf.entity.Warehouse;
import com.yf.mapper.MenuMapper;
import com.yf.mapper.RoleMenuMapper;
import com.yf.mapper.UserMenuMapper;
import com.yf.mapper.UserWarehouseMapper;
import com.yf.mapper.WarehouseMapper;
import com.yf.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private UserMenuMapper userMenuMapper;
    
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    
    @Autowired
    private UserWarehouseMapper userWarehouseMapper;
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Autowired
    private WarehouseMapper warehouseMapper;

    @Override
    @Transactional
    public boolean assignMenuPermissionsToRole(String roleId, List<String> menuIds) {
        try {
            // 先删除角色现有的菜单权限
            roleMenuMapper.delete(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
            
            // 为角色分配新的菜单权限
            if (menuIds != null && !menuIds.isEmpty()) {
                for (String menuId : menuIds) {
                    RoleMenu roleMenu = RoleMenu.builder()
                            .roleId(roleId)
                            .menuId(menuId)
                            .build();
                    roleMenuMapper.insert(roleMenu);
                }
            }
            
            log.info("为角色 {} 分配菜单权限成功", roleId);
            return true;
        } catch (Exception e) {
            log.error("分配菜单权限失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public boolean assignWarehousePermissions(String userId, List<String> warehouseIds) {
        try {
            // 先删除用户现有的仓库权限
            userWarehouseMapper.delete(new LambdaQueryWrapper<UserWarehouse>().eq(UserWarehouse::getUserId, userId));
            
            // 为用户分配新的仓库权限
            if (warehouseIds != null && !warehouseIds.isEmpty()) {
                for (String warehouseId : warehouseIds) {
                    UserWarehouse userWarehouse = UserWarehouse.builder()
                            .userId(userId)
                            .warehouseId(warehouseId)
                            .build();
                    userWarehouseMapper.insert(userWarehouse);
                }
            }
            
            log.info("为用户 {} 分配仓库权限成功", userId);
            return true;
        } catch (Exception e) {
            log.error("分配仓库权限失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public List<Menu> getUserMenus(String userId) {
        // 根据用户角色获取菜单权限
        return menuMapper.getMenusByUserId(userId);
    }

    @Override
    public List<Warehouse> getUserWarehouses(String userId) {

        return List.of();
    }

    @Override
    public List<Menu> getRoleMenus(String roleId) {
        return menuMapper.getMenusByRoleId(roleId);
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.selectList(null);
    }

    @Override
    public List<Warehouse> getAllWarehouses() {
        return warehouseMapper.selectList(null);
    }

    @Override
    public boolean assignRoleToUser(String userId, String roleId) {
        return false;
    }

    @Override
    public boolean removeRoleFromUser(String userId, String roleId) {
        return false;
    }

    @Override
    public List<String> getUserRoles(String userId) {
        return List.of();
    }


}