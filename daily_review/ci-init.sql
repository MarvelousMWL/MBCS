-- ============================================
-- MBCS CI 数据库初始化（合并所有建表脚本）
-- 用于 GitHub Actions MySQL 服务容器
-- ============================================

-- 1. 基础表 + 柜员/机构
source bank-core/db/init.sql;
source bank-core/db/teller.sql;
source bank-core/db/teller_data.sql;

-- 2. 产品工厂表（ADBC参数表体系）
source adbc_refactor_existing.sql;
source adbc_new_tables.sql;
source bank-core/db/migration_product_factory.sql;

-- 3. 账户层表
source adbc_account_tables.sql;

-- 4. 产品初始化数据
source 产品初始化数据.sql;

-- 5. 其他表（子账户等）
source bank-core/db/alter_sub_account.sql;
