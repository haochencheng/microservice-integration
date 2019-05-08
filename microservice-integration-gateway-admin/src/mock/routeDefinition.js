import Mock from 'mockjs'
import { param2Obj } from '@/utils'

const List = []
const count = 20

for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    name: 'aaa',
    routeDefinitionUri: '/ubet',
    createTime: +Mock.Random.date('T'),
    status: 1,
    order: 0,
    desc: ''
  }))
}

export default {
  getList: config => {
    // console.log('config===========>>>>' + JSON.stringify(config))
    const { page = 1, limit = 20 } = param2Obj(config.url)

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
