# Bank Core System - 银行核心系统

基于 Spring Boot 2.7 + MyBatis-Plus 的银行核心业务系统。

## 模块结构
| 模块 | 说明 |
|------|------|
| bank-common | 公共组件（工具类、异常、枚举、统一返回）|
| bank-customer | 客户管理模块 |
| bank-liability | 负债业务模块（账户、交易）|
| bank-teller | 柜员与机构管理模块 |
| bank-server | 聚合服务（单体应用入口）|

## 技术栈
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3.1
- MySQL 8.0
- Java 17
- Spring Security (BCrypt加密)
- Hutool 5.8.22

## 启动方式
1. 创建数据库：执行 `bank-core/db/init.sql`
2. 启动：`cd bank-server && mvn spring-boot:run`
3. 访问：http://localhost:8080

## 前端
前端项目位于 `bank-web/` 目录，使用 Vue 3 + Element Plus。
