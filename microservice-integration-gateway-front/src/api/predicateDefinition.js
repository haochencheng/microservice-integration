import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/predicate/list',
    method: 'get',
    params: query
  })
}

export function enabledById(data) {
  return request({
    url: '/predicate/enabled',
    method: 'post',
    data
  })
}

export function createPredicate(data) {
  return request({
    url: '/predicate/create',
    method: 'post',
    data
  })
}

export function updatePredicate(data) {
  return request({
    url: '/predicate/update',
    method: 'post',
    data
  })
}

