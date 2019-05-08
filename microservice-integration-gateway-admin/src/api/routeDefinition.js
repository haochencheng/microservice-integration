import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/route/list',
    method: 'get',
    params: query
  })
}

export function enabledById(data) {
  return request({
    url: '/route/enabled',
    method: 'post',
    data
  })
}

export function createRoute(data) {
  return request({
    url: '/route/create',
    method: 'post',
    data
  })
}

export function updateRoute(data) {
  return request({
    url: '/route/update',
    method: 'post',
    data
  })
}

export function fetchAvailableRouteList(query) {
  return request({
    url: '/route/list/available',
    method: 'get',
    params: query
  })
}
