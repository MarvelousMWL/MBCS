/**
 * MBCS 枚举字典
 * 将后端返回的 Integer 枚举码翻译为中文显示
 * 使用方式: {{ ENUM_TYPE[value] || value }}
 */

// 客户状态
export const CUSTOMER_STATUS = { 0: '正常', 1: '停用', 2: '销户' }

// 客户账号状态
export const CUSTOMER_ACCOUNT_STATUS = { 0: '正常', 1: '已销户' }

// 负债账户状态
export const LIABILITY_ACCOUNT_STATUS = { 0: '正常', 1: '停用', 2: '已销户', 3: '冻结' }

// 子账户状态
export const SUB_ACCOUNT_STATUS = { 0: '正常', 1: '已销户' }

// 交易状态
export const TX_STATUS = { 0: '正常', 1: '已冲正' }

// 交易类型
export const TX_TYPE = {
  1001: '客户账号开户', 1002: '客户账号销户',
  1003: '负债账号开户', 1004: '负债账号销户',
  2001: '存款', 2002: '取款',
  2003: '行内转账',
  3001: '存款冲正', 3002: '取款冲正', 3003: '转账冲正'
}

// 柜员类型
export const TELLER_TYPE = { 0: '普通柜员', 1: '库管柜员' }

// 柜员状态
export const TELLER_STATUS = { 0: '正常', 1: '停用', 2: '离职' }

// 机构状态
export const INSTITUTION_STATUS = { 0: '正常', 1: '停用' }

// 证件类型 (目前后端仍返回 String，保留字典供后续改造)
export const ID_TYPE = { 0: '身份证', 1: '护照', 2: '军官证' }

// 负债账户类型 (目前后端仍返回 String "DEMAND"/"TERM")
export const LIABILITY_ACCOUNT_TYPE = { 0: '活期存款', 1: '定期存款' }

// 客户账号类型 (目前后端仍返回 String "PERSONAL"/"CORPORATE")
export const CUSTOMER_ACCOUNT_TYPE = { 0: '个人', 1: '企业' }

// 大额存单状态
export const CD_STATUS = { 0: '待生效', 1: '正常', 2: '冻结', 3: '已兑付', 4: '已撤销' }

// 后端 String 类型枚举 → 中文映射（兼容旧数据）
export const LIABILITY_ACCOUNT_TYPE_STR = { DEMAND: '活期存款', TERM: '定期存款' }
export const CUSTOMER_ACCOUNT_TYPE_STR = { PERSONAL: '个人', CORPORATE: '企业' }
export const ID_TYPE_STR = { ID_CARD: '身份证', PASSPORT: '护照', MILITARY_ID: '军官证' }