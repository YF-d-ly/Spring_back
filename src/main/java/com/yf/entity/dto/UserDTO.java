package com.yf.entity.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO implements Serializable {
    private String id;
    private String username;
    private String password;
    private String roleId; // 修改为roleId，与数据库字段对应


}