import axios from 'axios'
import { Message } from 'element-ui'
import store from '../store'
import { getToken } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_API, // api 的 base_url
  timeout: 5000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      config.headers['Gateway-Token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    } else {
      if (!config.url.startsWith('/user/login')) {
        this.store.dispatch('FedLogOut')
      }
    }
    return config
  },
  error => {
    // Do something with request error
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * retCode 非0为错误
     */
    const res = response.data
    if (res.retCode === 0) {
      return res.data
    } else {
      // this.$message.error(res.retMsg)
      Message({
        message: res.retMsg,
        type: 'error',
        duration: 2 * 1000
      })
      // -100:非法的token;-200:Token 过期了
      if (res.retCode === -100 || res.retCode === -200) {
        this.store.dispatch('FedLogOut')
      }
      throw new Error(res.retMsg)
    }
  },
  err => {
    if (err && err.response) {
      switch (err.response.status) {
        case 400:
          err.message = '请求错误'
          break

        case 401:
          err.message = '未授权，请登录'
          this.store.dispatch('FedLogOut')
          break

        case 403:
          err.message = '拒绝访问'
          break

        case 404:
          err.message = `请求地址出错: ${err.response.config.url}`
          break

        case 408:
          err.message = '请求超时'
          break

        case 500:
          err.message = '服务器内部错误'
          break

        case 501:
          err.message = '服务未实现'
          break

        case 502:
          err.message = '网关错误'
          break

        case 503:
          err.message = '服务不可用'
          break

        case 504:
          err.message = '网关超时'
          break

        case 505:
          err.message = 'HTTP版本不受支持'
          break
      }
    }
    Message({
      message: err.message,
      type: 'error',
      duration: 2 * 1000
    })
    return Promise.reject(err)
  }
)

export default service
