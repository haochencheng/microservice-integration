import { param2Obj } from '@/utils'

const userMap = {
  admin: {
    roles: ['admin'],
    token: 'admin',
    introduction: '我是超级管理员',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Super Admin'
  },
  editor: {
    roles: ['editor'],
    token: 'editor',
    introduction: '我是编辑',
    avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Normal Editor'
  }
}

export default {
  loginByUsername: config => {
    console.log('config=======' + JSON.stringify(config))
    const { username } = JSON.parse(config.body)
    const result = {
      retCode: 0,
      data: userMap[username]
    }
    console.log('mock login result' + JSON.stringify(result))
    return result
  },
  getUserInfo: config => {
    const { token } = param2Obj(config.url)
    if (userMap[token]) {
      const result = {
        retCode: 0,
        data: userMap[token]
      }
      return result
    } else {
      return false
    }
  },
  logout: () => {
    return {
      retCode: 0,
      data: 'success'
    }
  }
}
