package com.yf.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分配菜单权限请求DTO
 */
@Data
@Schema(description = "分配菜单权限请求DTO")
public class AssignMenuPermissionDTO {
    
    @Schema(description = "角色ID")
    private String roleId;
    
    @Schema(description = "菜单ID列表")
    private List<String> menuIds;
}