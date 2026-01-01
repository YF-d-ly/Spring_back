package com.yf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("xmut_role")
@Schema(description = "角色实体")
public class Role {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "角色ID")
    private String id;
    
    @Schema(description = "角色名称")
    private String roleName;
    
    @Schema(description = "角色代码")
    private String roleCode;
    
    @Schema(description = "角色描述")
    private String description;
    
    @Schema(description = "状态：0=禁用，1=启用")
    private Integer status;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}