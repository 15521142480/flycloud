import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/login' // 入口重定向到路由login
    },
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
      path: '/home',
      name: 'home',
      meta: {
        title: '首页'
      },
      component: () => import('../view/home/home')
    },
    {
      path: '/file-list',
      name: 'fileList',
      meta: {
        title: '文件列表'
      },
      component: () => import('../view/file/file-list')
    },
    {
      path: '/test-list',
      name: 'testList',
      meta: {
        title: '文件列表'
      },
      component: () => import('../view/test/test-list')
    }
  ]
})
