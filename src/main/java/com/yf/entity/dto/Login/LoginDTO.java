package com.yf.entity.dto.Login;

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
    @Schema(defaultValue = "用户名或邮箱")
    @NotBlank(message = "用户名或邮箱不能为空")
    private String username;

    @Schema(defaultValue = "密码")
    private String password;
    
    @Schema(defaultValue = "验证码")
    private String code;
    
    @Schema(defaultValue = "登录类型")
    private String loginType; // password(密码登录), code(验证码登录)
}