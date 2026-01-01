package com.yf.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登入需要的数据")
public class LoginDTO {
    @Schema(defaultValue = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(defaultValue = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
