# MBCS 银行核心业务系统 — 测试文档

## 📊 总览

| 指标 | 数值 |
|------|------|
| 测试文件数 | 22 |
| 总体测试数 | **86** |
| 测试框架 | JUnit 5 + Mockito |
| 运行方式 | `mvn test` |

---

## 📁 模块分布

### 1. bank-customer（客户管理）— 17 个测试

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **CreateCustomerServiceTest** | `create_shouldSucceed_whenCommandIsValid` | 创建客户成功（客户号生成、所有字段校验） |
| **DeleteCustomerServiceTest** | `delete_shouldSucceed_whenCustomerExistsAndValid` | 正常删除客户，状态变更为 CLOSED |
| | `delete_shouldThrowException_whenCustomerNotExist` | 删除不存在的客户抛异常 |
| **UpdateCustomerServiceTest** | `update_shouldSucceed_whenCustomerExists` | 更新客户信息（姓名、电话、地址） |
| | `update_shouldThrowException_whenCustomerNotExist` | 更新不存在的客户抛异常 |
| **CustomerQueryServiceTest** | `findById_shouldReturnCustomer_whenExists` | 按 ID 查询存在客户 |
| | `findById_shouldReturnEmpty_whenNotExists` | 按 ID 查询不存在客户 |
| | `findByCustomerNo_shouldReturnCustomer_whenExists` | 按客户号查询 |
| | `findByCustomerNo_shouldReturnEmpty_whenNotExists` | 按客户号查询不存在 |
| | `findAll_shouldReturnAllCustomers` | 查询所有客户 |
| **CustomerDomainServiceTest** | `validateCreate_shouldSucceed_whenAllFieldsValid` | 创建校验通过 |
| | `validateCreate_shouldThrowException_whenIdNumberAlreadyExists` | 证件号重复校验 |
| | `validateUpdate_shouldSucceed_whenCustomerExistsAndIsNormal` | 更新校验通过 |
| | `validateUpdate_shouldThrowException_whenCustomerNotExist` | 更新时客户不存在 |
| | `validateUpdate_shouldThrowException_whenCustomerStatusIsNotNormal` | 更新时客户状态异常 |
| | `validateDelete_shouldSucceed_whenCustomerExistsAndIsNormal` | 删除校验通过 |
| | `validateDelete_shouldThrowException_whenCustomerStatusIsNotNormal` | 删除时客户状态异常 |

### 2. bank-liability（负债业务）— 51 个测试

#### 交易管理（17个）

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **DepositServiceTest** | `deposit_shouldSucceed_whenAccountIsNormal` | 存款成功（余额增加、交易流水生成） |
| | `deposit_shouldThrowException_whenAccountNotExist` | 存款时账户不存在 |
| | `deposit_shouldThrowException_whenAccountStatusIsNotNormal` | 存款时账户状态异常 |
| **WithdrawServiceTest** | `withdraw_shouldSucceed_whenAccountIsNormal` | 取款成功（余额减少、交易流水生成） |
| | `withdraw_shouldThrowException_whenAccountNotExist` | 取款时账户不存在 |
| | `withdraw_shouldThrowException_whenAccountIsNotNormal` | 取款时账户状态异常 |
| **DepositCancelServiceTest** | `cancel_shouldSucceed_whenConditionsMet` | 存款冲正成功 |
| | `cancel_shouldThrowException_whenOriginalTxNotExist` | 冲正时原始交易不存在 |
| | `cancel_shouldThrowException_whenOriginalTxIsNotDeposit` | 冲正时原始交易不是存款 |
| | `cancel_shouldThrowException_whenAccountNotExist` | 冲正时账户不存在 |
| | `cancel_shouldThrowException_whenAccountNotNormal` | 冲正时账户状态异常 |
| | `cancel_shouldThrowException_whenBalanceInsufficient` | 冲正时余额不足 |
| **WithdrawCancelServiceTest** | `cancel_shouldSucceed_whenConditionsMet` | 取款冲正成功 |
| | `cancel_shouldThrowException_whenOriginalTxNotExist` | 冲正时原始交易不存在 |
| | `cancel_shouldThrowException_whenOriginalTxIsNotWithdraw` | 冲正时原始交易不是取款 |
| | `cancel_shouldThrowException_whenAccountNotExist` | 冲正时账户不存在 |
| | `cancel_shouldThrowException_whenAccountNotNormal` | 冲正时账户状态异常 |

#### 账户管理（19个）

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **OpenLiabilityAccountServiceTest** | `open_shouldSucceed_whenCustomerAccountExists` | 负债账户开户成功（含子账户创建） |
| | `open_shouldThrowException_whenCustomerAccountNotExist` | 开户时客户账户不存在 |
| **CloseLiabilityAccountServiceTest** | `close_shouldSucceed_whenAccountExistsAndValid` | 销户成功（含子账户联动关闭） |
| | `close_shouldSucceed_whenNoSubAccount` | 销户时无子账户情况 |
| | `close_shouldThrowException_whenAccountNotExist` | 销户时账户不存在 |
| **FreezeLiabilityAccountServiceTest** | `freeze_shouldSucceed_whenAccountIsNormal` | 冻结正常账户 |
| | `freeze_shouldThrowException_whenAccountNotExist` | 冻结不存在的账户 |
| | `freeze_shouldThrowException_whenAccountAlreadyFrozen` | 重复冻结 |
| | `unfreeze_shouldSucceed_whenAccountIsFrozen` | 解冻已冻结账户 |
| | `unfreeze_shouldThrowException_whenAccountNotFrozen` | 解冻非冻结账户 |
| **LiabilityAccountQueryServiceTest** | `findByLiabilityAccountNo_shouldReturnAccount_whenExists` | 按账号查询 |
| | `findByLiabilityAccountNo_shouldReturnEmpty_whenNotExists` | 按账号查询不存在 |
| | `findByCustomerAccountNo_shouldReturnAccounts` | 按客户账号查询 |
| | `findAll_shouldReturnAllAccounts` | 查询全部 |

