-- ============================================
-- MBCS CI 产品工厂表初始化
-- 全量建表，匹配 产品初始化数据.sql 的列名
-- ============================================

-- 产品基础属性表 (40字段)
DROP TABLE IF EXISTS kdpf_chpshx;
CREATE TABLE kdpf_chpshx (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL UNIQUE COMMENT '产品编号',
  product_description VARCHAR(200) COMMENT '产品说明',
  marketing_description VARCHAR(200) COMMENT '产品营销说明',
  effective_date DATE NOT NULL COMMENT '生效日期',
  expiry_date DATE NOT NULL DEFAULT '2999-12-31' COMMENT '负债产品失效日',
  current_fixed_flag VARCHAR(4) NOT NULL DEFAULT '0' COMMENT '产品定活标志',
  customer_type VARCHAR(20) NOT NULL DEFAULT '2' COMMENT '产品所属对象',
  product_type VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '产品类型',
  default_currency VARCHAR(6) NOT NULL DEFAULT '156' COMMENT '产品默认币种',
  cash_exchange_flag VARCHAR(4) DEFAULT '0' COMMENT '现金通兑标志',
  transfer_exchange_flag VARCHAR(4) DEFAULT '0' COMMENT '转账通兑标志',
  withdraw_scope VARCHAR(20) DEFAULT '0' COMMENT '通兑范围',
  deposit_scope VARCHAR(20) DEFAULT '0' COMMENT '通存范围',
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
  exchange_flag VARCHAR(4) DEFAULT '1' COMMENT '通兑标志',
  balance_sync_flag VARCHAR(4) DEFAULT '1' COMMENT '余额同步标志',
  form_transfer_flag VARCHAR(4) DEFAULT '0' COMMENT '形态转移标志',
  total_limit DECIMAL(18,2) DEFAULT 0.00 COMMENT '总限额',
  registered_flag VARCHAR(4) DEFAULT '0' COMMENT '注册标志',
  product_switch_flag VARCHAR(4) DEFAULT '0' COMMENT '产品开关标志',
  cost_center VARCHAR(20) COMMENT '成本中心',
  deposit_type VARCHAR(4) COMMENT '存款类型',
  conversion_currency VARCHAR(6) COMMENT '换算币种',
  product_designer VARCHAR(50) COMMENT '产品设计人',
  product_manager VARCHAR(50) COMMENT '产品经理',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品对象控制表
DROP TABLE IF EXISTS kdpf_cpdxkz;
CREATE TABLE kdpf_cpdxkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品开户控制表
DROP TABLE IF EXISTS kdpf_kaihkz;
CREATE TABLE kdpf_kaihkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  specify_account_rule VARCHAR(4),
  account_gen_rule VARCHAR(10),
  account_seq_rule VARCHAR(4),
  open_restrict_flag VARCHAR(4),
  restrict_type VARCHAR(4),
  restrict_period VARCHAR(20),
  maturity_determine_method VARCHAR(4),
  interest_start_method VARCHAR(4),
  early_interest_days INT,
  late_interest_days INT,
  voucher_type VARCHAR(4),
  fund_source VARCHAR(20),
  transfer_account_nature VARCHAR(4),
  specify_customer_account_rule VARCHAR(4),
  customer_account_rule VARCHAR(4),
  customer_open_limit_flag VARCHAR(4),
  open_limit_type VARCHAR(4),
  max_open_quantity INT,
  exchange_flag VARCHAR(4),
  force_voucher_flag VARCHAR(4),
  prepaid_interest_flag VARCHAR(4),
  prepaid_interest_code VARCHAR(20),
  penalty_interest_flag VARCHAR(4),
  penalty_interest_code VARCHAR(20),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品销户控制表
DROP TABLE IF EXISTS kdpf_xiohkz;
CREATE TABLE kdpf_xiohkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  close_ctrl_method VARCHAR(4),
  custom_ctrl_method VARCHAR(4),
  early_close_ctrl VARCHAR(4),
  penalty_type VARCHAR(4),
  sign_check_method VARCHAR(4),
  arrears_check_method VARCHAR(4),
  close_fund_to VARCHAR(20),
  transfer_account_nature VARCHAR(4),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品存入控制表
DROP TABLE IF EXISTS kdpf_cunrkz;
CREATE TABLE kdpf_cunrkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  cash_deposit_flag VARCHAR(4),
  transfer_deposit_flag VARCHAR(4),
  deposit_ctrl_mode VARCHAR(4),
  deposit_ctrl_method VARCHAR(4),
  amount_ctrl_mode VARCHAR(4),
  single_min_amount DECIMAL(18,2),
  single_max_amount DECIMAL(18,2),
  times_ctrl_mode VARCHAR(4),
  min_deposit_times INT,
  max_deposit_times INT,
  deposit_plan_flag VARCHAR(4),
  plan_adjust_method VARCHAR(4),
  plan_adjust_cycle VARCHAR(20),
  plan_end_date_method VARCHAR(4),
  plan_gen_method VARCHAR(4),
  miss_deposit_grace_days INT,
  miss_deposit_supplement VARCHAR(4),
  max_supplement_times INT,
  deposit_default_std DECIMAL(18,2),
  miss_deposit_times INT,
  default_handling_method VARCHAR(4),
  plan_ctrl_mode VARCHAR(4),
  first_min_amount DECIMAL(18,2),
  first_increment DECIMAL(18,2),
  retain_max_balance DECIMAL(18,2),
  deposit_frequency VARCHAR(20),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品支取控制表
DROP TABLE IF EXISTS kdpf_zhiqkz;
CREATE TABLE kdpf_zhiqkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  cash_withdraw_flag VARCHAR(4),
  transfer_withdraw_flag VARCHAR(4),
  withdraw_ctrl_mode VARCHAR(4),
  withdraw_ctrl_method VARCHAR(4),
  custom_ctrl_method VARCHAR(4),
  withdraw_appointment VARCHAR(4),
  withdraw_protect_order VARCHAR(4),
  protect_nature VARCHAR(4),
  amount_ctrl_mode VARCHAR(4),
  single_min_amount DECIMAL(18,2),
  single_max_amount DECIMAL(18,2),
  times_ctrl_mode VARCHAR(4),
  min_withdraw_times INT,
  max_withdraw_times INT,
  withdraw_plan_flag VARCHAR(4),
  plan_adjust_cycle_method VARCHAR(4),
  plan_adjust_cycle VARCHAR(20),
  plan_end_date_method VARCHAR(4),
  plan_ctrl_mode VARCHAR(4),
  default_standard DECIMAL(18,2),
  default_handling_method VARCHAR(4),
  retain_min_balance DECIMAL(18,2),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品利息定义表
DROP TABLE IF EXISTS kdpf_lixidy;
CREATE TABLE kdpf_lixidy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  interest_type VARCHAR(4),
  interest_flag VARCHAR(4),
  interest_balance_flag VARCHAR(4),
  interest_rule_code VARCHAR(20),
  tax_flag VARCHAR(4),
  min_interest_amount DECIMAL(18,2),
  max_interest_amount DECIMAL(18,2),
  interest_adjust_method VARCHAR(4),
  standard_interest_method VARCHAR(4),
  interest_payment_method VARCHAR(4),
  interest_start_method VARCHAR(4),
  interest_frequency VARCHAR(20),
  payment_frequency VARCHAR(20),
  rate_plan_flag VARCHAR(4),
  date_end_method VARCHAR(4),
  interest_basis VARCHAR(4),
  accrual_frequency VARCHAR(20),
  avg_cycle_days_method VARCHAR(4),
  avg_balance_cycle_type VARCHAR(4),
  avg_balance_method VARCHAR(4),
  specify_term VARCHAR(20),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品利率定义表
DROP TABLE IF EXISTS kdpf_lilvdy;
CREATE TABLE kdpf_lilvdy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  liability_rate_type VARCHAR(4),
  rate_gear_method VARCHAR(4),
  rate_code_type VARCHAR(20),
  rate_term VARCHAR(20),
  rate_term_flag VARCHAR(4),
  rate_balance_flag VARCHAR(4),
  rate_determine_date VARCHAR(4),
  rate_determine_method VARCHAR(4),
  rate_adjust_frequency VARCHAR(20),
  rate_change_adjust_rate VARCHAR(4),
  rate_change_adjust_int VARCHAR(4),
  discount_adjust_frequency VARCHAR(20),
  discount_change_flag VARCHAR(4),
  avg_balance_type VARCHAR(4),
  specify_term VARCHAR(20),
  rate_update_method VARCHAR(4),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品到期控制表
DROP TABLE IF EXISTS kdpf_daoqkz;
CREATE TABLE kdpf_daoqkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  holiday_process_method VARCHAR(4),
  deposit_delay_flag VARCHAR(4),
  maturity_grace_days INT,
  renew_flag VARCHAR(4),
  change_renew_product_flag VARCHAR(4),
  renew_product_code VARCHAR(20),
  renew_rate_adjust_method VARCHAR(4),
  interest_settlement_method VARCHAR(4),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品形态转移定义表
DROP TABLE IF EXISTS kdpf_xtaizy;
CREATE TABLE kdpf_xtaizy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  transfer_type VARCHAR(4),
  start_date_method VARCHAR(4),
  transfer_cycle VARCHAR(20),
  interest_flag VARCHAR(4),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品凭证控制表
DROP TABLE IF EXISTS kdpf_pngzkz;
CREATE TABLE kdpf_pngzkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  voucher_type VARCHAR(4),
  customer_account_type VARCHAR(4),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品核算控制表
DROP TABLE IF EXISTS kdpf_bizhkz;
CREATE TABLE kdpf_bizhkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品存期控制表
DROP TABLE IF EXISTS kdpf_cunqkz;
CREATE TABLE kdpf_cunqkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 产品账户分类控制表
DROP TABLE IF EXISTS kdpf_zhflkz;
CREATE TABLE kdpf_zhflkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL,
  currency_code VARCHAR(6) NOT NULL DEFAULT '156',
  account_classify1 VARCHAR(20),
  account_classify2 VARCHAR(20),
  account_classify3 VARCHAR(20),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
