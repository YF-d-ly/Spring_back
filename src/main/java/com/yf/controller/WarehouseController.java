package com.yf.controller;

import com.yf.entity.Category;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.WarehouseDTO;
import com.yf.entity.dto.WarehouseQueryDTO;
import com.yf.entity.vo.CategoryVO;
import com.yf.entity.vo.GoodVO;
import com.yf.entity.vo.WarehouseNameVO;
import com.yf.entity.vo.WarehouseVO;

import com.yf.service.WarehouseService;
import com.yf.util.PageResult;
import com.yf.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库控制器
 */
@RestController
@RequestMapping("/warehouse")
@Tag(name = "仓库管理", description = "仓库管理相关接口")
@Slf4j
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;


    @GetMapping("/names")
    public Result<List<WarehouseNameVO>> getWarehouseNames() {
        List<Warehouse> warehouseList = warehouseService.list();
        List<WarehouseNameVO> warehouseNameVOList = warehouseList.stream()
                .map(warehouse -> WarehouseNameVO.builder()
                        .id(warehouse.getId())
                        .name(warehouse.getName())
                        .build())
                .collect(Collectors.toList());
        return Result.success(warehouseNameVOList, "获取仓库名称列表成功");
    }

    //分页获取仓库
    @GetMapping("/Page" )
    public Result<PageResult<WarehouseVO>> pageQuery(WarehouseQueryDTO queryDTO) {
        PageResult<WarehouseVO> warehouseList = warehouseService.pageQuery(queryDTO);
        return Result.success(warehouseList, "分页获取仓库成功");

    }






    // 添加仓库
    @PostMapping
    @Operation(summary = "新增仓库", description = "创建一个新的仓库")
    public Result<Void> addWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        warehouseService.save(warehouseDTO);
        return Result.success(null, "新增仓库成功");
    }
    
    // 修改仓库
    @PutMapping
    @Operation(summary = "修改仓库", description = "更新仓库信息")
    public Result<Void> updateWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        warehouseService.updateById(warehouseDTO);
        return Result.success(null, "修改仓库成功");
    }
    
    // 删除仓库
    @DeleteMapping("/{id}")
    @Operation(summary = "删除仓库", description = "根据ID删除仓库")
    public Result<Void> deleteWarehouse(@PathVariable String id) {
        warehouseService.removeById(id);
        return Result.success(null, "删除仓库成功");
    }

    

}