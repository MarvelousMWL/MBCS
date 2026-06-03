USE bank_core;

-- 机构表
CREATE TABLE IF NOT EXISTS sys_institution (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    institution_no VARCHAR(6) NOT NULL COMMENT '机构编号',
    institution_name VARCHAR(100) NOT NULL COMMENT '机构名称',
    institution_level VARCHAR(20) NOT NULL DEFAULT 'BRANCH' COMMENT '机构级别：HEADQUARTERS/BRANCH/SUBBRANCH',
    parent_institution_no VARCHAR(6) COMMENT '上级机构编号',
    status VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-停用',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_institution_no (institution_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- 柜员表
CREATE TABLE IF NOT EXISTS sys_teller (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    teller_no VARCHAR(10) NOT NULL COMMENT '柜员编号',
    teller_name VARCHAR(50) NOT NULL COMMENT '柜员姓名',
    institution_no VARCHAR(6) NOT NULL COMMENT '机构编号',
    teller_type VARCHAR(20) NOT NULL DEFAULT '0' COMMENT '柜员类型：0-普通柜员/1-库管柜员',
    password VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
    status VARCHAR(10) NOT NULL DEFAULT '0' COMMENT '状态：0-正常/1-停用/2-离职',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_teller_no (teller_no),
    KEY idx_institution_no (institution_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='柜员表';