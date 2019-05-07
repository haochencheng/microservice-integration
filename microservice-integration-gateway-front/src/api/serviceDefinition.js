import request from '@/utils/request'

export function fetchList(query) {
  console.log(JSON.stringify(query))
  const data = JSON.stringify(query)
  return request({
    url: '/service/list',
    method: 'post',
    data
  })
}

export function enabledById(data) {
  return request({
    url: '/service/enabled',
    method: 'post',
    data
  })
}

export function createService(data) {
  return request({
    url: '/service/create',
    method: 'post',
    data
  })
}

export function updateService(data) {
  return request({
    url: '/service/update',
    method: 'post',
    data
  })
}

