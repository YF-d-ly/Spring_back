-- 创建数据库
CREATE DATABASE IF NOT EXISTS xmut_warehouse
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE xmut_warehouse;

-- 1. 企业基本信息（仅1条）
CREATE TABLE xmut_company (
                              id VARCHAR(45) PRIMARY KEY COMMENT 'UUID，固定为1条记录',
                              name VARCHAR(100) NOT NULL COMMENT '企业名称',
                              address VARCHAR(255) NOT NULL COMMENT '具体地址',
                              contact VARCHAR(50) NOT NULL COMMENT '联系人',
                              phone VARCHAR(20) NOT NULL COMMENT '联系方式',
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='企业基本信息';

-- 2. 仓库信息
CREATE TABLE xmut_warehouse (
                                id VARCHAR(45) PRIMARY KEY COMMENT 'UUID',
                                name VARCHAR(100) NOT NULL COMMENT '仓库名称',
                                address VARCHAR(255) NOT NULL COMMENT '仓库地址',
                                description VARCHAR(500) COMMENT '仓库描述',
                                contact VARCHAR(50) COMMENT '联系人',
                                phone VARCHAR(20) COMMENT '联系电话',
                                status TINYINT DEFAULT 1 COMMENT '状态：0=禁用，1=启用',
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='仓库信息';

-- 3. 货品类别（支持多级）
CREATE TABLE xmut_category (
                               id VARCHAR(45) PRIMARY KEY COMMENT 'UUID',
                               name VARCHAR(50) NOT NULL COMMENT '类别名称',
                               parent_id VARCHAR(45) NULL COMMENT '父类ID，NULL表示顶级',
                               description VARCHAR(255) COMMENT '描述',
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (parent_id) REFERENCES xmut_category(id) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='货品类别';

-- 4. 货品信息
CREATE TABLE xmut_goods (
                            id VARCHAR(45) PRIMARY KEY COMMENT 'UUID',
                            name VARCHAR(100) NOT NULL COMMENT '货品名称',
                            price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
                            image_url VARCHAR(255) COMMENT '图片URL',
                            stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0) COMMENT '当前库存',
                            category_id VARCHAR(45) NOT NULL COMMENT '所属类别ID',
                            warehouse_id VARCHAR(45) NOT NULL COMMENT '所属仓库ID',
                            description VARCHAR(255) COMMENT '描述',
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            FOREIGN KEY (category_id) REFERENCES xmut_category(id) ON DELETE RESTRICT,
                            FOREIGN KEY (warehouse_id) REFERENCES xmut_warehouse(id) ON DELETE RESTRICT
) ENGINE=InnoDB COMMENT='货品信息';

-- 5. 系统用户
CREATE TABLE xmut_user (
                           id VARCHAR(45) PRIMARY KEY COMMENT 'UUID',
                           account VARCHAR(45) NOT NULL UNIQUE COMMENT '登录账号',
                           password VARCHAR(100) NOT NULL COMMENT '加密密码',
                           nickname VARCHAR(45) NOT NULL COMMENT '昵称/真实姓名',
                           telephone VARCHAR(20) COMMENT '联系电话',
                           mobile VARCHAR(20) COMMENT '手机号',
                           email VARCHAR(50) COMMENT '邮箱',
                           sex TINYINT DEFAULT 2 COMMENT '性别：0-女，1-男，2-保密',
                           role TINYINT NOT NULL DEFAULT 2 COMMENT '1=超级管理员，2=信息管理员',
                           status TINYINT NOT NULL DEFAULT 1 COMMENT '0=禁用，1=启用',
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='系统用户';

-- 6. 系统菜单
CREATE TABLE xmut_menu (
                           id VARCHAR(45) PRIMARY KEY COMMENT 'UUID',
                           menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
                           menu_path VARCHAR(100) NOT NULL COMMENT '前端路由路径',
                           icon VARCHAR(50) COMMENT '图标标识',
                           parent_id VARCHAR(45) NULL COMMENT '父菜单ID',
                           sort_order INT DEFAULT 0 COMMENT '排序权重',
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (parent_id) REFERENCES xmut_menu(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='系统菜单';

-- 7. 用户-菜单权限关联表
CREATE TABLE xmut_user_menu (
                                user_id VARCHAR(45) NOT NULL,
                                menu_id VARCHAR(45) NOT NULL,
                                PRIMARY KEY (user_id, menu_id),
                                FOREIGN KEY (user_id) REFERENCES xmut_user(id) ON DELETE CASCADE,
                                FOREIGN KEY (menu_id) REFERENCES xmut_menu(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='用户菜单权限';

-- 8. 用户-仓库权限关联表
CREATE TABLE xmut_user_warehouse (
                                     user_id VARCHAR(45) NOT NULL,
                                     warehouse_id VARCHAR(45) NOT NULL,
                                     PRIMARY KEY (user_id, warehouse_id),
                                     FOREIGN KEY (user_id) REFERENCES xmut_user(id) ON DELETE CASCADE,
                                     FOREIGN KEY (warehouse_id) REFERENCES xmut_warehouse(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='用户仓库权限';

-- 9. 出入库记录（含调货）
CREATE TABLE xmut_stock_log (
                                id VARCHAR(45) PRIMARY KEY COMMENT 'UUID',
                                goods_id VARCHAR(45) NOT NULL COMMENT '货品ID',
                                warehouse_id VARCHAR(45) NOT NULL COMMENT '仓库ID',
                                type TINYINT NOT NULL COMMENT '1=入库，2=出库',
                                num INT NOT NULL CHECK (num > 0) COMMENT '数量',
                                operator VARCHAR(45) NOT NULL COMMENT '操作人账号（xmut_user.account）',
                                create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                transfer_id VARCHAR(45) NULL COMMENT '调货单ID：非NULL表示属于某次调货',
                                remark VARCHAR(255) COMMENT '备注',
                                FOREIGN KEY (goods_id) REFERENCES xmut_goods(id) ON DELETE RESTRICT,
                                FOREIGN KEY (warehouse_id) REFERENCES xmut_warehouse(id) ON DELETE RESTRICT
) ENGINE=InnoDB COMMENT='出入库记录';

-- 索引优化
CREATE INDEX idx_stock_goods ON xmut_stock_log(goods_id);
CREATE INDEX idx_stock_warehouse ON xmut_stock_log(warehouse_id);
CREATE INDEX idx_stock_transfer ON xmut_stock_log(transfer_id);
CREATE INDEX idx_stock_time ON xmut_stock_log(create_time);

-- ========================
-- 初始化数据（可选）
-- ========================

-- 插入企业信息（id 固定为 'COMPANY_001'）
INSERT INTO xmut_company (id, name, address, contact, phone)
VALUES ('COMPANY_001', '厦门理工学院示范企业', '福建省厦门市集美区理工路600号', '张主任', '0592-1234567');

-- 插入超级管理员（account: admin / password: 123456，实际应加密）
INSERT INTO xmut_user (id, account, password, nickname, role, status)
VALUES ('USER_ADMIN', 'admin', '$2a$10$DfTqWzQYvJZmKlMnOpQrS.OuXyZ1234567890', '超级管理员', 1, 1);

-- 插入基础菜单（示例）
INSERT INTO xmut_menu (id, menu_name, menu_path, icon, parent_id, sort_order) VALUES
                                                                                  ('MENU_01', '系统管理', '/system', 'setting', NULL, 1),
                                                                                  ('MENU_02', '用户管理', '/system/user', 'user', 'MENU_01', 1),
                                                                                  ('MENU_03', '仓库管理', '/warehouse/list', 'warehouse', NULL, 2),
('MENU_04', '货品管理', '/goods/list', 'package', NULL, 3),
                                                                                  ('MENU_05', '出入库记录', '/stock/log', 'history', NULL, 4),
                                                                                  ('MENU_06', '数据统计', '/report', 'bar-chart', NULL, 5);

-- 超级管理员拥有所有菜单
INSERT INTO xmut_user_menu (user_id, menu_id)
SELECT 'USER_ADMIN', id FROM xmut_menu;

-- ========================
-- 测试数据（可选）
-- ========================

-- 插入仓库数据
INSERT INTO xmut_warehouse (id, name, address, description, contact, phone, status) VALUES
('WH001', 'A仓库', '福建省厦门市集美区理工路600号A栋', '主要存放电子产品', '张主任', '0592-1111111', 1),
('WH002', 'B仓库', '福建省厦门市集美区理工路600号B栋', '主要存放机械设备', '李主任', '0592-2222222', 1),
('WH003', 'C仓库', '福建省厦门市集美区理工路600号C栋', '主要存放原材料', '王主任', '0592-3333333', 1),
('WH004', 'D仓库', '福建省厦门市集美区理工路600号D栋', '主要存放办公用品', '陈主任', '0592-4444444', 1),
('WH005', 'E仓库', '福建省厦门市集美区理工路600号E栋', '主要存放食品', '刘主任', '0592-5555555', 0),
('WH006', 'F仓库', '福建省厦门市集美区理工路600号F栋', '主要存放纺织品', '赵主任', '0592-6666666', 1);

-- 插入货品类别数据
INSERT INTO xmut_category (id, name, description) VALUES
('CAT001', '电子产品', '电子设备及配件'),
('CAT002', '办公用品', '日常办公用品'),
('CAT003', '生活用品', '日常生活用品'),
('CAT004', '食品饮料', '食品和饮料类'),
('CAT005', '图书文具', '图书和文具用品');

-- 插入货品数据
INSERT INTO xmut_goods (id, name, price, image_url, stock, category_id, warehouse_id, description) VALUES
('GOODS001', '笔记本电脑', 5999.99, '/images/laptop.jpg', 50, 'CAT001', 'WH001', '高性能办公笔记本电脑'),
('GOODS002', '无线鼠标', 99.00, '/images/mouse.jpg', 200, 'CAT001', 'WH001', '无线光电鼠标'),
('GOODS003', 'A4打印纸', 25.50, '/images/paper.jpg', 100, 'CAT002', 'WH001', 'A4规格打印纸，每包500张'),
('GOODS004', '签字笔', 15.00, '/images/pen.jpg', 300, 'CAT002', 'WH002', '黑色签字笔'),
('GOODS005', '保温杯', 45.00, '/images/cup.jpg', 80, 'CAT003', 'WH002', '500ml不锈钢保温杯'),
('GOODS006', '矿泉水', 2.00, '/images/water.jpg', 500, 'CAT004', 'WH002', '550ml瓶装矿泉水'),
('GOODS007', 'C++程序设计', 65.80, '/images/book.jpg', 40, 'CAT005', 'WH003', 'C++程序设计教材'),
('GOODS008', '文件夹', 8.50, '/images/folder.jpg', 250, 'CAT002', 'WH003', 'A4文件夹');

-- 插入出入库记录数据
INSERT INTO xmut_stock_log (id, goods_id, warehouse_id, type, num, operator, create_time, transfer_id, remark) VALUES
('STOCK001', 'GOODS001', 'WH001', 1, 10, 'admin', '2024-01-15 09:30:00', NULL, '采购入库'),
('STOCK002', 'GOODS002', 'WH001', 1, 50, 'admin', '2024-01-16 10:15:00', NULL, '补充库存'),
('STOCK003', 'GOODS003', 'WH001', 1, 20, 'admin', '2024-01-17 11:45:00', NULL, '日常进货'),
('STOCK004', 'GOODS004', 'WH002', 1, 100, 'admin', '2024-01-18 14:20:00', NULL, '办公用品采购'),
('STOCK005', 'GOODS005', 'WH002', 1, 30, 'admin', '2024-01-19 15:10:00', NULL, '员工福利用品'),
('STOCK006', 'GOODS006', 'WH002', 1, 200, 'admin', '2024-01-20 16:05:00', NULL, '会议用水'),
('STOCK007', 'GOODS007', 'WH003', 1, 15, 'admin', '2024-01-21 09:00:00', NULL, '新学期教材入库'),
('STOCK008', 'GOODS008', 'WH003', 1, 80, 'admin', '2024-01-22 10:30:00', NULL, '办公用品入库'),
('STOCK009', 'GOODS001', 'WH001', 2, 2, 'user001', '2024-01-23 13:45:00', NULL, '销售出库'),  -- 出库记录
('STOCK010', 'GOODS004', 'WH002', 2, 10, 'user002', '2024-01-24 14:30:00', NULL, '部门领用');  -- 出库记录


-- 插入普通用户
INSERT INTO xmut_user (id, account, password, nickname, telephone, mobile, email, role, status) VALUES
('USER001', 'user001', '$2a$10$DfTqWzQYvJZmKlMnOpQrS.OuXyZ1234567890', '张三', '0592-1111111', '13800138001', 'zhangsan@example.com', 2, 1),
('USER002', 'user002', '$2a$10$DfTqWzQYvJZmKlMnOpQrS.OuXyZ1234567890', '李四', '0592-2222222', '13800138002', 'lisi@example.com', 2, 1),
('USER003', 'user003', '$2a$10$DfTqWzQYvJZmKlMnOpQrS.OuXyZ1234567890', '王五', '0592-3333333', '13800138003', 'wangwu@example.com', 2, 1);

-- 为普通用户分配仓库权限
INSERT INTO xmut_user_warehouse (user_id, warehouse_id) VALUES
('USER001', 'WH001'),
('USER002', 'WH002'),
('USER003', 'WH003');

-- ========================
-- 调货业务说明（伪代码逻辑）
-- ========================
/*
调货100个货品从A仓到B仓：
1. 生成唯一 transfer_id = 'TRANSFER_' + UUID()
2. 插入出库记录：type=2, warehouse_id=A, num=100, transfer_id=xxx
3. 插入入库记录：type=1, warehouse_id=B, num=100, transfer_id=xxx
4. 更新货品库存（Service层处理）：
   - A仓货品 stock -= 100
   - B仓需新增或更新货品记录（若B仓无此货品，则插入新记录）
*/