# 高校快递收取系统

基于 SSM (Spring + SpringMVC + MyBatis) 框架的高校快递收取系统，支持微信小程序端使用。

## 技术栈

### 后端
- Spring 5.3 + SpringMVC + MyBatis 3.5
- Spring Security 5.7 + JWT 认证
- MySQL 8.0 + Druid 连接池
- Redis (Jedis) 缓存会话
- Maven 构建

### 前端
- 微信小程序原生开发

## 功能模块

- **微信一键登录** - 学生/快递员双角色
- **快递入库** - 快递员录入快递信息，自动生成取件码
- **取件码取件** - 学生凭取件码到站自取
- **代取订单** - 学生下单代取，快递员接单配送
- **余额系统** - 充值、支付、退款、收入
- **消息通知** - 快递到达通知、订单状态更新
- **站点管理** - 快递员管理快递站点

## 项目结构

```
kuaid/
├── backend/          # 后端 Java 代码 (WAR)
│   ├── pom.xml
│   ├── sql/          # 数据库脚本
│   └── src/main/
│       ├── java/com/kuaid/
│       └── resources/
└── miniprogram/      # 微信小程序前端
    ├── app.js
    ├── pages/
    └── utils/
```

## 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Tomcat 9.0+
- 微信开发者工具

### 后端部署

1. 创建数据库并执行脚本：
```bash
mysql -u root -p < backend/sql/schema.sql
mysql -u root -p < backend/sql/init-data.sql
```

2. 修改配置文件（注意：properties 文件在 .gitignore 中，需手动创建）：
   - `backend/src/main/resources/jdbc.properties` - 数据库连接
   - `backend/src/main/resources/redis.properties` - Redis 连接
   - `backend/src/main/resources/wechat.properties` - 微信小程序配置

3. 编译打包：
```bash
cd backend
mvn clean package
```

4. 部署 `target/kuaid-express.war` 到 Tomcat

### 小程序开发

1. 用微信开发者工具打开 `miniprogram/` 目录
2. 在 `app.js` 中修改 `baseUrl` 为后端地址
3. 在 `project.config.json` 中填入小程序 AppID

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/v1/auth/wx-login` | POST | 微信登录 |
| `/api/v1/express` | POST | 快递入库 |
| `/api/v1/express/my` | GET | 我的快递 |
| `/api/v1/express/{id}/pickup` | PUT | 确认取件 |
| `/api/v1/orders` | POST | 创建代取订单 |
| `/api/v1/orders/{id}/accept` | PUT | 接单 |
| `/api/v1/orders/{id}/complete` | PUT | 完成订单 |
| `/api/v1/balance` | GET | 查询余额 |
| `/api/v1/balance/recharge` | POST | 充值 |
| `/api/v1/notifications` | GET | 通知列表 |
