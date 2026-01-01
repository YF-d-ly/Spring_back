package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.entity.Role;
import com.yf.mapper.RoleMapper;
import com.yf.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public List<Role> getAllRoles() {
        return roleMapper.selectList(null);
    }
}