package com.yf.controller;

import com.yf.entity.StockLog;
import com.yf.service.StockLogService;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@Tag(name = "库存管理", description = "库存管理相关接口")
public class StockController {

    @Autowired
    private StockLogService stockLogService;

    // 获取出入库记录列表
    @GetMapping("/log/list")
    public Result<List<StockLog>> getStockLogList(
            @RequestParam(value = "goodsId", required = false) String goodsId,
            @RequestParam(value = "warehouseId", required = false) String warehouseId,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        // 实现获取出入库记录列表逻辑
        List<StockLog> stockLogList = stockLogService.list(); // 临时实现
        return Result.success(stockLogList, "获取出入库记录列表成功");
    }

    // 添加入库记录
    @PostMapping("/inbound")
    public Result<String> addInbound(@RequestBody Object data) {
        // 实现添加入库记录逻辑
        return Result.success("添加入库记录成功");
    }

    // 添加出库记录
    @PostMapping("/outbound")
    public Result<String> addOutbound(@RequestBody Object data) {
        // 实现出库记录逻辑
        return Result.success("添加出库记录成功");
    }

    // 调货
    @PostMapping("/transfer")
    public Result<String> transferGoods(@RequestBody Object data) {
        // 实现调货逻辑
        return Result.success("调货成功");
    }
}