USE kuaid_express;

-- 插入测试快递员用户
INSERT INTO `user` (openid, nickname, phone, real_name, role, status) VALUES
('test_courier_openid_001', '张快递', '13800000001', '张三', 1, 1);

-- 插入测试学生用户
INSERT INTO `user` (openid, nickname, phone, real_name, student_id, role, balance, status) VALUES
('test_student_openid_001', '李同学', '13900000001', '李四', '2021001', 0, 100.00, 1),
('test_student_openid_002', '王同学', '13900000002', '王五', '2021002', 0, 50.00, 1);

-- 插入测试站点
INSERT INTO `station` (name, address, contact_phone, manager_id, status) VALUES
('菜鸟驿站-南门', '南门商业街101号', '0571-88888001', 1, 1),
('丰巢快递柜-图书馆', '图书馆一楼东侧', '0571-88888002', 1, 1),
('校园快递中心-北区', '北区生活区3号楼', '0571-88888003', 1, 1);
