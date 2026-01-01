package com.yf.service;

import com.yf.entity.CompanyInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CompanyInfoService extends IService<CompanyInfo> {
    CompanyInfo getCompanyInfo();
    boolean updateCompanyInfo(CompanyInfo companyInfo);
    boolean addCompanyInfo(CompanyInfo companyInfo);
}