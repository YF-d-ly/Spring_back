package com.yf.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分配仓库权限请求DTO
 */
@Data
@Schema(description = "分配仓库权限请求DTO")
public class AssignWarehousePermissionDTO {
    
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "仓库ID列表")
    private List<String> warehouseIds;
}