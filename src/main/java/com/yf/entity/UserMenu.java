package com.yf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

@TableName("xmut_user_menu")
@Schema(description = "用户菜单关联")
public class UserMenu {
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "菜单ID")
    private String menuId;
}