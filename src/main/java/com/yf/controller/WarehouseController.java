package com.yf.controller;

import com.yf.entity.Warehouse;
import com.yf.entity.dto.WarehouseDTO;
import com.yf.entity.dto.page.WarehouseQueryDTO;
import com.yf.entity.vo.select.WarehouseNameVO;
import com.yf.entity.vo.PageResult.WarehouseVO;

import com.yf.service.WarehouseService;
import com.yf.service.PermissionService;
import com.yf.util.PageResult;
import com.yf.util.Result;
import com.yf.util.UserHolder;
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

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/names")
    public Result<List<WarehouseNameVO>> getWarehouseNames() {
        // 检查当前用户是否为超级管理员
        String currentUserId = UserHolder.getUser().getId();
        String currentUserRoleId = UserHolder.getUser().getRoleId();
        
        List<Warehouse> warehouseList;
        if (currentUserRoleId != null && currentUserRoleId.equals("ROLE_001")) {
            // 超级管理员可以查看所有仓库
            warehouseList = warehouseService.list();
        } else {
            // 普通用户只能查看自己有权限的仓库
            warehouseList = permissionService.getUserWarehouses(currentUserId);
        }
        
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
        // 检查当前用户是否为超级管理员
        String currentUserId = UserHolder.getUser().getId();
        String currentUserRoleId = UserHolder.getUser().getRoleId();

        PageResult<WarehouseVO> warehouseList;
        if (currentUserRoleId != null && currentUserRoleId.equals("ROLE_001")) {
            // 超级管理员可以查看所有仓库
            warehouseList = warehouseService.pageQuery(queryDTO);
        } else {
            // 普通用户只能查看自己有权限的仓库
            warehouseList = warehouseService.pageQueryByUserPermission(queryDTO, currentUserId);
        }
        return Result.success(warehouseList, "分页获取仓库成功");

    }

    // 获取所有仓库（不按权限过滤，仅超级管理员可访问）
    @GetMapping("/all")
    @Operation(summary = "获取所有仓库", description = "获取所有仓库（仅超级管理员）")
    public Result<List<WarehouseVO>> getAllWarehouses() {
        // 检查当前用户是否为超级管理员
        String currentUserRoleId = UserHolder.getUser().getRoleId();
        if (currentUserRoleId == null || !currentUserRoleId.equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        List<Warehouse> warehouseList = warehouseService.list();
        List<WarehouseVO> warehouseVOList = warehouseList.stream()
                .map(warehouse -> WarehouseVO.builder()
                        .id(warehouse.getId())
                        .name(warehouse.getName())
                        .address(warehouse.getAddress())
                        .contact(warehouse.getContact())
                        .description(warehouse.getDescription())
                        .phone(warehouse.getPhone())
                        .status(warehouse.getStatus())
                        .build())
                .collect(Collectors.toList());
        return Result.success(warehouseVOList, "获取所有仓库成功");
    }
    
    // 添加仓库
    @PostMapping("/add")
    @Operation(summary = "新增仓库", description = "创建一个新的仓库")
    public Result<Void> addWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        // 检查当前用户是否为超级管理员
        String currentUserRoleId = UserHolder.getUser().getRoleId();
        if (currentUserRoleId == null || !currentUserRoleId.equals("ROLE_001")) {
            return Result.error("权限不足，只有超级管理员可以执行此操作");
        }
        
        warehouseService.save(warehouseDTO);
        return Result.success(null, "新增仓库成功");
    }
    
    // 修改仓库
    @PutMapping
    @Operation(summary = "修改仓库", description = "更新仓库信息")
    public Result<Void> updateWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        // 检查当前用户是否为超级管理员
        String currentUserRoleId = UserHolder.getUser().getRoleId();

        if (currentUserRoleId == null || !currentUserRoleId.equals("ROLE_001")) {
            // 普通用户需要检查是否有该仓库的权限
            String currentUserId = UserHolder.getUser().getId();
            if (!warehouseService.hasWarehousePermission(currentUserId, warehouseDTO.getId())) {
                return Result.error("权限不足，您没有操作此仓库的权限");
            }
        }
        warehouseService.updateById(warehouseDTO);

        return Result.success(null, "修改仓库成功");
    }
    
    // 删除仓库
    @DeleteMapping("/{id}")
    @Operation(summary = "删除仓库", description = "根据ID删除仓库")
    public Result<Void> deleteWarehouse(@PathVariable String id) {

        // 检查当前用户是否为超级管理员
        String currentUserRoleId = UserHolder.getUser().getRoleId();
        if (currentUserRoleId == null || !currentUserRoleId.equals("ROLE_001")) {
            // 普通用户需要检查是否有该仓库的权限
            String currentUserId = UserHolder.getUser().getId();
            if (!warehouseService.hasWarehousePermission(currentUserId, id)) {
                return Result.error("权限不足，您没有操作此仓库的权限");
            }
        }
        
        warehouseService.removeById(id);
        return Result.success(null, "删除仓库成功");
    }

    

}