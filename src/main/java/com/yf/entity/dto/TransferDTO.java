package com.yf.entity.dto;

import lombok.Data;

@Data
public class TransferDTO {
    private String goodsId;
    private String sourceWarehouseId;
    private String targetWarehouseId;
    private Integer num;
    private String operator;
    private String transferId;
    private String remark;
}