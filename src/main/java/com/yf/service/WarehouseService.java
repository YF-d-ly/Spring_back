package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.WarehouseDTO;
import com.yf.entity.dto.WarehouseQueryDTO;
import com.yf.entity.vo.GoodVO;
import com.yf.entity.vo.WarehouseVO;
import com.yf.util.PageResult;
import java.util.List;

public interface WarehouseService extends IService<Warehouse> {
    void save(WarehouseDTO warehouseDTO);

    void updateById(WarehouseDTO warehouseDTO);
    
    PageResult<WarehouseVO> pageQuery(WarehouseQueryDTO queryDTO);


}