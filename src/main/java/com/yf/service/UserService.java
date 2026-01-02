package com.yf.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.entity.User;
import com.yf.entity.dto.LoginDTO;
import com.yf.entity.dto.page.UserQueryDTO;
import com.yf.entity.vo.Login.UserPermissionVO;
import com.yf.util.PageResult;

public interface UserService extends IService<User> {

    UserPermissionVO login(LoginDTO loginDTO);
    void logout(String token);

    // 用户管理功能
    IPage<User> getUserList(String account, String nickname, String roleId, Integer page, Integer size);
    
    PageResult<User> query(UserQueryDTO userQueryDTO);
    
    boolean addUser(User user);
    
    boolean updateUser(User user);
    
    boolean deleteUser(String id);
    
    boolean resetPassword(String id);
    
    boolean changePassword(String id, String oldPassword, String newPassword);
    
    boolean updateStatus(String id, Integer status);
    
    User getUserById(String id);
}