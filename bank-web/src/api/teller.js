import request from '../utils/request'

export function getTellerList() {
  return request({
    url: '/teller/teller',
    method: 'get'
  })
}

export function getTellerByNo(tellerNo) {
  return request({
    url: `/teller/teller/${tellerNo}`,
    method: 'get'
  })
}

export function getTellerByInstitution(institutionNo) {
  return request({
    url: `/teller/teller/institution/${institutionNo}`,
    method: 'get'
  })
}

export function createTeller(data) {
  return request({
    url: '/teller/teller',
    method: 'post',
    data
  })
}

export function updateTeller(tellerNo, data) {
  return request({
    url: `/teller/teller/${tellerNo}`,
    method: 'put',
    data
  })
}

export function deleteTeller(tellerNo) {
  return request({
    url: `/teller/teller/${tellerNo}`,
    method: 'delete'
  })
}
