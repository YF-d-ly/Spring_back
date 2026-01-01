package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.dto.LoginDTO;
import com.yf.entity.User;
import com.yf.entity.dto.UserDTO;
import com.yf.entity.vo.UserPermissionVO;

public interface UserService extends IService<User> {
    UserPermissionVO login(LoginDTO loginDTO);
    void logout(String token);
    
    // 用户管理功能
    IPage<User> getUserList(String account, String nickname, String roleId, Integer page, Integer size);
    
    boolean addUser(User user);
    
    boolean updateUser(User user);
    
    boolean deleteUser(String id);
    
    boolean resetPassword(String id);
    
    boolean changePassword(String id, String oldPassword, String newPassword);
    
    boolean updateStatus(String id, Integer status);
    
    User getUserById(String id);
}