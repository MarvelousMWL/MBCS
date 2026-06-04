import request from '../utils/request'

// 批量结息
export function batchSettleInterest(data) {
  return request({
    url: '/liability/batch/settle-interest',
    method: 'post',
    data
  })
}

// 批量兑付
export function batchRedeem(data) {
  return request({
    url: '/liability/batch/redeem',
    method: 'post',
    data
  })
}

// 批量逾期处理
export function batchOverdue(data) {
  return request({
    url: '/liability/batch/overdue',
    method: 'post',
    data
  })
}

// 批量形态转移
export function batchTransferType(data) {
  return request({
    url: '/liability/batch/transfer-type',
    method: 'post',
    data
  })
}

// 查询批量处理记录
export function getBatchRecordList(params) {
  return request({
    url: '/liability/batch/records',
    method: 'get',
    params
  })
}
