# Bank Liability - 负债模块

负债业务模块，管理客户账户、负债账号和交易。

## 功能
- 客户账户开户/销户
- 负债账号开户/销户
- 子账户管理
- 存款/取款
- 交易冲正（存款撤销/取款撤销）
- 交易流水查询（支持分页、多条件筛选）

## 数据表
- acc_customer_account — 客户账号表
- acc_customer_sub_account — 客户子账户对照表
- acc_liability_account — 负债账号表（含乐观锁）
- acc_liability_transaction — 交易流水表

## API
- POST /api/liability/customer-account/open — 客户账户开户
- POST /api/liability/customer-account/close — 客户账户销户
- POST /api/liability/liability-account/open — 负债账户开户
- POST /api/liability/liability-account/close — 负债账户销户
- POST /api/liability/customer-sub-account/open — 子账户开户
- POST /api/liability/transaction/deposit — 存款
- POST /api/liability/transaction/withdraw — 取款
- POST /api/liability/transaction/deposit-cancel — 存款撤销
- POST /api/liability/transaction/withdraw-cancel — 取款撤销
- GET /api/liability/transaction — 交易流水查询（分页+筛选）
