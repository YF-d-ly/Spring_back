package com.yf.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改密码请求参数")
public class ChangePasswordDTO {
    @Schema(description = "用户ID")
    private String id;
    
    @Schema(description = "旧密码")
    private String oldPassword;
    
    @Schema(description = "新密码")
    private String newPassword;
}