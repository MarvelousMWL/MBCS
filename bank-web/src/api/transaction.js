import request from '../utils/request'

export function deposit(data) {
  return request({
    url: '/liability/transaction/deposit',
    method: 'post',
    data
  })
}

export function withdraw(data) {
  return request({
    url: '/liability/transaction/withdraw',
    method: 'post',
    data
  })
}

export function cancelDeposit(data) {
  return request({
    url: '/liability/transaction/deposit-cancel',
    method: 'post',
    data
  })
}

export function withdrawCancel(data) {
  return request({
    url: '/liability/transaction/withdraw-cancel',
    method: 'post',
    data
  })
}

export function getTransactionList(params) {
  return request({
    url: '/liability/transaction',
    method: 'get',
    params
  })
}
