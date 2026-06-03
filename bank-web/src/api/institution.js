import request from '../utils/request'

export function getInstitutionList() {
  return request({
    url: '/teller/institution',
    method: 'get'
  })
}

export function getInstitutionByNo(institutionNo) {
  return request({
    url: `/teller/institution/${institutionNo}`,
    method: 'get'
  })
}

export function createInstitution(data) {
  return request({
    url: '/teller/institution',
    method: 'post',
    data
  })
}

export function updateInstitution(institutionNo, data) {
  return request({
    url: `/teller/institution/${institutionNo}`,
    method: 'put',
    data
  })
}

export function deleteInstitution(institutionNo) {
  return request({
    url: `/teller/institution/${institutionNo}`,
    method: 'delete'
  })
}
