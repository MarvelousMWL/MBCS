-- ============================================
-- MBCS 账户层数据表 - ADBC标准
-- 继承自产品工厂表，开户时实例化
-- ============================================

-- 1. 负债账户信息表 (核心账户表)
DROP TABLE IF EXISTS kdaf_zhxx;
CREATE TABLE kdaf_zhxx (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL UNIQUE COMMENT '负债账号',
  account_name VARCHAR(200) COMMENT '账户名称',
  customer_no VARCHAR(20) COMMENT '客户号',
  country_code VARCHAR(6) COMMENT '国别代码',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  cash_exchange_flag VARCHAR(4) COMMENT '账户钞汇标志',
  term VARCHAR(20) COMMENT '存期',
  maturity_date DATE COMMENT '到期日期',
  interest_start_method VARCHAR(4) COMMENT '起息方式',
  initial_interest_start_date DATE COMMENT '初始起息日期',
  initial_maturity_date DATE COMMENT '初始到期日期',
  business_code VARCHAR(20) COMMENT '业务代号',
  batch_institution VARCHAR(20) COMMENT '批处理机构',
  account_institution VARCHAR(20) COMMENT '账户所属机构',
  open_institution VARCHAR(20) COMMENT '开户机构',
  open_date DATE COMMENT '开户日期',
  open_teller VARCHAR(20) COMMENT '账户开户柜员',
  close_institution VARCHAR(20) COMMENT '账户销户机构',
  close_date DATE COMMENT '账户销户日期',
  close_teller VARCHAR(20) COMMENT '账户销户柜员',
  deposit_promoter VARCHAR(50) COMMENT '揽存人员',
  account_manager VARCHAR(50) COMMENT '账户经理名称',
  account_valid_date DATE COMMENT '账户有效期',
  current_unused_seq INT COMMENT '当前未用序号',
  current_balance DECIMAL(18,2) DEFAULT 0 COMMENT '当前账户余额',
  last_day_balance DECIMAL(18,2) DEFAULT 0 COMMENT '上日账户余额',
  balance_update_date DATE COMMENT '余额最近更新日期',
  first_deposit_date DATE COMMENT '首次存入日期',
  last_business_date DATE COMMENT '上次业务日期',
  last_collection_date DATE COMMENT '上次代收付日期',
  product_code VARCHAR(20) COMMENT '产品编号',
  liability_product_type VARCHAR(20) COMMENT '负债产品类型',
  customer_type VARCHAR(10) COMMENT '产品所属对象',
  account_classify1 VARCHAR(20) COMMENT '账户分类代码1',
  account_classify2 VARCHAR(20) COMMENT '账户分类代码2',
  account_classify3 VARCHAR(20) COMMENT '账户分类代码3',
  conversion_currency VARCHAR(6) COMMENT '换算币种',
  max_retain_balance DECIMAL(18,2) COMMENT '最大留存余额',
  min_retain_balance DECIMAL(18,2) COMMENT '最小留存余额',
  deposit_ctrl_mode VARCHAR(4) COMMENT '存入控制方式',
  deposit_ctrl_method VARCHAR(4) COMMENT '存入控制方法',
  deposit_process_order VARCHAR(4) COMMENT '存入处理顺序',
  withdraw_ctrl_mode VARCHAR(4) COMMENT '支取控制方式',
  withdraw_ctrl_method VARCHAR(4) COMMENT '支取控制方法',
  custom_withdraw_method VARCHAR(20) COMMENT '自定义支取控制方式',
  customer_account VARCHAR(30) COMMENT '客户账号',
  renew_method VARCHAR(4) COMMENT '转存方式',
  reserve_amount DECIMAL(18,2) COMMENT '备用金额',
  open_amount DECIMAL(18,2) COMMENT '开户金额',
  deposit_type VARCHAR(4) COMMENT '存款种类',
  account_status VARCHAR(4) DEFAULT '0' COMMENT '账户状态',
  balance_sync_flag VARCHAR(4) DEFAULT '1' COMMENT '余额与总账同步标志',
  combo_product_no VARCHAR(20) COMMENT '组合产品号',
  combo_product_seq INT COMMENT '组合产品序号',
  combo_master_account VARCHAR(30) COMMENT '组合产品母账户号',
  combo_template_code VARCHAR(20) COMMENT '组合账户模板代码',
  combo_template_name VARCHAR(100) COMMENT '组合账户模板名称',
  combo_account_type VARCHAR(20) COMMENT '组合账户类型',
  account_restrict_flag VARCHAR(4) COMMENT '账户限制标志',
  restrict_type VARCHAR(10) COMMENT '限制类型',
  overdraft_flag VARCHAR(4) COMMENT '关联透支标志',
  form_transfer_flag VARCHAR(4) COMMENT '形态转移标志',
  dormant_to_normal_flag VARCHAR(4) COMMENT '存入后不动户转正常标志',
  monitor_flag VARCHAR(4) COMMENT '监控账户标志',
  allow_overdraft_flag VARCHAR(4) COMMENT '允许透支标志',
  forex_supervision_flag VARCHAR(4) COMMENT '外汇监管标志',
  forex_check_flag VARCHAR(4) COMMENT '外汇核查标志',
  settlement_flag VARCHAR(4) COMMENT '结算账户标志',
  financial_planning_flag VARCHAR(4) COMMENT '签约理财标志',
  cash_withdraw_flag VARCHAR(4) COMMENT '允许现金支取标志',
  transfer_withdraw_flag VARCHAR(4) COMMENT '允许转账支取标志',
  cash_deposit_flag VARCHAR(4) COMMENT '允许现金存入标志',
  transfer_deposit_flag VARCHAR(4) COMMENT '允许转账存入标志',
  agreement_deposit_flag VARCHAR(4) COMMENT '协定存款标志',
  simple_interest_flag VARCHAR(4) COMMENT '是否简单计息',
  interest_payment_method VARCHAR(4) COMMENT '利息支付方式',
  passbook_flag VARCHAR(4) COMMENT '是否有折标志',
  realtime_transfer_flag VARCHAR(4) COMMENT '实时划拨标志',
  balance_collection_flag VARCHAR(4) COMMENT '余额归集标志',
  amount_freeze_flag VARCHAR(4) COMMENT '账户金额冻结标志',
  closed_freeze_flag VARCHAR(4) COMMENT '账户封闭冻结标志',
  only_receive_no_pay_flag VARCHAR(4) COMMENT '账户只收不付标志',
  only_pay_no_receive_flag VARCHAR(4) COMMENT '账户只付不收标志',
  check_code VARCHAR(10) COMMENT '校验码',
  reserve_field01 VARCHAR(50) COMMENT '备用字段01',
  reserve_field02 VARCHAR(50) COMMENT '备用字段02',
  reserve_field03 VARCHAR(50) COMMENT '备用字段03',
  reserve_balance01 DECIMAL(18,2) COMMENT '备用余额01',
  reserve_date01 DATE COMMENT '备用日期1',
  open_channel VARCHAR(20) COMMENT '开户渠道',
  accrual_biz_code VARCHAR(20) COMMENT '计提业务编码',
  real_account_flag VARCHAR(4) COMMENT '是否反应到实账户',
  account_status_field VARCHAR(20) COMMENT '账户状态字段',
  batch_split_group VARCHAR(20) COMMENT '批量拆分组号',
  supervise_account_flag VARCHAR(4) COMMENT '监管账户标志',
  supervise_account_type VARCHAR(10) COMMENT '监管账户类型',
  check_flag VARCHAR(4) COMMENT '领用支票标志',
  whitelist_flag VARCHAR(4) COMMENT '白名单标志',
  ftz_account_flag VARCHAR(4) COMMENT '自贸区账户标志',
  cash_management_flag VARCHAR(4) COMMENT '现金管理签约标志',
  reserve_fund_flag VARCHAR(4) COMMENT '备付金账户标志',
  custody_flag VARCHAR(4) COMMENT '托管类账户标志',
  fiscal_account_flag VARCHAR(4) COMMENT '财政账户标志',
  fast_open_flag VARCHAR(4) COMMENT '快速开户标志',
  approval_flag VARCHAR(4) COMMENT '核准备案标志',
  account_eng_name VARCHAR(200) COMMENT '账户英文名称',
  account_eng_short VARCHAR(100) COMMENT '账户英文简称',
  account_cn_short VARCHAR(100) COMMENT '账户中文简称',
  related_biz_no VARCHAR(50) COMMENT '相关业务编号',
  sellable_product_no VARCHAR(20) COMMENT '可售产品编号',
  reserve_fund_type VARCHAR(10) COMMENT '备付金账户类型',
  interbank_deposit_type VARCHAR(10) COMMENT '同业存放账户类型',
  fiscal_deposit_type VARCHAR(10) COMMENT '财政存款账户类型',
  custody_account_type VARCHAR(10) COMMENT '托管账户类型',
  ftz_account_type VARCHAR(10) COMMENT '自贸区账户类型',
  foreign_ctrl_account_nature VARCHAR(10) COMMENT '外管账户性质',
  clean_account_flag VARCHAR(4) COMMENT '是否廉政账户标志',
  fund_supervision_flag VARCHAR(4) COMMENT '资金监管标志',
  billing_flag VARCHAR(4) COMMENT '计费标志',
  face_verify_flag VARCHAR(4) COMMENT '当面核实标志',
  reserve_char01 VARCHAR(50) COMMENT '备用字符01',
  reserve_char02 VARCHAR(50) COMMENT '备用字符02',
  reserve_char03 VARCHAR(50) COMMENT '备用字符03',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_customer_no (customer_no),
  INDEX idx_product_code (product_code),
  INDEX idx_customer_account (customer_account)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='负债账户信息表';

-- 2. 账户销户控制表 (继承产品销户控制表)
DROP TABLE IF EXISTS kdaf_xiohkz;
CREATE TABLE kdaf_xiohkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  close_ctrl_method VARCHAR(4) DEFAULT '0' COMMENT '销户控制方法',
  custom_ctrl_method VARCHAR(20) COMMENT '自定义控制方式',
  early_close_ctrl VARCHAR(4) DEFAULT '0' COMMENT '提前销户控制方式',
  penalty_type VARCHAR(4) DEFAULT '0' COMMENT '处罚类型',
  sign_check_method VARCHAR(4) DEFAULT '1' COMMENT '签约检查方式',
  arrears_check_method VARCHAR(4) DEFAULT '1' COMMENT '欠费检查方式',
  close_fund_to VARCHAR(4) DEFAULT '0' COMMENT '销户资金去向',
  transfer_account_nature VARCHAR(4) DEFAULT '0' COMMENT '转账账户性质',
  source VARCHAR(10) COMMENT '来源 P-产品继承 O-开户覆盖',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户销户控制表';

-- 3. 账户存入控制表 (继承产品存入控制表)
DROP TABLE IF EXISTS kdaf_cunrkz;
CREATE TABLE kdaf_cunrkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  cash_deposit_flag VARCHAR(4) DEFAULT '1' COMMENT '现金存入标志',
  transfer_deposit_flag VARCHAR(4) DEFAULT '1' COMMENT '转账存入标志',
  deposit_ctrl_mode VARCHAR(4) DEFAULT '2' COMMENT '存入控制方式',
  deposit_ctrl_method VARCHAR(4) DEFAULT '0' COMMENT '存入控制方法',
  amount_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '存入金额控制方式',
  single_min_amount DECIMAL(18,2) DEFAULT 0 COMMENT '单次存入最小金额',
  single_max_amount DECIMAL(18,2) DEFAULT 0 COMMENT '单次存入最大金额',
  times_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '存入次数控制方式',
  min_deposit_times INT DEFAULT 0 COMMENT '最小存入次数',
  max_deposit_times INT DEFAULT 0 COMMENT '最大存入次数',
  deposit_plan_flag VARCHAR(4) DEFAULT '0' COMMENT '设置存入计划标志',
  first_min_amount DECIMAL(18,2) DEFAULT 0 COMMENT '首次存入最小金额',
  retain_max_balance DECIMAL(18,2) DEFAULT 0 COMMENT '账户留存最大余额',
  deposit_frequency VARCHAR(20) COMMENT '存入频率',
  source VARCHAR(10) COMMENT '来源 P-产品继承 O-开户覆盖',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户存入控制表';

-- 4. 账户支取控制表 (继承产品支取控制表)
DROP TABLE IF EXISTS kdaf_zhiqkz;
CREATE TABLE kdaf_zhiqkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  cash_withdraw_flag VARCHAR(4) DEFAULT '1' COMMENT '现金支取标志',
  transfer_withdraw_flag VARCHAR(4) DEFAULT '1' COMMENT '转账支取标志',
  withdraw_ctrl_mode VARCHAR(4) DEFAULT '1' COMMENT '支取控制方式',
  amount_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '支取金额控制方式',
  single_min_amount DECIMAL(18,2) DEFAULT 0 COMMENT '单次支取最小金额',
  max_withdraw_times INT DEFAULT 0 COMMENT '最大支取次数',
  account_retain_min_balance DECIMAL(18,2) DEFAULT 0 COMMENT '账户留存最小余额',
  source VARCHAR(10) COMMENT '来源',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户支取控制表';

-- 5. 账户计息定义表 (继承产品利息定义表)
DROP TABLE IF EXISTS kdaf_lixidy;
CREATE TABLE kdaf_lixidy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  interest_type VARCHAR(4) DEFAULT '0' COMMENT '利息类型',
  interest_flag VARCHAR(4) DEFAULT '0' COMMENT '计息标志',
  interest_adjust_method VARCHAR(4) DEFAULT '0' COMMENT '利息调整方法',
  standard_interest_method VARCHAR(4) DEFAULT '10' COMMENT '标准计息方法',
  interest_payment_method VARCHAR(4) DEFAULT '0' COMMENT '利息支付方式',
  interest_frequency VARCHAR(10) DEFAULT 'D' COMMENT '计息频率',
  payment_frequency VARCHAR(20) DEFAULT '1QA21E' COMMENT '付息频率',
  tax_flag VARCHAR(4) DEFAULT '0' COMMENT '计税标志',
  min_interest_amount DECIMAL(18,2) DEFAULT 1 COMMENT '最小计息金额',
  source VARCHAR(10) COMMENT '来源',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户计息定义表';

-- 6. 账户利率定义表 (继承产品利率定义表)
DROP TABLE IF EXISTS kdaf_lilvdy;
CREATE TABLE kdaf_lilvdy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  liability_rate_type VARCHAR(4) DEFAULT '0' COMMENT '负债利率类型',
  rate_code VARCHAR(20) COMMENT '利率编号',
  rate_term_flag VARCHAR(4) DEFAULT '0' COMMENT '利率存期标志',
  rate_determine_date VARCHAR(4) DEFAULT '0' COMMENT '利率确定日期',
  rate_adjust_frequency VARCHAR(10) DEFAULT 'D' COMMENT '利率调整频率',
  rate_update_method VARCHAR(4) DEFAULT '0' COMMENT '利率更新处理方式',
  source VARCHAR(10) COMMENT '来源',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户利率定义表';

-- 7. 账户到期定义表 (继承产品到期定义表)
DROP TABLE IF EXISTS kdaf_daoqkz;
CREATE TABLE kdaf_daoqkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  account_institution VARCHAR(20) COMMENT '账户开户机构',
  account_maturity_date DATE COMMENT '账户到期日',
  holiday_process_method VARCHAR(4) DEFAULT '0' COMMENT '遇节假日处理方式',
  deposit_delay_flag VARCHAR(4) DEFAULT '0' COMMENT '是否根据存款顺延到期日',
  maturity_renew_method VARCHAR(4) COMMENT '到期续存方式',
  principal_transfer_flag VARCHAR(4) COMMENT '本金是否转入系统内',
  principal_transfer_account VARCHAR(30) COMMENT '本金转入客户账号',
  interest_transfer_flag VARCHAR(4) COMMENT '利息是否转入系统内',
  interest_transfer_account VARCHAR(30) COMMENT '利息转入客户账号',
  maturity_grace_days INT COMMENT '到期宽限期',
  process_status VARCHAR(4) COMMENT '处理状态',
  maturity_specify_flag VARCHAR(4) COMMENT '到期日指定标志',
  renew_rate_adjust_method VARCHAR(4) DEFAULT '0' COMMENT '转存利率调整方式',
  source VARCHAR(10) COMMENT '来源',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户到期定义表';

-- 8. 客户账号表
DROP TABLE IF EXISTS kdaf_khzh;
CREATE TABLE kdaf_khzh (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_account VARCHAR(30) NOT NULL UNIQUE COMMENT '客户账号',
  customer_account_type VARCHAR(10) COMMENT '客户账号类型',
  customer_no VARCHAR(20) COMMENT '客户号',
  exchange_flag VARCHAR(4) COMMENT '通兑标志',
  exchange_scope VARCHAR(20) COMMENT '通兑范围',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_customer_no (customer_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户账号表';

-- 9. 账户信息补充表
DROP TABLE IF EXISTS kdaf_xxbc;
CREATE TABLE kdaf_xxbc (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  holder_nature VARCHAR(10) COMMENT '开户单位性质',
  holder_credit_code VARCHAR(50) COMMENT '开户单位统一信用代码',
  holder_id_type VARCHAR(10) COMMENT '开户单位证件类型',
  holder_id_no VARCHAR(50) COMMENT '开户单位证件号码',
  holder_legal_person VARCHAR(100) COMMENT '开户单位法人',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户信息补充表';

-- 10. 账户余额信息表
DROP TABLE IF EXISTS kdaf_yexx;
CREATE TABLE kdaf_yexx (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  cumulative_overdraft DECIMAL(18,2) DEFAULT 0 COMMENT '累计透支额',
  available_withdraw_interest DECIMAL(18,2) DEFAULT 0 COMMENT '可支取利息',
  unused_quota DECIMAL(18,2) DEFAULT 0 COMMENT '未使用额度',
  available_balance DECIMAL(18,2) DEFAULT 0 COMMENT '可用余额',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户余额信息表';

-- 11. 账户续存定义表
DROP TABLE IF EXISTS kdaf_xcdy;
CREATE TABLE kdaf_xcdy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  account_institution VARCHAR(20) COMMENT '账户开户机构',
  renew_date DATE COMMENT '续存日期',
  new_product_code VARCHAR(20) COMMENT '新产品编号',
  renew_term VARCHAR(20) COMMENT '续存存期',
  renew_amount DECIMAL(18,2) COMMENT '续存金额',
  remainder_transfer_account VARCHAR(30) COMMENT '零头转入客户账号',
  remainder_deposit_account VARCHAR(30) COMMENT '零头存入账户',
  renew_grace_days INT COMMENT '续存宽限期',
  interest_adjust_method VARCHAR(4) COMMENT '利息调整方式',
  renew_rate_adjust_method VARCHAR(4) COMMENT '转存利率调整方式',
  process_status VARCHAR(4) COMMENT '处理状态',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户续存定义表';

-- 12. 余额发生明细表
DROP TABLE IF EXISTS kdaf_yefsmx;
CREATE TABLE kdaf_yefsmx (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  customer_account_type VARCHAR(10) COMMENT '客户账号类型',
  balance_field_name VARCHAR(50) COMMENT '余额字段名称',
  detail_seq INT COMMENT '明细序号',
  debit_credit_flag VARCHAR(4) COMMENT '借贷标志',
  currency_code VARCHAR(6) COMMENT '交易币种',
  transaction_amount DECIMAL(18,2) COMMENT '交易金额',
  account_balance DECIMAL(18,2) COMMENT '账户余额',
  customer_account VARCHAR(30) COMMENT '客户账号',
  sub_account_seq INT COMMENT '子账户序号',
  product_code VARCHAR(20) COMMENT '产品编号',
  customer_type VARCHAR(10) COMMENT '产品所属对象',
  counter_customer_account VARCHAR(30) COMMENT '对方客户账号',
  counter_account_no VARCHAR(30) COMMENT '对方负债系统账号',
  reversal_flag VARCHAR(4) COMMENT '冲正标志',
  global_serial_no VARCHAR(50) COMMENT '全局流水',
  summary_code VARCHAR(20) COMMENT '摘要代码',
  summary_desc VARCHAR(200) COMMENT '摘要描述',
  channel VARCHAR(20) COMMENT '交易渠道',
  external_txn_code VARCHAR(20) COMMENT '外部交易码',
  internal_txn_code VARCHAR(20) COMMENT '内部交易码',
  teller_serial_no VARCHAR(50) COMMENT '柜员流水号',
  txn_institution VARCHAR(20) COMMENT '交易营业机构',
  txn_date DATE COMMENT '交易日期',
  txn_time TIME COMMENT '交易时间',
  operator VARCHAR(20) COMMENT '操作柜员',
  remark VARCHAR(500) COMMENT '备注',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no),
  INDEX idx_txn_date (txn_date),
  INDEX idx_global_serial_no (global_serial_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='余额发生明细表';

-- 13. 账户白名单登记簿
DROP TABLE IF EXISTS kdaf_bmd;
CREATE TABLE kdaf_bmd (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  seq_no INT COMMENT '序号',
  customer_account VARCHAR(30) COMMENT '客户账号',
  account_no VARCHAR(30) COMMENT '负债账号',
  counter_fin_institution_name VARCHAR(200) COMMENT '对方金融机构名称',
  counter_fin_institution_code VARCHAR(50) COMMENT '对方金融机构代码',
  counter_name VARCHAR(200) COMMENT '对方户名',
  counter_account VARCHAR(30) COMMENT '对方账号',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户白名单登记簿';

-- 14. 账户冻结登记簿
DROP TABLE IF EXISTS kdaf_dongj;
CREATE TABLE kdaf_dongj (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  freeze_no VARCHAR(30) NOT NULL COMMENT '冻结编号',
  freeze_operation_flag VARCHAR(4) COMMENT '冻结操作标志',
  seq_no INT COMMENT '顺序号',
  customer_account VARCHAR(30) COMMENT '客户账号',
  account_no VARCHAR(30) COMMENT '负债账号',
  freeze_scope VARCHAR(10) COMMENT '冻结范围',
  interest_flag VARCHAR(4) COMMENT '计息标志',
  freeze_source VARCHAR(20) COMMENT '冻结来源',
  freeze_type VARCHAR(10) COMMENT '冻结种类',
  required_freeze_amount DECIMAL(18,2) COMMENT '需冻结金额',
  current_freeze_amount DECIMAL(18,2) COMMENT '现冻结金额',
  cumulative_freeze_amount DECIMAL(18,2) COMMENT '累计冻结金额',
  excess_freeze_flag VARCHAR(4) COMMENT '是否允许超额冻结',
  freeze_level VARCHAR(4) COMMENT '冻结级别',
  freeze_start_date DATE COMMENT '冻结起始日期',
  freeze_end_date DATE COMMENT '冻结终止日期',
  unfreeze_flag VARCHAR(4) COMMENT '解冻标志',
  renew_freeze_seq INT COMMENT '续冻序号',
  deducted_amount DECIMAL(18,2) COMMENT '已扣划金额',
  freeze_notice_no VARCHAR(50) COMMENT '冻结通知书编号',
  freeze_doc_type VARCHAR(20) COMMENT '冻结证明文书类别',
  freeze_reason VARCHAR(200) COMMENT '冻结原因',
  law_dept VARCHAR(100) COMMENT '执法部门',
  law_dept_name VARCHAR(200) COMMENT '执法部门名称',
  txn_institution VARCHAR(20) COMMENT '交易机构',
  operator VARCHAR(20) COMMENT '经办人',
  reviewer VARCHAR(20) COMMENT '复核人',
  approver VARCHAR(20) COMMENT '审批人',
  freeze_date DATE COMMENT '冻结日期',
  freeze_time TIME COMMENT '冻结时间',
  teller_serial_no VARCHAR(50) COMMENT '柜员流水号',
  channel VARCHAR(20) COMMENT '渠道',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_account_no (account_no),
  INDEX idx_freeze_no (freeze_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户冻结登记簿';

-- 15. 账户冻结明细表
DROP TABLE IF EXISTS kdaf_djmx;
CREATE TABLE kdaf_djmx (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  freeze_no VARCHAR(30) NOT NULL COMMENT '冻结编号',
  account_no VARCHAR(30) COMMENT '负债账号',
  seq_no INT COMMENT '顺序号',
  freeze_level VARCHAR(4) COMMENT '冻结级别',
  freeze_scope VARCHAR(10) COMMENT '冻结范围',
  interest_flag VARCHAR(4) COMMENT '计息标志',
  restrict_type VARCHAR(10) COMMENT '限制类型',
  required_freeze_amount DECIMAL(18,2) COMMENT '需冻结金额',
  current_freeze_amount DECIMAL(18,2) COMMENT '现冻结金额',
  cumulative_freeze_amount DECIMAL(18,2) COMMENT '累计冻结金额',
  freeze_start_date DATE COMMENT '冻结起始日期',
  freeze_end_date DATE COMMENT '冻结终止日期',
  queue_seq INT COMMENT '轮候序号',
  unfreeze_flag VARCHAR(4) COMMENT '解冻标志',
  renew_freeze_seq INT COMMENT '续冻序号',
  deducted_amount DECIMAL(18,2) COMMENT '已扣划金额',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_freeze_no (freeze_no),
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户冻结明细表';

-- 16. 账户解冻登记簿
DROP TABLE IF EXISTS kdaf_jiedj;
CREATE TABLE kdaf_jiedj (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  freeze_no VARCHAR(30) NOT NULL COMMENT '冻结编号',
  account_no VARCHAR(30) COMMENT '负债账号',
  renew_freeze_seq INT COMMENT '续冻序号',
  freeze_operation_flag VARCHAR(4) COMMENT '冻结操作标志',
  unfreeze_amount DECIMAL(18,2) COMMENT '解冻金额',
  unfreeze_notice_no VARCHAR(50) COMMENT '解冻通知书编号',
  unfreeze_reason VARCHAR(200) COMMENT '解冻原因',
  law_dept VARCHAR(100) COMMENT '执法部门',
  law_dept_name VARCHAR(200) COMMENT '执法部门名称',
  txn_institution VARCHAR(20) COMMENT '交易机构',
  operator VARCHAR(20) COMMENT '经办人',
  reviewer VARCHAR(20) COMMENT '复核人',
  approver VARCHAR(20) COMMENT '审批人',
  txn_date DATE COMMENT '交易日期',
  txn_time TIME COMMENT '交易时间',
  teller_serial_no VARCHAR(50) COMMENT '柜员流水号',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_freeze_no (freeze_no),
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户解冻登记簿';

-- 17. 账户扣划登记簿
DROP TABLE IF EXISTS kdaf_kouhdj;
CREATE TABLE kdaf_kouhdj (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  deduction_no VARCHAR(30) NOT NULL COMMENT '扣划编号',
  deduction_method VARCHAR(4) COMMENT '扣划方式',
  freeze_no VARCHAR(30) COMMENT '冻结编号',
  customer_account VARCHAR(30) COMMENT '客户账号',
  account_no VARCHAR(30) COMMENT '负债账号',
  deduction_amount DECIMAL(18,2) COMMENT '扣划金额',
  payee_customer_account VARCHAR(30) COMMENT '收款人客户账号',
  law_dept VARCHAR(100) COMMENT '执法部门',
  law_dept_name VARCHAR(200) COMMENT '扣划部门名称',
  deduction_doc_no VARCHAR(50) COMMENT '扣划文书号',
  summary_desc VARCHAR(200) COMMENT '摘要描述',
  txn_institution VARCHAR(20) COMMENT '交易机构',
  operator VARCHAR(20) COMMENT '经办人',
  reviewer VARCHAR(20) COMMENT '复核人',
  approver VARCHAR(20) COMMENT '审批人',
  txn_date DATE COMMENT '交易日期',
  txn_time TIME COMMENT '交易时间',
  teller_serial_no VARCHAR(50) COMMENT '柜员流水号',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_freeze_no (freeze_no),
  INDEX idx_account_no (account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户扣划登记簿';
