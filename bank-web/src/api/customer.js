import request from '../utils/request'

export function getCustomerList() {
  return request({
    url: '/customer',
    method: 'get'
  })
}

export function getCustomerByNo(customerNo) {
  return request({
    url: `/customer/no/${customerNo}`,
    method: 'get'
  })
}

export function createCustomer(data) {
  return request({
    url: '/customer',
    method: 'post',
    data
  })
}

export function updateCustomer(data) {
  return request({
    url: '/customer',
    method: 'put',
    data
  })
}

export function deleteCustomer(id) {
  return request({
    url: `/customer/${id}`,
    method: 'delete'
  })
}
