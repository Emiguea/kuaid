CREATE DATABASE IF NOT EXISTS kuaid_express DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE kuaid_express;

-- 用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `openid` VARCHAR(64) NOT NULL UNIQUE COMMENT '微信openid',
    `union_id` VARCHAR(64) DEFAULT NULL COMMENT '微信unionid',
    `nickname` VARCHAR(64) DEFAULT NULL,
    `avatar_url` VARCHAR(512) DEFAULT NULL,
    `phone` VARCHAR(20) DEFAULT NULL,
    `real_name` VARCHAR(32) DEFAULT NULL,
    `student_id` VARCHAR(32) DEFAULT NULL COMMENT '学号',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT '0=学生, 1=快递员',
    `balance` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0=禁用, 1=正常',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (`phone`),
    INDEX idx_student_id (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 快递站点表
CREATE TABLE `station` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '站点名称',
    `address` VARCHAR(255) NOT NULL,
    `longitude` DECIMAL(10,7) DEFAULT NULL,
    `latitude` DECIMAL(10,7) DEFAULT NULL,
    `contact_phone` VARCHAR(20) DEFAULT NULL,
    `manager_id` BIGINT DEFAULT NULL COMMENT '管理员用户ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0=关闭, 1=营业',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_manager (`manager_id`),
    CONSTRAINT fk_station_manager FOREIGN KEY (`manager_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 快递包裹表
CREATE TABLE `express` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `tracking_no` VARCHAR(64) NOT NULL COMMENT '快递单号',
    `company` VARCHAR(32) DEFAULT NULL COMMENT '快递公司',
    `station_id` BIGINT NOT NULL,
    `recipient_phone` VARCHAR(20) NOT NULL COMMENT '收件人手机号',
    `recipient_name` VARCHAR(32) DEFAULT NULL,
    `pickup_code` VARCHAR(10) NOT NULL COMMENT '取件码',
    `shelf_no` VARCHAR(20) DEFAULT NULL COMMENT '货架号',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0=待取件, 1=已取件, 2=已过期, 3=已退回',
    `registered_by` BIGINT NOT NULL COMMENT '入库操作快递员',
    `picked_by` BIGINT DEFAULT NULL COMMENT '取件用户',
    `registered_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `picked_at` DATETIME DEFAULT NULL,
    `expire_at` DATETIME DEFAULT NULL COMMENT '过期时间',
    `remark` VARCHAR(255) DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_tracking_station (`tracking_no`, `station_id`),
    INDEX idx_pickup_code (`pickup_code`),
    INDEX idx_recipient_phone (`recipient_phone`),
    INDEX idx_station_status (`station_id`, `status`),
    CONSTRAINT fk_express_station FOREIGN KEY (`station_id`) REFERENCES `station`(`id`),
    CONSTRAINT fk_express_registered_by FOREIGN KEY (`registered_by`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 代取订单表
CREATE TABLE `order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_no` VARCHAR(32) NOT NULL UNIQUE COMMENT '订单编号',
    `express_id` BIGINT NOT NULL,
    `student_id` BIGINT NOT NULL COMMENT '下单学生',
    `courier_id` BIGINT DEFAULT NULL COMMENT '接单快递员',
    `station_id` BIGINT NOT NULL,
    `fee` DECIMAL(10,2) NOT NULL COMMENT '服务费',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0=待接单, 1=已接单, 2=配送中, 3=已完成, 4=已取消',
    `delivery_address` VARCHAR(255) DEFAULT NULL COMMENT '配送地址',
    `student_remark` VARCHAR(255) DEFAULT NULL,
    `completed_at` DATETIME DEFAULT NULL,
    `cancelled_at` DATETIME DEFAULT NULL,
    `cancel_reason` VARCHAR(255) DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_student (`student_id`),
    INDEX idx_courier (`courier_id`),
    INDEX idx_status (`status`),
    CONSTRAINT fk_order_express FOREIGN KEY (`express_id`) REFERENCES `express`(`id`),
    CONSTRAINT fk_order_student FOREIGN KEY (`student_id`) REFERENCES `user`(`id`),
    CONSTRAINT fk_order_courier FOREIGN KEY (`courier_id`) REFERENCES `user`(`id`),
    CONSTRAINT fk_order_station FOREIGN KEY (`station_id`) REFERENCES `station`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 余额变动记录表
CREATE TABLE `balance_record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `type` TINYINT NOT NULL COMMENT '0=充值, 1=支付, 2=退款, 3=收入',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '变动金额',
    `before_balance` DECIMAL(10,2) NOT NULL,
    `after_balance` DECIMAL(10,2) NOT NULL,
    `related_order_no` VARCHAR(32) DEFAULT NULL,
    `description` VARCHAR(128) DEFAULT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (`user_id`),
    INDEX idx_type (`type`),
    CONSTRAINT fk_balance_user FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 通知消息表
CREATE TABLE `notification` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `type` TINYINT NOT NULL COMMENT '0=包裹到达, 1=订单更新, 2=余额变动, 3=系统通知',
    `title` VARCHAR(64) NOT NULL,
    `content` VARCHAR(512) NOT NULL,
    `is_read` TINYINT NOT NULL DEFAULT 0,
    `extra_data` TEXT DEFAULT NULL COMMENT 'JSON格式附加数据',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_read (`user_id`, `is_read`),
    CONSTRAINT fk_notification_user FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
