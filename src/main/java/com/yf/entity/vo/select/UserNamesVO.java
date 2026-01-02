package com.yf.entity.vo.select;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户下拉表")
public class UserNamesVO {
    @Schema(description = "ID")
    private String id;
    @Schema(description = "名称")
    private String nickname;
}
