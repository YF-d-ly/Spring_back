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

import java.util.stream.Collectors;

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
    

}