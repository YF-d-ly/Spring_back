package com.yf.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "货品视图对象")
public class GoodVO {
    @Schema(description = "ID")
    private String id;
    
    @Schema(description = "名称")
    private String name;
    
    @Schema(description = "描述")
    private String description;
    
    @Schema(description = "图片")
    private String image;
    
    @Schema(description = "价格")
    private Double price;
    
    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "仓库名称")
    private String  warehouseName;

    @Schema(description = "类别名称")
    private String categoryName;


}
