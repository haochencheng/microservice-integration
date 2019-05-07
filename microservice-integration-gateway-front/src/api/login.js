import request from '@/utils/request'
const aesUtil = require('@/utils/aesUtil')

export function login(data) {
  const password = data.password
  const saltPassword = aesUtil.encryption(password)
  const loginData = Object.assign({}, data)
  loginData['password'] = saltPassword
  return request({
    url: '/user/login',
    method: 'post',
    data: loginData
  })
}

export function getInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
