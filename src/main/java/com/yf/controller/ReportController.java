package com.yf.controller;

import com.yf.entity.vo.GoodsRankVO;
import com.yf.entity.vo.WarehouseDailyTrendVO;
import com.yf.entity.vo.WarehouseGoodsInventoryVO;
import com.yf.entity.vo.WarehouseGoodsRankVO;
import com.yf.service.StockLogService;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@Tag(name = "报表管理", description = "统计报表相关接口")
public class ReportController {

    @Autowired
    private StockLogService stockLogService;


    @GetMapping("/goods-top10")
    public Result<List<GoodsRankVO>> getGoodsReport(@RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate) {
        List<GoodsRankVO> top10;
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            top10 = stockLogService.getTop10GoodsByInOut(startDate, endDate);
        } else {
            top10 = stockLogService.getTop10GoodsByInOut();
        }
        return Result.success(top10, "获取货品排行成功");
    }
    
    // 获取仓库进出货物排行
    @GetMapping("/warehouse-top10")
    public Result<List<WarehouseGoodsRankVO>> getWarehouseReport(@RequestParam(required = false) String startDate,
                                                                @RequestParam(required = false) String endDate) {
        List<WarehouseGoodsRankVO> top10;
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            top10 = stockLogService.getTop10WarehouseByInOut(startDate, endDate);
        } else {
            top10 = stockLogService.getTop10WarehouseByInOut();
        }
        return Result.success(top10, "获取仓库进出货物排行成功");
    }

    // 获取指定仓库内货物排行
    @GetMapping("/warehouse/{warehouseId}/goods-top10")
    public Result<List<WarehouseGoodsInventoryVO>> getWarehouseGoodsInventory(@PathVariable String warehouseId,
                                                                             @RequestParam(required = false) String startDate,
                                                                             @RequestParam(required = false) String endDate) {
        List<WarehouseGoodsInventoryVO> inventory = stockLogService.getWarehouseGoodsInventory(warehouseId, startDate, endDate);
        return Result.success(inventory, "获取仓库内货物排行成功");
    }
    
    // 获取仓库每日进出趋势
    @GetMapping("/warehouse/daily-trend")
    public Result<List<WarehouseDailyTrendVO>> getWarehouseDailyTrend(@RequestParam String warehouseId, 
                                                                      @RequestParam(required = false) String startDate, 
                                                                      @RequestParam(required = false) String endDate) {
        List<WarehouseDailyTrendVO> trends = stockLogService.getWarehouseDailyTrend(warehouseId, startDate, endDate);
        return Result.success(trends, "获取仓库每日进出趋势成功");
    }
}