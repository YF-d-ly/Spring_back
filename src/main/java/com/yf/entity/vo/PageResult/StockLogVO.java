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
@Schema(description = "库存日志视图对象")
public class StockLogVO {
    @Schema(description = "日志ID")
    private String id;
    
    @Schema(description = "货品ID")
    private String goodsId;
    
    @Schema(description = "货品名称")
    private String goodsName;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "仓库名称")
    private String warehouseName;
    
    @Schema(description = "日志类型")
    private Integer type;
    
    @Schema(description = "数量")
    private Integer num;
    
    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "调拨ID")
    private String transferId;
}