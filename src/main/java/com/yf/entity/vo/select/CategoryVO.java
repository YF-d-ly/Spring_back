package com.yf.entity.vo.select;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "类别下拉表")
@Data
public class CategoryVO {
    @Schema(description = "ID")
    private String id;
    @Schema(description = "名称")
    private String name;
}
