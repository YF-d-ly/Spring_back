package com.yf.entity.dto.Login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class LoginFormDTO {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;  // ✅ 改为email，与验证逻辑一致

//    @NotBlank(message = "手机号不能为空")
//    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
//    private String phone;  // ✅ 使用手机号格式验证

    @NotBlank(message = "验证码不能为空")
    private String code;

    private String password;
}