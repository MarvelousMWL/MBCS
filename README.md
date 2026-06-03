# MBCS - 银行核心业务系统

基于 Spring Boot 2.7 + MyBatis-Plus + Vue 3 的银行核心账务系统，涵盖对公存款、账户管理、交易处理等核心银行业务。

## ✨ 功能特性

| 模块 | 功能 |
|------|------|
| 🏢 **机构管理** | 银行机构层级管理 |
| 👤 **柜员管理** | 柜员账户、权限分配 |
| 👥 **客户管理** | 客户信息维护 |
| 💳 **账户业务** | 开销户（个人/对公/负债账户）、账户查询、子账户管理 |
| 🔒 **账户限制** | 账户冻结/解冻、限制状态总览 |
| 💰 **交易管理** | 存款/取款交易、交易冲正、交易流水查询 |
| 🏭 **产品工厂** | 产品参数化配置（kdpf_* 参数表体系）|

## 🏗️ 技术栈

### 后端
| 技术 | 说明 |
|------|------|
| Spring Boot 2.7.18 | Web 框架 |
| Spring Cloud Alibaba 2021.0.6.1 | 微服务 |
| MyBatis-Plus 3.5.3 | ORM |
| MySQL / Oracle / PostgreSQL / H2 | 数据库 |
| Nacos / ESB | 服务注册与配置中心 |
| Redis | 缓存、分布式锁 |
| Kafka | 异步消息 |
| Sentinel | 限流熔断 |
| Spring Security | 安全认证 |

### 前端
| 技术 | 说明 |
|------|------|
| Vue 3 | UI 框架 |
| Element Plus | 组件库 |
| Pinia | 状态管理 |
| Vue Router | 路由 |
| Axios | HTTP 请求 |
| Vite | 构建工具 |

## 📁 项目结构

`
MBCS/
├── bank-core/                   # 后端 (Java / Maven)
│   ├── bank-common/             # 公共组件（工具类、异常、枚举、统一返回）
│   ├── bank-customer/           # 客户管理模块
│   ├── bank-liability/          # 负债业务模块（账户、交易、产品工厂）
│   ├── bank-teller/             # 柜员与机构管理模块
│   ├── bank-server/             # 聚合服务（单体应用入口）
│   └── db/                      # 数据库初始化脚本
├── bank-web/                    # 前端 (Vue 3)
│   └── src/
│       ├── views/               # 页面组件
│       ├── router/              # 路由配置
│       ├── stores/              # 状态管理
│       └── api/                 # API 接口
├── bank-maven/                  # Maven 仓库配置
└── AGENTS.md                    # Codex 开发指引
`

## 🚀 快速启动

### 环境要求
- JDK 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0

### 后端启动

`ash
# 1. 创建数据库
mysql -u root -p < bank-core/db/init.sql

# 2. 启动服务
cd bank-core/bank-server
mvn spring-boot:run
`

服务启动后访问 http://localhost:8080

### 前端启动

`ash
cd bank-web
npm install
npm run dev
`

前端开发服务器访问 http://localhost:3000

### 默认登录

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | admin123 | 管理员 |

## 📚 领域知识

本项目的业务领域知识参考 \-deposit skill，涵盖：
- **产品工厂**：产品参数配置体系，8 张核心 kdpf_* 参数表
- **账户生命周期**：开户 → 正常 → 冻结/停用 → 销户
- **大额存单**：发行 → 认购 → 计息 → 转让 → 兑付
- **利率利息**：年/月/日利率，单利/复利/分段计息
- **批量作业**：结息、自动兑付、逾期处理