#### 客户账户管理（6个）

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **OpenCustomerAccountServiceTest** | `open_shouldSucceed_whenCommandIsValid` | 客户账户开户成功 |
| **CloseCustomerAccountServiceTest** | `close_shouldSucceed_whenNoActiveLiabilityAccount` | 销户成功（无活跃负债账户） |
| | `close_shouldThrowException_whenHasActiveLiabilityAccount` | 有未销负债账户时禁止销户 |
| | `close_shouldThrowException_whenCustomerAccountNotExist` | 客户账户不存在 |

#### 子账户管理（3个）

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **OpenSubAccountServiceTest** | `open_shouldSucceed_whenGeneratingUniqueAccountNo` | 子账户开户成功 |
| **CustomerSubAccountQueryServiceTest** | `findByCustomerAccountNo_shouldReturnSubAccounts` | 按客户账号查询 |
| | `findByCustomerAccountNoAndAccountType_shouldReturnFilteredResults` | 按账号+类型查询 |

#### 产品工厂（7个）

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **ProductServiceTest** | `listAll_shouldReturnAllProducts` | 查询所有产品 |
| | `getByProductCode_shouldReturnProduct_whenExists` | 按产品代码查询 |
| | `getByProductCode_shouldThrowException_whenNotExists` | 产品不存在 |
| | `save_shouldSucceed_whenProductCodeProvided` | 保存产品 |
| | `save_shouldThrowException_whenProductCodeNull` | 产品代码为空 |
| | `update_shouldSucceed_whenProductExists` | 更新产品 |
| | `update_shouldThrowException_whenProductNotExists` | 更新不存在的产品 |

#### 交易领域服务（6个）

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **TransactionDomainServiceTest** | `createTransaction_shouldSucceed_whenAllFieldsValid` | 创建交易流水 |
| | `createTransaction_shouldSaveWithRelatedTransactionNo` | 冲正交易有关联流水号 |
| | `validateCancel_shouldSucceed_whenTransactionIsNormal` | 验证可冲正 |
| | `validateCancel_shouldThrowException_whenTransactionNotExists` | 交易不存在不可冲正 |
| | `validateCancel_shouldThrowException_whenTransactionAlreadyCancelled` | 已冲正不可重复冲正 |
| | `cancelTransaction_shouldCreateCancelTransaction` | 执行冲正交易 |

### 3. bank-teller（柜员管理）— 18 个测试

| 测试类 | 方法 | 测试点 |
|--------|------|--------|
| **AuthServiceTest** | `login_shouldSucceed_whenCredentialsValid` | 登录成功（token 生成） |
| | `login_shouldThrowException_whenTellerNotExist` | 柜员不存在 |
| | `login_shouldThrowException_whenInstitutionNotMatch` | 机构号不匹配 |
| | `login_shouldThrowException_whenTellerStatusNotNormal` | 柜员状态异常 |
| | `login_shouldThrowException_whenPasswordWrong` | 密码错误 |
| | `validate_shouldSucceed_whenTokenExists` | token 验证合法 |
| | `validate_shouldThrowException_whenTokenInvalid` | token 非法 |
| | `logout_shouldRemoveToken` | 登出后 token 失效 |
| **InstitutionApplicationServiceTest** | `create_shouldSucceed_whenCommandIsValid` | 创建机构 |
| | `update_shouldSucceed_whenPartialFieldsProvided` | 更新机构 |
| | `update_shouldThrowException_whenInstitutionNotExist` | 更新不存在的机构 |
| | `delete_shouldSucceed_whenInstitutionExists` | 删除机构（状态变更） |
| | `delete_shouldThrowException_whenInstitutionNotExist` | 删除不存在的机构 |
| **TellerApplicationServiceTest** | `create_shouldSucceed_whenCommandIsValid` | 创建柜员（密码加密） |
| | `update_shouldSucceed_whenPartialFieldsProvided` | 更新柜员 |
| | `update_shouldThrowException_whenTellerNotExist` | 更新不存在的柜员 |
| | `delete_shouldSucceed_whenTellerExists` | 删除柜员（离职状态） |
| | `delete_shouldThrowException_whenTellerNotExist` | 删除不存在的柜员 |

---

## 🚀 运行方式

```bash
# 运行全部测试
cd bank-core
mvn test

# 运行单个模块
mvn test -pl bank-liability -am
mvn test -pl bank-teller -am
mvn test -pl bank-customer -am

# 运行单个测试类
mvn test -pl bank-liability -am -Dtest=WithdrawServiceTest
```

## 📝 测试规范

- **框架**: JUnit 5 (`@ExtendWith(MockitoExtension.class)`)
- **Mock 策略**: `@Mock` 注入 Repository 和 Domain Service，`@InjectMocks` 测试 Application Service
- **命名规范**: `{方法名}_should{预期结果}_when{条件}`
- **覆盖原则**: 每个方法至少包含 1 个正常路径 + 1 个异常路径
- **验证**: 使用 `verify()` 确认交互，`ArgumentCaptor` 确认保存的数据
