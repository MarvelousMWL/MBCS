-- ============================================
-- MBCS 批处理新增表
-- 1. kdpl_zhlxmx - 账户利息明细表
-- 2. kdpb_batch_run_log - 批处理运行日志表
-- ============================================

-- 1. 账户利息明细表
DROP TABLE IF EXISTS kdpl_zhlxmx;
CREATE TABLE kdpl_zhlxmx (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  batch_no VARCHAR(30) NOT NULL COMMENT '批次号',
  account_no VARCHAR(30) NOT NULL COMMENT '负债账号',
  product_code VARCHAR(20) COMMENT '产品编号',
  calc_date DATE NOT NULL COMMENT '计息日期（上日）',
  balance_type VARCHAR(10) NOT NULL COMMENT '余额类型：LAST_DAY/CURRENT',
  calc_balance DECIMAL(18,2) NOT NULL COMMENT '计息本金',
  interest_rate DECIMAL(9,6) NOT NULL COMMENT '年利率(%，如0.35=0.35%)',
  daily_interest DECIMAL(18,2) NOT NULL COMMENT '当日利息',
  accrued_interest DECIMAL(18,2) DEFAULT 0.00 COMMENT '累计未结利息',
  batch_type VARCHAR(20) NOT NULL COMMENT '批次类型：DAILY_ACCRUAL/SETTLEMENT',
  status VARCHAR(20) NOT NULL DEFAULT 'ACCRUED' COMMENT '状态：ACCRUED/SETTLED/FAILED',
  error_message VARCHAR(500) COMMENT '失败原因',
  version INT DEFAULT 0,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_batch_no (batch_no),
  INDEX idx_account_no (account_no),
  INDEX idx_calc_date (calc_date),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户利息明细表';

-- 2. 批处理运行日志表
DROP TABLE IF EXISTS kdpb_batch_run_log;
CREATE TABLE kdpb_batch_run_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  batch_no VARCHAR(30) NOT NULL COMMENT '批次号',
  batch_type VARCHAR(30) NOT NULL COMMENT '批次类型：DAILY_ACCRUAL/SETTLEMENT/REDEMPTION/OVERDUE',
  system_date DATE NOT NULL COMMENT '系统日期（换日后）',
  calc_date DATE NOT NULL COMMENT '业务日期（上日）',
  total_accounts INT DEFAULT 0 COMMENT '总处理账户数',
  success_count INT DEFAULT 0,
  fail_count INT DEFAULT 0,
  start_time DATETIME NOT NULL,
  end_time DATETIME,
  status VARCHAR(20) DEFAULT 'RUNNING' COMMENT 'RUNNING/COMPLETED/COMPLETED_WITH_ERRORS/FAILED',
  error_message TEXT COMMENT '整体错误信息',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_batch_no (batch_no),
  INDEX idx_batch_type_calc_date (batch_type, calc_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批处理运行日志表';
