package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.entity.Warehouse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {
    /**
     * 根据用户ID获取仓库列表
     */
    @Select("SELECT w.* FROM xmut_warehouse w " +
            "LEFT JOIN xmut_user_warehouse uw ON w.id = uw.warehouse_id " +
            "WHERE uw.userId = #{userId}")
    List<Warehouse> getWarehousesByUserId(@Param("userId") String userId);
}