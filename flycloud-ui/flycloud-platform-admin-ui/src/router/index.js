import Vue from 'vue'
import Router from 'vue-router'
import Main from '../components/main'

Vue.use(Router)

export default new Router({
  routes: [
    // {
    //   path: '/',
    //   redirect: '/login' // 入口重定向到路由login
    // },
    {
      path: '/login',
      name: 'login',
      meta: {
        title: '登陆'
        // isCache: true // 是否缓存改页面  !!! 用于路由返回时记住上次的滚动位置 等等
      },
      component: () => import('../view/system/login')
    },
    {
      path: '/',
      name: 'main',
      // todo 重定向指定某个路由
      redirect: '/home',
      meta: {
        title: '导航'
      },
      component: Main,
      children: [
        {
          path: 'home', // 带参数  path: 'test/:id'
          name: 'home',
          meta: {
            title: '文件服务'
          },
          component: () => import('../view/home/home')
        }
      ]
    },
    {
      path: '/sys',
      name: 'sys',
      meta: {
        title: '系统管理'
      },
      component: Main,
      children: [
        {
          path: 'menu',
          name: 'menu',
          meta: {
            title: '菜单管理'
          },
          component: () => import('../view/system/sys_menu/sys-menu-list')
        },
        {
          path: 'role',
          name: 'role',
          meta: {
            title: '角色管理'
          },
          component: () => import('../view/system/sys_role/sys-role-list')
        },
        {
          path: 'test',
          name: 'test',
          meta: {
            title: '文件服务'
          },
          component: () => import('../view/test/test-list')
        }
      ]
    }
  ]
})
