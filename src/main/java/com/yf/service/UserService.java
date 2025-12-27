package com.yf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.dto.LoginDTO;
import com.yf.entity.User;

public interface UserService extends IService<User> {
    String login(LoginDTO loginDTO);
    void logout(String token);
}