package com.yf.mapper;

import com.yf.entity.StockLog;
import com.yf.entity.vo.GoodsRankVO;
import com.yf.entity.vo.TransferVO;
import com.yf.entity.vo.WarehouseDailyTrendVO;
import com.yf.entity.vo.WarehouseGoodsInventoryVO;
import com.yf.entity.vo.WarehouseGoodsRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StockLogCustomMapper {
    
    /**
     * 查询调拨日志，返回TransferVO列表
     * @return 调拨记录列表
     */
    List<TransferVO> selectTransferLogs();
    
    /**
     * 查询货物进出排行前10
     * @return 货物进出排行列表
     */
    List<GoodsRankVO> selectTop10GoodsByInOut();
    
    /**
     * 查询货物进出排行前10（带日期范围）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 货物进出排行列表
     */
    List<GoodsRankVO> selectTop10GoodsByInOutWithDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    /**
     * 查询仓库进出货物排行前10
     * @return 仓库进出货物排行列表
     */
    List<WarehouseGoodsRankVO> selectTop10WarehouseByInOut();
    
    /**
     * 查询仓库进出货物排行前10（带日期范围）
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 仓库进出货物排行列表
     */
    List<WarehouseGoodsRankVO> selectTop10WarehouseByInOutWithDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    /**
     * 查询指定仓库内的货物排行
     * @param warehouseId 仓库ID
     * @return 仓库内货物排行列表
     */
    List<WarehouseGoodsInventoryVO> selectWarehouseGoodsInventory(@Param("warehouseId") String warehouseId);
    
    /**
     * 查询指定仓库内的货物排行（带日期范围）
     * @param warehouseId 仓库ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 仓库内货物排行列表
     */
    List<WarehouseGoodsInventoryVO> selectWarehouseGoodsInventoryWithDate(@Param("warehouseId") String warehouseId, 
                                                                         @Param("startDate") String startDate, 
                                                                         @Param("endDate") String endDate);
    
    /**
     * 查询仓库每日进出趋势
     * @param warehouseId 仓库ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 仓库每日进出趋势列表
     */
    List<WarehouseDailyTrendVO> selectWarehouseDailyTrend(@Param("warehouseId") String warehouseId, 
                                                          @Param("startDate") String startDate, 
                                                          @Param("endDate") String endDate);
}