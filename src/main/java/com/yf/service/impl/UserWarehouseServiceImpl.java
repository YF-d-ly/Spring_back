package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.entity.UserWarehouse;
import com.yf.mapper.UserWarehouseMapper;
import com.yf.service.UserWarehouseService;
import org.springframework.stereotype.Service;

@Service
public class UserWarehouseServiceImpl extends ServiceImpl<UserWarehouseMapper, UserWarehouse> implements UserWarehouseService {
}