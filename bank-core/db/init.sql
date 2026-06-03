CREATE DATABASE IF NOT EXISTS bank_core DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bank_core;
SET NAMES utf8mb4;

-- 客户表
CREATE TABLE crm_customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    customer_no VARCHAR(9) NOT NULL COMMENT '客户号',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户姓名',
    id_type VARCHAR(20) NOT NULL COMMENT '证件类型',
    id_number VARCHAR(30) NOT NULL COMMENT '证件号码',
    phone VARCHAR(20) COMMENT '手机号',
    address VARCHAR(200) COMMENT '地址',
    status VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-停用/2-注销',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_customer_no (customer_no),
    UNIQUE KEY uk_id_number (id_type, id_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 客户账号表
CREATE TABLE acc_customer_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    customer_account_no VARCHAR(14) NOT NULL COMMENT '客户账号',
    customer_no VARCHAR(9) NOT NULL COMMENT '客户号',
    account_type VARCHAR(20) NOT NULL DEFAULT 'PERSONAL' COMMENT '账号类型：PERSONAL/CORPORATE',
    status VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-已注销',
    open_date DATETIME NOT NULL COMMENT '开户日期',
    close_date DATETIME COMMENT '销户日期',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_customer_account_no (customer_account_no),
    KEY idx_customer_no (customer_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户账号表';

-- 客户账号对照表（客户账号 + 子账户序号 → 负债账号）
CREATE TABLE acc_customer_sub_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    customer_account_no VARCHAR(14) NOT NULL COMMENT '客户账号',
    sub_account_seq VARCHAR(6) NOT NULL COMMENT '子账户序号',
    liability_account_no VARCHAR(8) NOT NULL COMMENT '负债账号',
    account_type VARCHAR(20) NOT NULL COMMENT '账户类型：DEMAND（活期）/TIME（定期）',
    status VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-已注销',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_customer_sub (customer_account_no, sub_account_seq),
    UNIQUE KEY uk_liability_account_no (liability_account_no),
    KEY idx_customer_account_no (customer_account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户账号对照表';

-- 负债账号表
CREATE TABLE acc_liability_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    liability_account_no VARCHAR(8) NOT NULL COMMENT '负债账号',
    customer_account_no VARCHAR(14) NOT NULL COMMENT '客户账号',
    sub_account_seq VARCHAR(6) NOT NULL COMMENT '子账户序号',
    account_type VARCHAR(20) NOT NULL DEFAULT 'DEMAND' COMMENT '负债类型：DEMAND（活期）/TIME（定期）',
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00 COMMENT '余额',
    status VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-停用/2-已注销',
    open_date DATETIME NOT NULL COMMENT '开户日期',
    close_date DATETIME COMMENT '销户日期',
    version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_liability_account_no (liability_account_no),
    KEY idx_customer_account_no (customer_account_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='负债账号表';

-- 交易流水表
CREATE TABLE acc_liability_transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    transaction_no VARCHAR(30) NOT NULL COMMENT '交易流水号',
    liability_account_no VARCHAR(8) NOT NULL COMMENT '负债账号',
    transaction_type VARCHAR(30) NOT NULL COMMENT '交易类型',
    amount DECIMAL(15,2) NOT NULL COMMENT '交易金额',
    balance_before DECIMAL(15,2) NOT NULL COMMENT '交易前余额',
    balance_after DECIMAL(15,2) NOT NULL COMMENT '交易后余额',
    operate_time DATETIME NOT NULL COMMENT '操作时间',
    operator_no VARCHAR(20) NOT NULL COMMENT '操作员编号',
    remark VARCHAR(200) COMMENT '备注',
    related_transaction_no VARCHAR(30) COMMENT '关联原交易（用于撤销）',
    status VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-已冲正',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_transaction_no (transaction_no),
    KEY idx_liability_account_no (liability_account_no),
    KEY idx_related_transaction_no (related_transaction_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易流水表';

-- 机构表
CREATE TABLE sys_institution (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    institution_no VARCHAR(20) NOT NULL COMMENT '机构编号',
    institution_name VARCHAR(100) NOT NULL COMMENT '机构名称',
    institution_level VARCHAR(20) NOT NULL DEFAULT 'BRANCH' COMMENT '机构级别：HEADQUARTERS/BRANCH/SUBBRANCH',
    parent_institution_no VARCHAR(20) COMMENT '上级机构编号',
    status VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-停用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_institution_no (institution_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- 柜员表
CREATE TABLE sys_teller (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    teller_no VARCHAR(20) NOT NULL COMMENT '柜员编号',
    teller_name VARCHAR(50) NOT NULL COMMENT '柜员姓名',
    institution_no VARCHAR(20) NOT NULL COMMENT '所属机构',
    teller_type VARCHAR(20) NOT NULL COMMENT '柜员类型：0-普通柜员/1-库管柜员',
    password VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密）',
    status VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-停用/2-离职',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_teller_no (teller_no),
    KEY idx_institution_no (institution_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='柜员表';