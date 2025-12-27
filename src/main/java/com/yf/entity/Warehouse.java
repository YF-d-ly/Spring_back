package com.yf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("xmut_warehouse")
@Schema(description = "仓库信息")
public class Warehouse {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "仓库ID")
    private String id;
    
    @Schema(description = "仓库名称")
    private String name;
    
    @Schema(description = "仓库地址")
    private String address;

    @Schema(description = "仓库描述")
    private String description;

    
    @Schema(description = "联系人")
    private String contact;
    
    @Schema(description = "联系电话")
    private String phone;
    
    @Schema(description = "状态")
    private Integer status;
}