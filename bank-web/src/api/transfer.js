import request from '../utils/request'

// 账户转账
export function transfer(data) {
  return request({
    url: '/liability/transfer/execute',
    method: 'post',
    data
  })
}

// 转账冲正
export function reverseTransfer(data) {
  return request({
    url: '/liability/transfer/cancel',
    method: 'post',
    data
  })
}

// 查询转账记录
export function getTransferList(params) {
  return request({
    url: '/liability/transfer/records',
    method: 'get',
    params
  }).catch(() => {
    return { data: [] }
  })
}
