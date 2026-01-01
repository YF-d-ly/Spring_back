package com.yf.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("xmut_role_menu")
@Schema(description = "角色菜单关联实体")
public class RoleMenu {
    @Schema(description = "角色ID")
    private String roleId;
    
    @Schema(description = "菜单ID")
    private String menuId;
}