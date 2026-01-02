package com.yf.entity.dto.page;

import lombok.Data;

@Data
public class UserQueryDTO {
    private String account;
    private String username;
    private String roleId;
    private Integer page = 1;
    private Integer size = 10;
}