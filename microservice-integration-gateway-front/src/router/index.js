import Vue from 'vue'
import Router from 'vue-router'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/* Layout */
import Layout from '../views/layout/Layout'

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one routeManage under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authredirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  }
]

export default new Router({
  mode: 'history', // 后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: '',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
      }
    ],
    hidden: true
  },
  {
    name: 'Application',
    path: '/app',
    component: Layout,
    redirect: 'noredirect',
    children: [
      {
        path: 'list',
        name: 'List',
        component: () => import('@/views/gateway/application/index'),
        meta: { title: '应用管理', icon: 'table', noCache: true }
      }
    ]
  },
  {
    name: 'ServiceManage',
    path: '/service',
    component: Layout,
    redirect: 'noredirect',
    children: [
      {
        path: 'list',
        name: 'ServiceList',
        component: () => import('@/views/gateway/service/index'),
        meta: { title: '服务管理', icon: 'component', noCache: true }
      }
    ]
  },
  {
    path: '/route',
    name: 'Route',
    redirect: 'noredirect',
    component: Layout,
    meta: { title: '路由管理', icon: 'component', noCache: true },
    children: [
      {
        path: 'list',
        name: 'RouteDefinition',
        component: () => import('@/views/gateway/routeManage/route/index'),
        meta: { title: '路由列表' }
      },
      {
        path: 'predicate/list',
        name: 'PredicateDefinition',
        component: () => import('@/views/gateway/routeManage/predicate/index'),
        meta: { title: '断言列表' }
      },
      {
        path: 'filter/list',
        name: 'FilterDefinition',
        component: () => import('@/views/gateway/routeManage/filter/index'),
        meta: { title: '过滤器列表' }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]
