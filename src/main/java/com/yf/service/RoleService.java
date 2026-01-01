package com.yf.service;

import com.yf.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface RoleService extends IService<Role> {
    List<Role> getAllRoles();
}