package com.yf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("xmut_menu")
@Schema(description = "菜单信息")
public class Menu {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "菜单ID")
    private String id;
    
    @Schema(description = "菜单名称")
    private String menuName;
    
    @Schema(description = "菜单路径")
    private String menuPath;
    
    @Schema(description = "图标")
    private String icon;
    
    @Schema(description = "父菜单ID")
    private String parentId;
    
    @Schema(description = "排序")
    private Integer sortOrder;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}