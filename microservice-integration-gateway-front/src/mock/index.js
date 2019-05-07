import Mock from 'mockjs'
// import loginAPI from './login'
// import routeDefinitionAPI from './routeDefinition'
// import serviceDefinitionAPI from './serviceDefinition'

// 修复在使用 MockJS 情况下，设置 withCredentials = true，且未被拦截的跨域请求丢失 Cookies 的问题
// https://github.com/nuysoft/Mock/issues/300
Mock.XHR.prototype.proxy_send = Mock.XHR.prototype.send
Mock.XHR.prototype.send = function() {
  if (this.custom.xhr) {
    this.custom.xhr.withCredentials = this.withCredentials || false
  }
  this.proxy_send(...arguments)
}

Mock.setup({
  timeout: '350-600'
})

// 登录相关
// Mock.mock(/\/user\/login/, 'post', loginAPI.loginByUsername)
// Mock.mock(/\/user\/logout/, 'post', loginAPI.logout)
// Mock.mock(/\/user\/info\.*/, 'get', loginAPI.getUserInfo)

// 网关相关
// Mock.mock(/\/routeDefinition\/list/, 'get', routeDefinitionAPI.getList)
// Mock.mock(/\/service\/list/, 'get', serviceDefinitionAPI.getList)

export default Mock
