package com.yf.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "仓库视图对象")
public class WarehouseVO {
    @Schema(description = "仓库ID")
    private String id;

    @Schema(description = "仓库名称")
    private String name;

    @Schema(description = "仓库地址")
    private String address;

    @Schema(description = "联系人")
    private String contact;

    @Schema(description = "联系方式")
    private String phone;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态")
    private Integer status;
}