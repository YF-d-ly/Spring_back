-- 更新xmut_warehouse表结构，添加缺失的字段
-- 请在执行前确保数据库中已存在xmut_warehouse表且未包含以下字段

-- 添加address字段
ALTER TABLE xmut_warehouse 
ADD COLUMN address VARCHAR(255) NOT NULL COMMENT '仓库地址';

-- 添加description字段
ALTER TABLE xmut_warehouse 
ADD COLUMN description VARCHAR(500) COMMENT '仓库描述';

-- 添加contact字段
ALTER TABLE xmut_warehouse 
ADD COLUMN contact VARCHAR(50) COMMENT '联系人';

-- 添加phone字段
ALTER TABLE xmut_warehouse 
ADD COLUMN phone VARCHAR(20) COMMENT '联系电话';

-- 添加status字段
ALTER TABLE xmut_warehouse 
ADD COLUMN status TINYINT DEFAULT 1 COMMENT '状态：0=禁用，1=启用';