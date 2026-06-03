CREATE TABLE IF NOT EXISTS kdpf_chpshx (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(20) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    effective_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    product_status VARCHAR(4) NOT NULL DEFAULT '0',
    current_fixed_flag VARCHAR(4) NOT NULL,
    customer_type VARCHAR(20) NOT NULL,
    product_type VARCHAR(20) NOT NULL DEFAULT 'TRADITIONAL',
    deposit_type VARCHAR(4) NOT NULL,
    default_currency VARCHAR(6) NOT NULL DEFAULT '156',
    withdraw_scope VARCHAR(20) DEFAULT 'BRANCH',
    deposit_scope VARCHAR(20) DEFAULT 'ALL',
    cash_withdrawal_flag VARCHAR(4) DEFAULT '0',
    tt_withdrawal_flag VARCHAR(4) DEFAULT '0',
    maturity_flag VARCHAR(4) DEFAULT '0',
    overdraft_flag VARCHAR(4) DEFAULT '0',
    charge_flag VARCHAR(4) DEFAULT '0',
    account_classify_flag VARCHAR(4) DEFAULT '0',
    channel_ctrl_mode VARCHAR(4) DEFAULT '0',
    currency_ctrl_mode VARCHAR(4) DEFAULT '0',
    institution_ctrl_mode VARCHAR(4) DEFAULT '0',
    customer_ctrl_mode VARCHAR(4) DEFAULT '0',
    voucher_ctrl_mode VARCHAR(4) DEFAULT '0',
    term_ctrl_mode VARCHAR(4) DEFAULT '0',
    simple_interest_flag VARCHAR(4) DEFAULT '0',
    settlement_flag VARCHAR(4) DEFAULT '0',
    exchange_flag VARCHAR(4) DEFAULT '0',
    exchange_sell_flag VARCHAR(4) DEFAULT '0',
    balance_sync_flag VARCHAR(4) DEFAULT '1',
    form_transfer_flag VARCHAR(4) DEFAULT '0',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS kdpf_cpdxkz (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(20) NOT NULL,
    control_object VARCHAR(20) NOT NULL,
    currency_rule VARCHAR(4) NOT NULL DEFAULT '1',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY idx_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO kdpf_chpshx (product_code, product_name, effective_date, expiry_date, product_status, current_fixed_flag, customer_type, deposit_type, withdraw_scope, deposit_scope, cash_withdrawal_flag, maturity_flag, settlement_flag, balance_sync_flag) VALUES
('DP001', '??????', '2024-01-01', '2099-12-31', '0', '0', 'CORPORATE', '00', 'BRANCH', 'ALL', '1', '0', '1', '1'),
('DP002', '??????', '2024-01-01', '2099-12-31', '0', '1', 'CORPORATE', '02', 'BRANCH', 'ALL', '0', '1', '0', '1'),
('DP003', '??????', '2024-01-01', '2099-12-31', '0', '0', 'CORPORATE', '05', 'BRANCH', 'ALL', '0', '0', '0', '0'),
('DP004', '???????', '2024-01-01', '2099-12-31', '0', '0', 'CORPORATE', '06', 'BRANCH', 'ALL', '0', '0', '1', '1'),
('DP005', '??????', '2024-01-01', '2099-12-31', '0', '0', 'TRADE', '14', 'BRANCH', 'ALL', '0', '0', '1', '1');
