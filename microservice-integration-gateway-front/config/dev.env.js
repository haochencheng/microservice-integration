'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  //dev
  BASE_API: '"http://localhost:40100/admin"',
  // BOSS_API:'"http://localhost:8081/"',
  // test
  // BASE_API: '"https://api3-test.urwork.cn/admin"',
  BOSS_API:'"https://boss-docker.urwork.cn/"'
})
