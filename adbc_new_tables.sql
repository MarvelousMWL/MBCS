-- 产品销户控制表 (10字段) - 新增
DROP TABLE IF EXISTS kdpf_xiohkz;
CREATE TABLE kdpf_xiohkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL COMMENT '产品编号',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  close_ctrl_method VARCHAR(4) DEFAULT '0' COMMENT '销户控制方法 0-系统标准 1-自定义',
  custom_ctrl_method VARCHAR(20) COMMENT '自定义控制方式',
  early_close_ctrl VARCHAR(4) DEFAULT '0' COMMENT '提前销户控制方式',
  penalty_type VARCHAR(4) DEFAULT '0' COMMENT '处罚类型 0-新计息方法',
  sign_check_method VARCHAR(4) DEFAULT '1' COMMENT '签约检查方式',
  arrears_check_method VARCHAR(4) DEFAULT '1' COMMENT '欠费检查方式',
  close_fund_to VARCHAR(4) DEFAULT '0' COMMENT '销户资金去向 0-不控制 1-转账',
  transfer_account_nature VARCHAR(4) DEFAULT '0' COMMENT '转账账户性质',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品销户控制表';

-- 产品存入控制表 (29字段) - 新增
DROP TABLE IF EXISTS kdpf_cunrkz;
CREATE TABLE kdpf_cunrkz (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL COMMENT '产品编号',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  cash_deposit_flag VARCHAR(4) DEFAULT '1' COMMENT '现金存入标志',
  transfer_deposit_flag VARCHAR(4) DEFAULT '1' COMMENT '转账存入标志',
  deposit_ctrl_mode VARCHAR(4) DEFAULT '2' COMMENT '存入控制方式 1-无条件 2-有条件',
  deposit_ctrl_method VARCHAR(4) DEFAULT '0' COMMENT '存入控制方法 0-系统标准',
  amount_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '存入金额控制方式',
  single_min_amount DECIMAL(18,2) DEFAULT 0 COMMENT '单次存入最小金额',
  single_max_amount DECIMAL(18,2) DEFAULT 0 COMMENT '单次存入最大金额',
  times_ctrl_mode VARCHAR(4) DEFAULT '0' COMMENT '存入次数控制方式',
  min_deposit_times INT DEFAULT 0 COMMENT '最小存入次数',
  max_deposit_times INT DEFAULT 0 COMMENT '最大存入次数',
  deposit_plan_flag VARCHAR(4) DEFAULT '0' COMMENT '设置存入计划标志',
  plan_adjust_method VARCHAR(20) COMMENT '存入计划调整方式',
  plan_adjust_cycle VARCHAR(20) COMMENT '存入计划调整周期',
  plan_end_date_method VARCHAR(20) COMMENT '存入计划结束日期方式',
  plan_gen_method VARCHAR(20) COMMENT '存入计划生成方式',
  miss_deposit_grace_days INT COMMENT '漏存补足宽限期',
  miss_deposit_supplement VARCHAR(4) DEFAULT '0' COMMENT '存入漏补方式',
  max_supplement_times INT DEFAULT 0 COMMENT '最大补足次数',
  deposit_default_std VARCHAR(20) COMMENT '存入违约标准',
  miss_deposit_times INT COMMENT '漏存次数',
  default_handling_method VARCHAR(20) COMMENT '存入违约处理方式',
  plan_ctrl_mode VARCHAR(20) COMMENT '存入计划控制方式',
  deposit_process_order VARCHAR(4) DEFAULT '0' COMMENT '存入处理顺序',
  first_min_amount DECIMAL(18,2) DEFAULT 0 COMMENT '首次存入最小金额',
  first_increment DECIMAL(18,2) DEFAULT 0 COMMENT '首次存入金额增量',
  retain_max_balance DECIMAL(18,2) DEFAULT 0 COMMENT '账户留存最大余额',
  deposit_frequency VARCHAR(20) COMMENT '存入频率',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品存入控制表';

-- 产品利息定义表 (23字段) - 新增
DROP TABLE IF EXISTS kdpf_lixidy;
CREATE TABLE kdpf_lixidy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL COMMENT '产品编号',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  interest_type VARCHAR(4) DEFAULT '0' COMMENT '利息类型 0-正利息 1-到期利息',
  interest_flag VARCHAR(4) DEFAULT '0' COMMENT '计息标志 0-否 1-是',
  interest_balance_flag VARCHAR(4) DEFAULT '0' COMMENT '计息余额标志',
  interest_rule_code VARCHAR(20) COMMENT '计息规则代码',
  tax_flag VARCHAR(4) DEFAULT '0' COMMENT '计税标志',
  min_interest_amount DECIMAL(18,2) DEFAULT 1 COMMENT '最小计息金额',
  max_interest_amount DECIMAL(18,2) COMMENT '最大计息金额',
  interest_adjust_method VARCHAR(4) DEFAULT '0' COMMENT '利息调整方法 0-多段 1-积数',
  standard_interest_method VARCHAR(4) DEFAULT '10' COMMENT '标准计息方法',
  interest_payment_method VARCHAR(4) DEFAULT '0' COMMENT '利息支付方式 0-定期付息 1-利随本清',
  interest_start_method VARCHAR(4) DEFAULT '0' COMMENT '付息起始日确定方式',
  interest_frequency VARCHAR(10) DEFAULT 'D' COMMENT '计息频率',
  payment_frequency VARCHAR(20) DEFAULT '1QA21E' COMMENT '付息频率',
  rate_plan_flag VARCHAR(4) DEFAULT '0' COMMENT '设置利率计划标志',
  date_end_method VARCHAR(20) COMMENT '日期结束方式',
  interest_basis VARCHAR(20) COMMENT '计息依据',
  accrual_frequency VARCHAR(10) DEFAULT 'D' COMMENT '计提入账频率',
  avg_cycle_days_method VARCHAR(20) COMMENT '平均余额周期天数方式',
  avg_balance_cycle_type VARCHAR(20) COMMENT '平均余额周期类型',
  avg_balance_method VARCHAR(20) COMMENT '平均余额方式',
  specify_term VARCHAR(20) COMMENT '指定期限',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品利息定义表';

-- 产品利率定义表 (19字段) - 新增
DROP TABLE IF EXISTS kdpf_lilvdy;
CREATE TABLE kdpf_lilvdy (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_code VARCHAR(20) NOT NULL COMMENT '产品编号',
  currency_code VARCHAR(6) DEFAULT '156' COMMENT '货币代号',
  liability_rate_type VARCHAR(4) DEFAULT '0' COMMENT '负债利率类型 0-正利率 1-到期利率',
  rate_gear_method VARCHAR(4) DEFAULT '0' COMMENT '利率靠档方式',
  rate_code VARCHAR(20) COMMENT '利率编号',
  rate_code_type VARCHAR(20) COMMENT '利率编号类型',
  rate_term VARCHAR(10) DEFAULT 'D' COMMENT '利率存期',
  rate_term_flag VARCHAR(4) DEFAULT '0' COMMENT '利率存期标志',
  rate_balance_flag VARCHAR(4) COMMENT '利率余额标志',
  rate_determine_date VARCHAR(4) DEFAULT '0' COMMENT '利率确定日期 0-开户日 1-结息日',
  rate_determine_method VARCHAR(4) DEFAULT '0' COMMENT '利率确定方式',
  rate_adjust_frequency VARCHAR(10) DEFAULT 'D' COMMENT '利率调整频率',
  rate_change_adjust_rate VARCHAR(4) DEFAULT '0' COMMENT '利率变化调整利率标志',
  rate_change_adjust_int VARCHAR(4) DEFAULT '0' COMMENT '利率变化调整利息标志',
  discount_adjust_frequency VARCHAR(20) COMMENT '优惠调整频率',
  discount_change_flag VARCHAR(20) COMMENT '优惠变化调整优惠标志',
  avg_balance_type VARCHAR(20) COMMENT '平均余额类型',
  specify_term VARCHAR(20) COMMENT '指定期限',
  rate_update_method VARCHAR(4) DEFAULT '0' COMMENT '利率更新处理方式',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品利率定义表';
