(function(env) {
// 开发环境服务器地址

  const dev = {

    host: 'http://web.dev.cn',

    remote: 'http://api.dev.cn'

  }

  // 测试环境服务器地址

  const prod = {

    host: 'http://web.test.cn',

    remote: 'http://api.test.cn'

  }

  if (env === 'dev') {
    return dev
  } else {
    return prod
  }
}('test'))
