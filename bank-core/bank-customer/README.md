# Bank Customer - 客户模块

客户关系管理（CRM）模块。

## 功能
- 客户信息创建、修改、查询、删除
- 客户状态管理（正常/停用/注销）
- 证件号码唯一性校验

## 数据表
- crm_customer — 客户信息表

## API
- POST /api/customer — 创建客户
- PUT /api/customer — 修改客户
- DELETE /api/customer/{id} — 删除客户
- GET /api/customer — 查询所有客户
- GET /api/customer/{id} — 按ID查询
- GET /api/customer/no/{customerNo} — 按客户号查询
