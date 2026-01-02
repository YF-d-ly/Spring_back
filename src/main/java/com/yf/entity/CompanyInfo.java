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
@TableName("xmut_company")
@Schema(description = "企业信息")
public class CompanyInfo {
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "企业ID")
    private String id;
    
    @Schema(description = "企业名称")
    private String name;
    
    @Schema(description = "企业地址")
    private String address;
    
    @Schema(description = "联系人")
    private String contact;
    
    @Schema(description = "联系电话")
    private String phone;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}