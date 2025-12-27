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
@TableName("xmut_category")
@Schema(description = "类别信息")
public class Category {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "类别ID")
    private String id;
    
    @Schema(description = "类别名称")
    private String name;
    
    @Schema(description = "父类别ID")
    private String parentId;
    
    @Schema(description = "描述")
    private String description;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}