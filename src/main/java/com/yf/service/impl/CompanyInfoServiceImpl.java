package com.yf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yf.entity.CompanyInfo;
import com.yf.mapper.CompanyInfoMapper;
import com.yf.service.CompanyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CompanyInfoServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements CompanyInfoService {

    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Override
    public CompanyInfo getCompanyInfo() {
        // 由于系统只有一家企业信息，查询第一条记录即可
        return companyInfoMapper.selectOne(new LambdaQueryWrapper<>());
    }

    @Override
    public boolean updateCompanyInfo(CompanyInfo companyInfo) {
        // 更新企业信息，如果不存在则插入
        if (companyInfo.getId() != null) {
            return this.updateById(companyInfo);
        } else {
            // 如果没有ID，尝试获取现有的企业信息
            CompanyInfo existingInfo = this.getCompanyInfo();
            if (existingInfo != null) {
                companyInfo.setId(existingInfo.getId());
                return this.updateById(companyInfo);
            } else {
                // 如果不存在企业信息，则添加新的
                return this.save(companyInfo);
            }
        }
    }

    @Override
    public boolean addCompanyInfo(CompanyInfo companyInfo) {
        // 如果已存在企业信息，则不能添加
        CompanyInfo existingInfo = this.getCompanyInfo();
        if (existingInfo != null) {
            log.warn("企业信息已存在，无法重复添加");
            return false;
        }
        
        return this.save(companyInfo);
    }
}