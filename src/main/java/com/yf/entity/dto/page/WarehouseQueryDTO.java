package com.yf.entity.dto.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "仓库分页查询DTO")
public class WarehouseQueryDTO {
    @Schema(description = "当前页码")
    private Integer page = 1;

    @Schema(description = "每页大小")
    private Integer size = 10;

    @Schema(description = "仓库名称")
    private String name;

    @Schema(description = "仓库地址")
    private String address;

    @Schema(description = "联系人")
    private String contact;
}