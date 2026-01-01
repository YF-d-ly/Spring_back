-- 更新xmut_warehouse表结构，将location字段改回address字段
-- 请在执行前确保数据库中已存在xmut_warehouse表

-- 重命名location字段为address
ALTER TABLE xmut_warehouse 
CHANGE COLUMN location address VARCHAR(255) NOT NULL COMMENT '仓库地址';