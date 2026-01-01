package com.yf.entity.dto;

import lombok.Data;

@Data
public class OutboundDTO {
    private String goodsId;
    private String warehouseId;
    private Integer num;
    private String operator;
    private String remark;
    private String transferId;

}