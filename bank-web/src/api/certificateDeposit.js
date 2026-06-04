import request from '../utils/request'

// 大额存单 - 产品发行
export function issueCertificate(data) {
  return request({
    url: '/liability/certificate-deposit/issue',
    method: 'post',
    data
  })
}

// 大额存单 - 认购
export function subscribeCertificate(data) {
  return request({
    url: '/liability/certificate-deposit/subscribe',
    method: 'post',
    data
  })
}

// 大额存单 - 兑付
export function redeemCertificate(data) {
  return request({
    url: '/liability/certificate-deposit/redeem',
    method: 'post',
    data
  })
}

// 大额存单 - 转让
export function transferCertificate(data) {
  return request({
    url: '/liability/certificate-deposit/transfer',
    method: 'post',
    data
  })
}

// 大额存单 - 批量到期处理
export function batchMaturity(data) {
  return request({
    url: '/liability/certificate-deposit/batch-maturity',
    method: 'post',
    data
  })
}

// 大额存单 - 查询产品列表
export function getProductList(params) {
  return request({
    url: '/liability/certificate-deposit/products',
    method: 'get',
    params
  })
}

// 大额存单 - 查询认购记录
export function getSubscribeList(params) {
  return request({
    url: '/liability/certificate-deposit/subscribes',
    method: 'get',
    params
  })
}
