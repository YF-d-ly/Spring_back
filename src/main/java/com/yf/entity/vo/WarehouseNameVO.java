package com.yf.entity.vo;

import com.fasterxml.jackson.core.JsonToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "仓库下拉表")
@Data
@Builder
public class WarehouseNameVO {

    @Schema(description = "ID")
    private String id;
    @Schema(description = "名称")
    private String name;


}
