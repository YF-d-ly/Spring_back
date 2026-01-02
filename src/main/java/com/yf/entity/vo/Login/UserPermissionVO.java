package com.yf.entity.vo.Login;

import com.yf.entity.Menu;
import com.yf.entity.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户权限信息VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionVO {
    private String token;               // 认证令牌
    private String userId;              // 用户ID
    private String username;            // 用户名
    private String roleId;
    private List<Menu> menus;           // 用户可访问菜单列表
    private List<Warehouse> warehouses; // 用户可访问仓库列表
}