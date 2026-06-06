import request from '../utils/request'

// 大额存单 - 产品发行
export function issueCertificate(data) {
  return request({
    url: '/liability/cd/product',
    method: 'post',
    data
  })
}

// 大额存单 - 认购
export function subscribeCertificate(data) {
  return request({
    url: '/liability/cd/subscribe',
    method: 'post',
    data
  })
}

// 大额存单 - 兑付
export function redeemCertificate(data) {
  return request({
    url: '/liability/cd/redeem',
    method: 'post',
    data
  })
}

// 大额存单 - 转让
export function transferCertificate(data) {
  return request({
    url: '/liability/cd/transfer',
    method: 'post',
    data
  })
}

// 大额存单 - 批量到期处理
export function batchMaturity(data) {
  return request({
    url: '/liability/cd/batch/maturity',
    method: 'post',
    data
  })
}

// 大额存单 - 查询产品列表
export function getProductList(params) {
  return request({
    url: '/liability/cd/products',
    method: 'get',
    params
  })
}

// 大额存单 - 查询认购记录
export function getSubscribeList(params) {
  return request({
    url: '/liability/cd/accounts',
    method: 'get',
    params
  })
}
