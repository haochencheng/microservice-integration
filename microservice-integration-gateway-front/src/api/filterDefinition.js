import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/filter/list',
    method: 'get',
    params: query
  })
}

export function enabledById(data) {
  return request({
    url: '/filter/enabled',
    method: 'post',
    data
  })
}

export function createFilter(data) {
  return request({
    url: '/filter/create',
    method: 'post',
    data
  })
}

export function updateFilter(data) {
  return request({
    url: '/filter/update',
    method: 'post',
    data
  })
}

