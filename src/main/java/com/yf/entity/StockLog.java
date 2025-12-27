package com.yf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@TableName("xmut_stock_log")
@Schema(description = "库存日志")
public class StockLog {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "日志ID")
    private String id;
    
    @Schema(description = "货品ID")
    private String goodsId;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
    
    @Schema(description = "日志类型")
    private Integer type;
    
    @Schema(description = "数量")
    private Integer num;
    
    @Schema(description = "操作人")
    private String operator;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "调拨ID")
    private String transferId;
}