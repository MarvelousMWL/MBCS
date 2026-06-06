import request from '../utils/request'

export function getLiabilityAccountList() {
  return request({ url: '/liability/liability-account', method: 'get' })
}
export function getLiabilityAccountByNo(no) {
  return request({ url: '/liability/liability-account/' + no, method: 'get' })
}
export function getLiabilityAccountByCustomerAccountNo(no) {
  return request({ url: '/liability/liability-account/customer-account/' + no, method: 'get' })
}
export function openCustomerAccount(data) {
  return request({ url: '/liability/customer-account/open', method: 'post', data })
}
export function openLiabilityAccount(data) {
  return request({ url: '/liability/liability-account/open', method: 'post', data })
}
export function closeLiabAcct(data) {
  return request({ url: '/liability/liability-account/close', method: 'post', data })
}
export function freezeAccount(data) {
  return request({ url: '/liability/liability-account/freeze', method: 'post', data })
}
export function unfreezeAccount(data) {
  return request({ url: '/liability/liability-account/unfreeze', method: 'post', data })
}
export function getCustomerAccountList() {
  return request({ url: '/liability/customer-account', method: 'get' })
}
export function openSubAccount(data) {
  return request({ url: '/liability/customer-sub-account/open', method: 'post', data })
}
export function getCustomerSubAccountList(customerAccountNo) {
  return request({ url: '/liability/customer-sub-account/customer-account/' + customerAccountNo, method: 'get' })
}
export function getCustomerSubAccountByType(customerAccountNo, accountType) {
  return request({ url: '/liability/customer-sub-account/customer-account/' + customerAccountNo + '/account-type/' + accountType, method: 'get' })
}

// Deposit product API
export function getProductList() {
  return request({ url: '/liability/product', method: 'get' })
}
export function createProduct(data) {
  return request({ url: '/liability/product', method: 'post', data })
}
export function updateProduct(code, data) {
  return request({ url: '/liability/product/' + code, method: 'put', data })
}
