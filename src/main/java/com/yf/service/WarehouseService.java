package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.WarehouseDTO;
import com.yf.entity.dto.page.WarehouseQueryDTO;
import com.yf.entity.vo.PageResult.WarehouseVO;
import com.yf.util.PageResult;

public interface WarehouseService extends IService<Warehouse> {
    void save(WarehouseDTO warehouseDTO);

    void updateById(WarehouseDTO warehouseDTO);

    PageResult<WarehouseVO> pageQuery(WarehouseQueryDTO queryDTO);
    
    PageResult<WarehouseVO> pageQueryByUserPermission(WarehouseQueryDTO queryDTO, String userId);
    
    boolean hasWarehousePermission(String userId, String warehouseId);


}