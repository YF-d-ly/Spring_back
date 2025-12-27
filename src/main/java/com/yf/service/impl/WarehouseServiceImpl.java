package com.yf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.WarehouseDTO;
import com.yf.entity.dto.WarehouseQueryDTO;
import com.yf.entity.vo.WarehouseVO;
import com.yf.mapper.WarehouseMapper;
import com.yf.service.WarehouseService;
import com.yf.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {
    
    @Override
    public void save(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = new Warehouse();
        BeanUtils.copyProperties(warehouseDTO, warehouse);
        this.save(warehouse);
    }
    
    @Override
    public void updateById(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = new Warehouse();
        BeanUtils.copyProperties(warehouseDTO, warehouse);
        this.updateById(warehouse);
    }
    
    @Override
    public PageResult<WarehouseVO> pageQuery(WarehouseQueryDTO queryDTO) {
        // 创建分页对象
        Page<Warehouse> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        
        // 构建查询条件
        LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据仓库名称模糊查询
        if (queryDTO.getName() != null && !queryDTO.getName().isEmpty()) {
            queryWrapper.like(Warehouse::getName, queryDTO.getName());
        }
        
        // 根据仓库地址模糊查询
        if (queryDTO.getAddress() != null && !queryDTO.getAddress().isEmpty()) {
            queryWrapper.like(Warehouse::getAddress, queryDTO.getAddress());
        }
        
        // 根据联系人模糊查询
        if (queryDTO.getContact() != null && !queryDTO.getContact().isEmpty()) {
            queryWrapper.like(Warehouse::getContact, queryDTO.getContact());
        }
        
        // 执行分页查询
        IPage<Warehouse> result = this.page(page, queryWrapper);
        
        // 将Warehouse实体列表转换为WarehouseVO列表
        List<WarehouseVO> warehouseVOList = result.getRecords().stream()
                .map(warehouse -> WarehouseVO.builder()
                        .id(warehouse.getId())
                        .name(warehouse.getName())
                        .address(warehouse.getAddress())
                        .contact(warehouse.getContact())
                        .description(warehouse.getDescription())
                        .phone(warehouse.getPhone())
                        .status(warehouse.getStatus())
                        .build())
                .collect(Collectors.toList());
        
        // 封装并返回PageResult
        return new PageResult<>(
            result.getTotal(),           // 总记录数
            warehouseVOList,             // 当前页数据列表（VO类型）
            result.getPages(),           // 总页数
            result.getSize(),            // 每页大小
            result.getCurrent()          // 当前页码
        );
    }
}