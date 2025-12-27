package com.yf.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockLogQueryDTO {
    // 当前页码
    private Integer page = 1;
    // 每页大小
    private Integer size = 10;
    // 货品ID
    private String goodsName;


    // 仓库ID
    private String warehouseId;

    // 日志类型
    private Integer type;
    // 操作人
    private String operator;
    // 开始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    // 结束时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}