package com.yf.controller;

import com.yf.entity.StockLog;
import com.yf.entity.dto.InboundDTO;
import com.yf.entity.dto.OutboundDTO;
import com.yf.entity.dto.StockLogQueryDTO;
import com.yf.entity.dto.TransferDTO;
import com.yf.entity.vo.GoodVO;
import com.yf.entity.vo.StockLogVO;
import com.yf.service.StockLogService;
import com.yf.util.PageResult;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.Operation;
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

    
    // 分页获取出入库记录
    @GetMapping("/log/page")
    @Operation(summary = "分页获取出入库记录", description = "根据条件分页获取出入库记录")
    public Result<PageResult<StockLogVO>> pageQuery(StockLogQueryDTO queryDTO) {
        PageResult<StockLogVO> pageResult = stockLogService.pageQuery(queryDTO);
        return Result.success(pageResult, "分页获取出入库记录成功");
    }

    // 添加入库记录
    @PostMapping("/inbound")
    @Operation(summary = "添加入库记录", description = "创建一条入库记录")
    public Result<String> addInbound(@RequestBody InboundDTO inboundDTO) {
        stockLogService.addInbound(inboundDTO);
        return Result.success("添加入库记录成功");
    }

    // 添加出库记录
    @PostMapping("/outbound")
    @Operation(summary = "添加出库记录", description = "创建一条出库记录")
    public Result<String> addOutbound(@RequestBody OutboundDTO outboundDTO) {
        stockLogService.addOutbound(outboundDTO);
        return Result.success("添加出库记录成功");
    }

    // 调货
    @PostMapping("/transfer")
    @Operation(summary = "调货", description = "在不同仓库间调货")
    public Result<String> transferGoods(@RequestBody TransferDTO transferDTO) {
        stockLogService.transferGoods(transferDTO);
        return Result.success("调货成功");
    }
}