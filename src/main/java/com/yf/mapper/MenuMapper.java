package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户ID获取菜单列表（通过角色）
     */
    @Select("SELECT m.* FROM xmut_menu m " +
            "LEFT JOIN xmut_role_menu rm ON m.id = rm.menu_id " +
            "LEFT JOIN xmut_user u ON u.role_id = rm.role_id " +
            "WHERE u.id = #{userId}")
    List<Menu> getMenusByUserId(@Param("userId") String userId);
    
    /**
     * 根据角色ID获取菜单列表
     */
    @Select("SELECT m.* FROM xmut_menu m " +
            "LEFT JOIN xmut_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId}")
    List<Menu> getMenusByRoleId(@Param("roleId") String roleId);
}