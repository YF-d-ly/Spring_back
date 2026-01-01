package com.yf.controller;

import com.yf.entity.CompanyInfo;
import com.yf.service.CompanyInfoService;
import com.yf.util.Result;
import com.yf.util.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Tag(name = "企业信息管理", description = "企业信息管理相关接口")
@Slf4j
public class CompanyInfoController {

    private static final String ROLE_SUPER_ADMIN = "ROLE_001"; // 超级管理员角色ID

    @Autowired
    private CompanyInfoService companyInfoService;

    /**
     * 获取企业信息
     */
    @GetMapping
    @Operation(summary = "获取企业信息")
    public Result<CompanyInfo> getCompanyInfo() {
        CompanyInfo companyInfo = companyInfoService.getCompanyInfo();
        return Result.success(companyInfo, "获取企业信息成功");
    }

    /**
     * 更新企业信息（仅超级管理员可访问）
     */
    @PutMapping
    @Operation(summary = "更新企业信息")
    public Result<Boolean> updateCompanyInfo(@RequestBody CompanyInfo companyInfo) {
        // 检查当前用户是否为超级管理员
        if (!ROLE_SUPER_ADMIN.equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = companyInfoService.updateCompanyInfo(companyInfo);
        return Result.success(result, result ? "更新企业信息成功" : "更新企业信息失败");
    }

    /**
     * 添加企业信息（仅超级管理员可访问，用于初始化）
     */
    @PostMapping
    @Operation(summary = "添加企业信息")
    public Result<Boolean> addCompanyInfo(@RequestBody CompanyInfo companyInfo) {
        // 检查当前用户是否为超级管理员
        if (!ROLE_SUPER_ADMIN.equals(UserHolder.getUser().getRoleId())) {
            return Result.error("权限不足，只有超级管理员可以访问此功能");
        }
        
        boolean result = companyInfoService.addCompanyInfo(companyInfo);
        return Result.success(result, result ? "添加企业信息成功" : "添加企业信息失败");
    }
}