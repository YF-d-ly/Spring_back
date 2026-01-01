package com.yf.entity.dto;

import lombok.Data;

@Data
public class InboundDTO {
    private String goodsId;
    private String warehouseId;
    private Integer num;
    private String operator;
    private String transferId;
    private String remark;
}