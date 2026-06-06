import request from '../utils/request'

// 通知存款 - 开户
export function openNoticeDeposit(data) {
  return request({
    url: '/liability/notice-deposit/open',
    method: 'post',
    data
  })
}

// 通知存款 - 通知支取预约
export function appointWithdraw(data) {
  return request({
    url: '/liability/notice-deposit/book',
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
