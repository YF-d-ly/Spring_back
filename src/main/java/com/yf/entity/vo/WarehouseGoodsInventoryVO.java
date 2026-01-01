package com.yf.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "仓库内货物排行VO")
public class WarehouseGoodsInventoryVO {

    @Schema(description = "货品ID")
    private String goodsId;

    @Schema(description = "货品名称")
    private String goodsName;

    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "价格")
    private Double price;

    @Schema(description = "类别名称")
    private String categoryName;

    // 新增：入库总数量
    @Schema(description = "入库总数量")
    private Integer inboundNum;

    // 新增：出库总数量
    @Schema(description = "出库总数量")
    private Integer outboundNum;
}