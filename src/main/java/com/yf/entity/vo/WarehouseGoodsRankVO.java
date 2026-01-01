package com.yf.entity.vo;

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
@Schema(description = "仓库进出货物排行VO")
public class WarehouseGoodsRankVO {

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "入库总数量")
    private Integer inboundTotal;

    @Schema(description = "出库总数量")
    private Integer outboundTotal;

    @Schema(description = "总进出数量")
    private Integer totalInOut;

    @Schema(description = "最新操作时间")
    private LocalDateTime lastOperationTime;
}