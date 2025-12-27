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
@TableName("xmut_goods")
@Schema(description = "货品信息")
public class Goods {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "货品ID")
    private String id;
    
    @Schema(description = "货品名称")
    private String name;
    
    @Schema(description = "类别ID")
    private String categoryId;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "价格")
    private Double price;
    
    @Schema(description = "图片URL")
    private String imageUrl;
    
    @Schema(description = "描述")
    private String description;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

}