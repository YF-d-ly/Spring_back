package com.yf.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.constant.RedisConstants;
import com.yf.entity.Menu;
import com.yf.entity.Role;
import com.yf.entity.User;
import com.yf.entity.UserMenu;
import com.yf.entity.UserWarehouse;
import com.yf.entity.Warehouse;
import com.yf.entity.dto.UserDTO;
import com.yf.entity.vo.UserPermissionVO;
import com.yf.enums.UserRoleEnum;
import com.yf.mapper.MenuMapper;
import com.yf.mapper.RoleMapper;
import com.yf.mapper.UserMapper;
import com.yf.mapper.UserMenuMapper;
import com.yf.mapper.UserWarehouseMapper;
import com.yf.mapper.WarehouseMapper;
import com.yf.service.UserService;
import com.yf.dto.LoginDTO;
import com.yf.util.HutoolJwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private MenuMapper menuMapper;
    
    @Autowired
    private WarehouseMapper warehouseMapper;
    
    @Autowired
    private UserMenuMapper userMenuMapper;
    
    @Autowired
    private UserWarehouseMapper userWarehouseMapper;

    @Autowired
    private HutoolJwtUtil hutoolJwtUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RoleMapper roleMapper;

    @Value("${jwt.expiration:86400}")
    private Long expiration; // 过期时间，秒

    @Override
    public UserPermissionVO login(LoginDTO loginDTO) {
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginDTO.getUsername()));

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 验证密码（不直接在SQL中验证）
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 使用HutoolJwtUtil生成JWT
        String token = hutoolJwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 创建用户DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setRoleId(user.getRoleId()); // 设置用户角色ID
        
        // 将用户信息存储到Redis，以用户ID作为key
        String key = RedisConstants.LOGIN_USER_KEY + user.getId();
        // 将JWT token存储到Redis中，用于会话管理
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("token", token); // 存储JWT token
        userMap.put("userDTO", userDTO); // 存储用户信息
        redisTemplate.opsForHash().putAll(key, userMap);
        // 设置过期时间为JWT的过期时间（从配置中获取）
        redisTemplate.expire(key, Duration.ofSeconds(expiration));

        log.info("用户登录成功: {}", user.getUsername());
        
        // 获取用户角色
        Role role = roleMapper.selectById(user.getRoleId());
        boolean isSuperAdmin = role != null && "SUPER_ADMIN".equals(role.getRoleCode());
        
        // 获取用户菜单权限
        List<Menu> menus = getMenuListByUserId(user.getId(), isSuperAdmin);
        
        // 获取用户仓库权限
        List<Warehouse> warehouses = getWarehouseListByUserId(user.getId(), isSuperAdmin);

        // 返回用户权限信息
        return UserPermissionVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .roleId(user.getRoleId())
                .menus(menus)
                .warehouses(warehouses)
                .build();
    }

    /**
     * 根据用户ID获取菜单列表
     */
    private List<Menu> getMenuListByUserId(String userId, boolean isSuperAdmin) {
        if (isSuperAdmin) {
            // 超级管理员拥有所有菜单权限
            return menuMapper.selectList(null);
        } else {
            // 普通用户根据角色获取菜单权限
            User user = userMapper.selectById(userId);
            if (user != null) {
                return menuMapper.getMenusByRoleId(user.getRoleId());
            }
            return null;
        }
    }

    /**
     * 根据用户ID获取仓库列表
     */
    private List<Warehouse> getWarehouseListByUserId(String userId, boolean isSuperAdmin) {
        if (isSuperAdmin) {
            // 超级管理员拥有所有仓库权限
            return warehouseMapper.selectList(null);
        } else {
            // 普通用户只拥有分配的仓库权限
            return warehouseMapper.getWarehousesByUserId(userId);
        }
    }

    @Override
    public void logout(String token) {
        // 从Redis中删除用户会话信息
        try {
            // 先解析JWT获取用户ID
            JWT jwt = JWTUtil.parseToken(token);
            String userId = jwt.getPayloads().getStr("userId");
            String key = RedisConstants.LOGIN_USER_KEY + userId;
            
            // 验证Redis中存储的token是否与当前token一致
            Map<Object, Object> userMap = redisTemplate.opsForHash().entries(key);
            String storedToken = (String) userMap.get("token");
            
            if (storedToken != null && storedToken.equals(token)) {
                redisTemplate.delete(key);
            }
            
            // 同时将JWT加入黑名单，使其在过期前失效
            // 解析JWT获取过期时间
            Long expiresAt = jwt.getPayloads().getLong("exp");
            if (expiresAt != null) {
                long remainingTime = expiresAt * 1000 - System.currentTimeMillis();
                if (remainingTime > 0) {
                    hutoolJwtUtil.blacklistToken(token, remainingTime);
                }
            }
            log.info("用户登出成功，Token: {}", token);
        } catch (Exception e) {
            log.warn("登出失败: {}", e.getMessage());
        }
    }

    @Override
    public IPage<User> getUserList(String account, String username, String roleId, Integer page, Integer size) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (account != null && !account.isEmpty()) {
            queryWrapper.like("username", account);
        }
        if (username != null && !username.isEmpty()) {
            queryWrapper.like("username", username);
        }
        if (roleId != null && !roleId.isEmpty()) {
            queryWrapper.eq("role_id", roleId);
        }
        queryWrapper.orderByDesc("created_at");
        
        Page<User> userPage = new Page<>(page, size);
        return userMapper.selectPage(userPage, queryWrapper);
    }

    @Override
    public boolean addUser(User user) {
        // 设置默认值
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认启用
        }
        if (user.getRoleId() == null) {
            user.setRoleId("ROLE_003"); // 默认普通用户
        }
        
        // 密码加密
        if (user.getPassword() != null) {
            String encryptedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(encryptedPassword);
        }
        
        return this.save(user);
    }

    @Override
    public boolean updateUser(User user) {
        // 不能修改密码字段，密码单独通过其他接口修改
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser != null) {
            user.setPassword(existingUser.getPassword()); // 保持原密码
            return this.updateById(user);
        }
        return false;
    }

    @Override
    public boolean deleteUser(String id) {
        return this.removeById(id);
    }

    @Override
    public boolean resetPassword(String id) {
        User user = this.getById(id);
        if (user != null) {
            // 重置为默认密码，这里使用123456的MD5值
            String defaultPassword = DigestUtils.md5DigestAsHex("123456".getBytes());
            user.setPassword(defaultPassword);
            return this.updateById(user);
        }
        return false;
    }

    @Override
    public boolean changePassword(String id, String oldPassword, String newPassword) {
        User user = this.getById(id);
        if (user != null) {
            String encryptedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
            if (user.getPassword().equals(encryptedOldPassword)) {
                String encryptedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
                user.setPassword(encryptedNewPassword);
                return this.updateById(user);
            }
        }
        return false;
    }

    @Override
    public boolean updateStatus(String id, Integer status) {
        User user = this.getById(id);
        if (user != null) {
            user.setStatus(status);
            return this.updateById(user);
        }
        return false;
    }

    @Override
    public User getUserById(String id) {
        return this.getById(id);
    }
}