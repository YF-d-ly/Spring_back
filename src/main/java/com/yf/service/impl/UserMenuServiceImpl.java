package com.yf.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.entity.UserMenu;
import com.yf.mapper.UserMenuMapper;
import com.yf.service.UserMenuService;
import org.springframework.stereotype.Service;

@Service
public class UserMenuServiceImpl extends ServiceImpl<UserMenuMapper, UserMenu> implements UserMenuService {
}