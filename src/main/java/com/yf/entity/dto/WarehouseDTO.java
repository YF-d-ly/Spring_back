package com.yf.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "仓库传输对象")
public class WarehouseDTO {
    @Schema(description = "仓库ID")
    private String id;

    @Schema(description = "仓库名称")
    private String name;

    @Schema(description = "仓库描述")
    private String description;

    @Schema(description = "仓库地址")
    private String address;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "状态")
    private Integer status;
}