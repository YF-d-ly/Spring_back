package com.yf.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yf.entity.Role;
import com.yf.service.RoleService;
import com.yf.util.Result;
import com.yf.util.UserHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@Tag(name = "角色管理", description = "角色管理相关接口")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;



}