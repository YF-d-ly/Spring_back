package com.yf.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "货品传输对象")
public class GoodsDTO {
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


}