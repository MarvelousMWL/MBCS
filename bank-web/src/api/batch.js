import request from '../utils/request'

// 批量结息
export function batchSettleInterest(data) {
  return request({
    url: '/liability/batch/interest-settlement/execute',
    method: 'post',
    params: data
  })
}

// 批量自动兑付
export function batchRedeem(data) {
  return request({
    url: '/liability/batch/auto-redemption/execute',
    method: 'post',
    params: data
  })
}

// 批量逾期处理
export function batchOverdue(data) {
  return request({
    url: '/liability/batch/overdue-processing/execute',
    method: 'post',
    params: data
  })
}

// 批量形态转移
export function batchTransferType(data) {
  return request({
    url: '/liability/batch/form-transfer/execute',
    method: 'post',
    params: data
  })
}