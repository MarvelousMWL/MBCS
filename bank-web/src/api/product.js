import request from '../utils/request'
export function getProductList() { return request({ url: '/liability/product', method: 'get' }) }
export function getProduct(code) { return request({ url: '/liability/product/' + code, method: 'get' }) }
export function createProduct(data) { return request({ url: '/liability/product', method: 'post', data }) }
export function updateProduct(code, data) { return request({ url: '/liability/product/' + code, method: 'put', data }) }
export function getCfg(code, ep) { return request({ url: '/liability/product/' + code + '/' + ep, method: 'get' }) }
