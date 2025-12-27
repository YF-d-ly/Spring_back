package com.yf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

@TableName("xmut_user_warehouse")
@Schema(description = "用户仓库关联")
public class UserWarehouse {
    @Schema(description = "用户ID")
    private String userId;
    
    @Schema(description = "仓库ID")
    private String warehouseId;
}