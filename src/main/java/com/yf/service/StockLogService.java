package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.entity.StockLog;
import com.yf.entity.dto.InboundDTO;
import com.yf.entity.dto.OutboundDTO;
import com.yf.entity.dto.page.StockLogQueryDTO;
import com.yf.entity.dto.TransferDTO;
import com.yf.entity.vo.GoodsRankVO;
import com.yf.entity.vo.PageResult.StockLogVO;
import com.yf.entity.vo.PageResult.TransferVO;
import com.yf.entity.vo.WarehouseDailyTrendVO;
import com.yf.entity.vo.WarehouseGoodsRankVO;
import com.yf.entity.vo.WarehouseGoodsInventoryVO;
import com.yf.util.PageResult;
import java.util.List;

public interface StockLogService extends IService<StockLog> {
     void addInbound(InboundDTO inboundDTO);
    
    void addOutbound(OutboundDTO outboundDTO);
    
    void transferGoods(TransferDTO transferDTO);
    
    PageResult<StockLogVO> pageQuery(StockLogQueryDTO queryDTO);

    List<TransferVO> getTransferLog();

    List<GoodsRankVO> getTop10GoodsByInOut();

    // 获取仓库进出货物排行
    List<WarehouseGoodsRankVO> getTop10WarehouseByInOut();
    
    // 获取指定仓库内货物排行
    List<WarehouseGoodsInventoryVO> getWarehouseGoodsInventory(String warehouseId);
    
    // 获取指定仓库内货物排行（带日期范围）
    List<WarehouseGoodsInventoryVO> getWarehouseGoodsInventory(String warehouseId, String startDate, String endDate);
    
    // 获取货物进出排行（带日期范围）
    List<GoodsRankVO> getTop10GoodsByInOut(String startDate, String endDate);
    
    // 获取仓库进出货物排行（带日期范围）
    List<WarehouseGoodsRankVO> getTop10WarehouseByInOut(String startDate, String endDate);

    // 获取仓库每日进出趋势
    List<WarehouseDailyTrendVO> getWarehouseDailyTrend(String warehouseId, String startDate, String endDate);
}