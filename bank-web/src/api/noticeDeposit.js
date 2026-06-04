import request from '../utils/request'

// 通知存款 - 开户
export function openNoticeDeposit(data) {
  return request({
    url: '/liability/notice-deposit/open',
    method: 'post',
    data
  })
}

// 通知存款 - 预约支取
export function appointWithdraw(data) {
  return request({
    url: '/liability/notice-deposit/appoint-withdraw',
    method: 'post',
    data
  })
}

// 通知存款 - 支取
export function withdrawNoticeDeposit(data) {
  return request({
    url: '/liability/notice-deposit/withdraw',
    method: 'post',
    data
  })
}

// 通知存款 - 查询账户列表
export function getNoticeDepositList(params) {
  return request({
    url: '/liability/notice-deposit/accounts',
    method: 'get',
    params
  })
}
