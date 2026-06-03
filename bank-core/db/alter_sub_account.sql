USE bank_core;
SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS acc_customer_sub_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_account_no VARCHAR(14) NOT NULL,
    sub_account_seq VARCHAR(6) NOT NULL,
    liability_account_no VARCHAR(8) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'NORMAL',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_customer_sub (customer_account_no, sub_account_seq),
    UNIQUE KEY uk_liability_account_no (liability_account_no),
    KEY idx_customer_account_no (customer_account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE acc_liability_account ADD COLUMN sub_account_seq VARCHAR(6) NOT NULL DEFAULT '001CNY' AFTER customer_account_no;
