package com.yf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yf.entity.Warehouse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseMapper extends BaseMapper<Warehouse> {
    /**
     * 根据用户ID获取仓库列表
     */
    @Select("SELECT w.* FROM xmut_warehouse w " +
            "INNER JOIN xmut_user_warehouse uw ON w.id = uw.warehouse_id " +
            "WHERE uw.user_id = #{userId}")
    List<Warehouse> getWarehousesByUserId(@Param("userId") String userId);
    
    /**
     * 根据用户ID和查询条件获取仓库列表（带分页）
     */
    @Select("<script>" +
            "SELECT w.* FROM xmut_warehouse w " +
            "INNER JOIN xmut_user_warehouse uw ON w.id = uw.warehouse_id " +
            "WHERE uw.user_id = #{userId} " +
            "<if test='name != null and name != \"\"'> AND w.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='address != null and address != \"\"'> AND w.address LIKE CONCAT('%', #{address}, '%')</if> " +
            "<if test='contact != null and contact != \"\"'> AND w.contact LIKE CONCAT('%', #{contact}, '%')</if>" +
            "</script>")
    IPage<Warehouse> getUserWarehousesWithFiltersAndPaging(IPage<Warehouse> page, @Param("userId") String userId, 
                                                         @Param("name") String name,
                                                         @Param("address") String address,
                                                         @Param("contact") String contact);
    
    /**
     * 获取用户有权限访问的仓库总数（用于分页计算）
     */
    @Select("<script>" +
            "SELECT COUNT(1) FROM xmut_warehouse w " +
            "INNER JOIN xmut_user_warehouse uw ON w.id = uw.warehouse_id " +
            "WHERE uw.user_id = #{userId} " +
            "<if test='name != null and name != \"\"'> AND w.name LIKE CONCAT('%', #{name}, '%')</if> " +
            "<if test='address != null and address != \"\"'> AND w.address LIKE CONCAT('%', #{address}, '%')</if> " +
            "<if test='contact != null and contact != \"\"'> AND w.contact LIKE CONCAT('%', #{contact}, '%')</if>" +
            "</script>")
    int getUserWarehousesCount(@Param("userId") String userId, 
                               @Param("name") String name,
                               @Param("address") String address,
                               @Param("contact") String contact);
    
    /**
     * 检查用户是否有指定仓库的访问权限
     */
    @Select("SELECT COUNT(1) FROM xmut_user_warehouse " +
            "WHERE user_id = #{userId} AND warehouse_id = #{warehouseId}")
    int checkUserWarehousePermission(@Param("userId") String userId, @Param("warehouseId") String warehouseId);
    
    /**
     * 获取用户仓库关联信息
     */
    @Select("SELECT * FROM xmut_user_warehouse " +
            "WHERE user_id = #{userId}")
    List<Map<String, Object>> getUserWarehouseRelations(@Param("userId") String userId);


}