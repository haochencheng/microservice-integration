import Mock from 'mockjs'
import { param2Obj } from '@/utils'

const List = []
const count = 20

for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    serviceName: Mock.Random.string(),
    servicePath: '/ubet',
    createTime: +Mock.Random.date('T'),
    serviceMethod: 'Get',
    needAuthorization: Mock.Random.boolean(),
    enabled: Mock.Random.boolean(),
    desc: Mock.Random.string()
  }))
}

export default {
  getList: config => {
    // console.log('config===========>>>>' + JSON.stringify(config))
    const { page = 1, limit = 10 } = param2Obj(config.url)

    // const mockList = List.filter(item => {
    //   if (name && item.name.indexOf(item.name) < 0) return false
    //   return true
    // })

    // if (sort === '-id') {
    //   mockList = mockList.reverse()
    // }

    const pageList = List.filter((item, index) => index < limit * page && index >= limit * (page - 1))
    return {
      retCode: 0,
      data: {
        total: List.length,
        list: pageList
      }
    }
  }
}
