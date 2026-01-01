package com.yf.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "仓库每日进出趋势VO")
public class WarehouseDailyTrendVO {

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "日期")
    private LocalDate date;

    @Schema(description = "入库数量")
    private Integer inboundNum;

    @Schema(description = "出库数量")
    private Integer outboundNum;

    @Schema(description = "净变化量")
    private Integer netChange;
}