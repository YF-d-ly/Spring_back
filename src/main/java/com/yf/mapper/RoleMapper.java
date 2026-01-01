package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID获取角色列表
     */
    @Select("SELECT r.* FROM xmut_role r " +
            "LEFT JOIN xmut_user u ON r.id = u.role_id " +
            "WHERE u.id = #{userId}")
    List<Role> getRolesByUserId(@Param("userId") String userId);
}