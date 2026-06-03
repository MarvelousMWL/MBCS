# Bank Teller - 柜员机构模块

柜员与机构管理模块。

## 功能
- 机构管理：创建、修改、查询、删除（软删除）
- 柜员管理：创建、修改、查询、删除（软删除）
- 权限控制：VAULT（库管员）可管理柜员和机构
- 登录认证：BCrypt 密码校验 + Token 会话管理

## 数据表
- sys_institution — 机构表（支持多级机构）
- sys_teller — 柜员表

## API
### 机构
- GET /api/teller/institution — 查询所有机构
- GET /api/teller/institution/{institutionNo} — 按编号查询
- POST /api/teller/institution — 创建机构（VAULT）
- PUT /api/teller/institution/{institutionNo} — 更新机构（VAULT）
- DELETE /api/teller/institution/{institutionNo} — 删除机构（VAULT）

### 柜员
- GET /api/teller/teller — 查询柜员列表
- GET /api/teller/teller/{tellerNo} — 按编号查询
- POST /api/teller/teller — 创建柜员（VAULT）
- PUT /api/teller/teller/{tellerNo} — 更新柜员（VAULT）
- DELETE /api/teller/teller/{tellerNo} — 删除柜员（VAULT）

### 认证
- POST /api/teller/auth/login — 登录
- POST /api/teller/auth/logout — 登出
- GET /api/teller/auth/me — 当前用户信息
