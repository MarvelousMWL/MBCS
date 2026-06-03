import request from '../utils/request'

export function getCustomerSubAccountList(customerAccountNo) {
  return request({
    url: '/liability/customer-sub-account/customer-account/' + customerAccountNo,
    method: 'get'
  })
}

export function getCustomerSubAccountByType(customerAccountNo, accountType) {
  return request({
    url: '/liability/customer-sub-account/customer-account/' + customerAccountNo + '/account-type/' + accountType,
    method: 'get'
  })
}

export function openSubAccount(data) {
  return request({
    url: '/liability/customer-sub-account/open',
    method: 'post',
    data
  })
}