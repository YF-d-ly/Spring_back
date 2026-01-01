package com.yf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("xmut_user")
@Schema(description = "用户实体")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "加密密码")
    private String password;

    @Schema(description = "昵称/真实姓名")
    private String nickname;

    @Schema(description = "联系电话")
    private String telephone;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "性别：0-女，1-男，2-保密")
    private Integer sex;

    @Schema(description = "角色ID")
    private String roleId;

    @Schema(description = "状态：0=禁用，1=启用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}