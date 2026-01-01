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
@Schema(description = "货物进出排行VO")
public class GoodsRankVO {

    @Schema(description = "货品ID")
    private String goodsId;

    @Schema(description = "货品名称")
    private String goodsName;

    @Schema(description = "入库总数量")
    private Integer inboundTotal;

    @Schema(description = "出库总数量")
    private Integer outboundTotal;

    @Schema(description = "总进出数量")
    private Integer totalInOut;

    @Schema(description = "最新操作时间")
    private LocalDateTime lastOperationTime;
}