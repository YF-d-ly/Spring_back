package com.yf.entity.vo.select;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema
@Data
@Builder
public class GoodNameVO {
    @Schema(description = "ID")
    private String id;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "库存`")
    private Integer stock;




}
