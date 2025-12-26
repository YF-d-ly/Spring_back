package com.yf.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户登入返回的数据")
public class LoginVO {


    @Schema(defaultValue = "主键值")
    private Long id;

    @Schema(defaultValue = "用户名")
    private String userName;

    @Schema(defaultValue = "姓名")
    private String name;

    @Schema(defaultValue = "jwt令牌")
    private String token;

}
