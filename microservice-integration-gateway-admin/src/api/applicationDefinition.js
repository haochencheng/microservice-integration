import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/app/list',
    method: 'get',
    params: query
  })
}

export function enabledById(data) {
  return request({
    url: '/app/enabled',
    method: 'post',
    data
  })
}

export function createApp(data) {
  return request({
    url: '/app/create',
    method: 'post',
    data
  })
}

export function updateApp(data) {
  return request({
    url: '/app/update',
    method: 'post',
    data
  })
}

export function fetchAvailableAppList(query) {
  return request({
    url: '/app/list/available',
    method: 'get',
    params: query
  })
}
