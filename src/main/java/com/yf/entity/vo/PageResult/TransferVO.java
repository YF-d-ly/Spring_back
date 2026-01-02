package com.yf.entity.vo.PageResult;

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
public class TransferVO {


    @Schema(description = "货品ID")
    private String goodsId;

    @Schema(description = "货品名称")
    private String goodsName;



    @Schema(description = "备注")
    private String remark;


    @Schema(description = "调拨ID")
    private String transferId;


    // 出库信息
    @Schema(description = "出库仓库名称")
    private String outboundWarehouseName;

    @Schema(description = "源仓库ID")
    private String sourceWarehouseId;

    @Schema(description = "出库数量")
    private Integer outboundNum;

    @Schema(description = "出库操作人")
    private String outboundOperator;

    @Schema(description = "出库时间")
    private LocalDateTime outboundTime;


    // 入库信息
    @Schema(description = "入库仓库名称")
    private String inboundWarehouseName;

    @Schema(description = "目的仓库ID")
    private String targetWarehouseId;

    @Schema(description = "入库数量")
    private Integer inboundNum;

    @Schema(description = "入库操作人")
    private String inboundOperator;

    @Schema(description = "入库时间")
    private LocalDateTime inboundTime;
}
