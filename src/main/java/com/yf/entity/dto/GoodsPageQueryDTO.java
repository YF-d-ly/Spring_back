package com.yf.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "货品分页查询DTO")
public class GoodsPageQueryDTO {
    @Schema(description = "当前页码")
    private Integer page = 1;
    
    @Schema(description = "每页大小")
    private Integer size = 10;
    
    @Schema(description = "货品名称")
    private String name;
    
    @Schema(description = "类别ID")
    private String categoryId;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "价格范围最小值")
    private Double minPrice;
    
    @Schema(description = "价格范围最大值")
    private Double maxPrice;
}