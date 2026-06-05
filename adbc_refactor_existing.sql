DROP TABLE IF EXISTS kdpf_chpshx_old;
DROP TABLE IF EXISTS kdpf_chpshx;
DROP TABLE IF EXISTS kdpf_kaihkz_old;
DROP TABLE IF EXISTS kdpf_kaihkz;
DROP TABLE IF EXISTS kdpf_zhiqkz_old;
DROP TABLE IF EXISTS kdpf_zhiqkz;
DROP TABLE IF EXISTS kdpf_daoqkz_old;
DROP TABLE IF EXISTS kdpf_daoqkz;

-- 产品基础属性表 (40字段)
CREATE TABLE kdpf_chpshx (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL UNIQUE COMMENT '产品编号',
  product_description VARCHAR(200) COMMENT '产品说明',
  marketing_description VARCHAR(200) COMMENT '产品营销说明',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expiry_date DATE NOT NULL DEFAULT '2999-12-31' COMMENT '负债产品失效日',
  current_fixed_flag VARCHAR(4) NOT NULL DEFAULT '0' COMMENT '产品定活标志 0-活期 1-定期',
  customer_type VARCHAR(20) NOT NULL DEFAULT '2' COMMENT '产品所属对象 1-对私 2-对公 3-同业',
  product_type VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '产品类型 0-传统 1-拓展',
  default_currency VARCHAR(6) NOT NULL DEFAULT '156' COMMENT '产品默认币种',
  cash_exchange_flag VARCHAR(4) DEFAULT '0' COMMENT '现金通兑标志 0-否 1-是',
  transfer_exchange_flag VARCHAR(4) DEFAULT '0' COMMENT '转账通兑标志 0-否 1-是',
  withdraw_scope VARCHAR(20) DEFAULT '0' COMMENT '通兑范围 0-全行 1-分行 2-开户机构',
  deposit_scope VARCHAR(20) DEFAULT '0' COMMENT '通存范围 0-全行 1-分行 2-开户机构',
  channel_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '渠道控制标志',
  currency_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '币种控制标志',
  institution_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '机构控制标志',
  customer_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '客户控制标志',
  account_classify_flag VARCHAR(4) DEFAULT '0' COMMENT '账户分类标志',
  voucher_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '凭证控制标志',
  term_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '存期控制方式',
  default_account_type VARCHAR(10) COMMENT '默认账户类型',
  product_status VARCHAR(4) NOT NULL DEFAULT '0' COMMENT '产品状态',
  maturity_flag VARCHAR(4) DEFAULT '0' COMMENT '是否到期定义',
  overdraft_flag VARCHAR(4) DEFAULT '0' COMMENT '是否透支定义',
  charge_flag VARCHAR(4) DEFAULT '0' COMMENT '是否收费定义',
  simple_interest_flag VARCHAR(4) DEFAULT '0' COMMENT '是否简单计息',
  settlement_flag VARCHAR(4) DEFAULT '0' COMMENT '是否结算户',
  auto_exchange_flag VARCHAR(4) DEFAULT '0' COMMENT '自动结汇标志',
  auto_exchange_sell_flag VARCHAR(4) DEFAULT '0' COMMENT '自动售汇标志',
  exchange_flag VARCHAR(4) DEFAULT '0' COMMENT '允许结汇标志',
  balance_sync_flag VARCHAR(4) DEFAULT '1' COMMENT '余额与总账同步标志',
  form_transfer_flag VARCHAR(4) DEFAULT '0' COMMENT '是否形态转移定义',
  total_limit DECIMAL(18,2) DEFAULT 0 COMMENT '总额度',
  registered_flag VARCHAR(4) DEFAULT '0' COMMENT '记名标志',
  product_switch_flag VARCHAR(4) DEFAULT '0' COMMENT '产品转换标志',
  cost_center VARCHAR(20) COMMENT '成本中心',
  deposit_type VARCHAR(4) NOT NULL COMMENT '存款种类',
  conversion_currency VARCHAR(6) COMMENT '换算币种',
  product_designer VARCHAR(50) COMMENT '产品设计方',
  product_manager VARCHAR(50) COMMENT '产品管理方',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品基础属性表';

-- 产品开户控制表 (26字段)
CREATE TABLE kdpf_kaihkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL COMMENT '产品编号',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  specify_account_rule VARCHAR(4) DEFAULT '0' COMMENT '指定账号生成规则',
  account_gen_rule VARCHAR(20) DEFAULT 'DPSACT' COMMENT '账号生成规则',
  account_seq_rule VARCHAR(20) COMMENT '账号序号生成规则',
  open_restrict_flag VARCHAR(4) DEFAULT '0' COMMENT '是否开户限制',
  restrict_type VARCHAR(10) COMMENT '限制种类',
  restrict_period VARCHAR(10) COMMENT '限制期限',
  maturity_determine_method VARCHAR(4) DEFAULT '0' COMMENT '到期日确定方式',
  interest_start_method VARCHAR(4) DEFAULT '0' COMMENT '起息方式',
  early_interest_days INT DEFAULT 0 COMMENT '早起息控制天数',
  late_interest_days INT DEFAULT 0 COMMENT '晚起息控制天数',
  voucher_type VARCHAR(20) COMMENT '凭证种类',
  fund_source VARCHAR(4) DEFAULT '0' COMMENT '开户资金来源',
  transfer_account_nature VARCHAR(4) DEFAULT '0' COMMENT '转账账户性质',
  specify_customer_account_rule VARCHAR(4) DEFAULT '1' COMMENT '指定客户账号生成规则',
  customer_account_rule VARCHAR(20) DEFAULT 'CUACNO' COMMENT '客户账号生成规则',
  customer_open_limit_flag VARCHAR(4) COMMENT '是否客户号开立限制数量',
  open_limit_type VARCHAR(10) DEFAULT '2' COMMENT '开立数量限制类型',
  max_open_quantity INT DEFAULT 0 COMMENT '产品最大可开立数量',
  exchange_flag VARCHAR(4) COMMENT '允许结汇标志',
  force_voucher_flag VARCHAR(4) COMMENT '是否开户强制关联凭证',
  prepaid_interest_flag VARCHAR(4) DEFAULT '0' COMMENT '预付息标志',
  prepaid_interest_code VARCHAR(20) COMMENT '预付息代码',
  penalty_interest_flag VARCHAR(4) DEFAULT '0' COMMENT '罚息标志',
  penalty_interest_code VARCHAR(20) COMMENT '罚息代码',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品开户控制表';

-- 产品支取控制表 (24字段)
CREATE TABLE kdpf_zhiqkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL COMMENT '产品编号',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  cash_withdraw_flag VARCHAR(4) DEFAULT '1' COMMENT '现金支取标志',
  transfer_withdraw_flag VARCHAR(4) DEFAULT '1' COMMENT '转账支取标志',
  withdraw_ctrl_mode VARCHAR(4) DEFAULT '1' COMMENT '支取控制方式',
  withdraw_ctrl_method VARCHAR(4) DEFAULT '0' COMMENT '支取控制方法',
  custom_ctrl_method VARCHAR(20) COMMENT '自定义控制方式',
  withdraw_appointment VARCHAR(20) COMMENT '支取预约方式',
  withdraw_protect_order INT COMMENT '支取保护顺序',
  protect_nature VARCHAR(4) COMMENT '保护性质',
  amount_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '支取金额控制方式',
  single_min_amount DECIMAL(18,2) DEFAULT 0 COMMENT '单次支取最小金额',
  single_max_amount DECIMAL(18,2) DEFAULT 0 COMMENT '单次支取最大金额',
  times_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '支取次数控制方式',
  min_withdraw_times INT DEFAULT 0 COMMENT '最小支取次数',
  max_withdraw_times INT DEFAULT 0 COMMENT '最大支取次数',
  withdraw_plan_flag VARCHAR(4) DEFAULT '0' COMMENT '是否设置支取计划',
  plan_adjust_cycle_method VARCHAR(20) COMMENT '支取计划调整周期方式',
  plan_adjust_cycle VARCHAR(20) COMMENT '支取计划调整周期',
  plan_end_date_method VARCHAR(20) COMMENT '支取计划结束日期方式',
  plan_ctrl_mode VARCHAR(20) COMMENT '支取计划控制方式',
  default_standard VARCHAR(20) COMMENT '支取违约标准',
  default_handling_method VARCHAR(20) COMMENT '支取违约处理方式',
  retain_min_balance DECIMAL(18,2) DEFAULT 0 COMMENT '账户留存最小余额',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品支取控制表';

-- 产品到期控制表 (10字段)
CREATE TABLE kdpf_daoqkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL COMMENT '产品编号',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  holiday_process_method VARCHAR(4) DEFAULT '0' COMMENT '遇节假日处理方式',
  deposit_delay_flag VARCHAR(4) DEFAULT '0' COMMENT '是否根据存款顺延到期日',
  maturity_grace_days INT COMMENT '到期宽限期',
  renew_flag VARCHAR(4) DEFAULT '1' COMMENT '允许转存标志',
  change_renew_product_flag VARCHAR(4) DEFAULT '0' COMMENT '可以更换转存产品号',
  renew_product_code VARCHAR(20) COMMENT '转存产品',
  renew_rate_adjust_method VARCHAR(4) DEFAULT '0' COMMENT '转存利率调整方式',
  interest_settlement_method VARCHAR(4) DEFAULT '0' COMMENT '到期利息结息方式',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品到期控制表';
