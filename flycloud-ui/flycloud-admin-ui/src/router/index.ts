import type { App } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { createRouter, createWebHashHistory } from 'vue-router'
import remainingRouter from './modules/remaining'

// 创建路由实例
const router = createRouter({
  // history: createWebHistory(import.meta.env.VITE_BASE_PATH), // createWebHashHistory URL带#，createWebHistory URL不带#
  history: createWebHashHistory(import.meta.env.VITE_BASE_PATH), // createWebHashHistory URL带#，createWebHistory URL不带#  strict: true,
  routes: remainingRouter as RouteRecordRaw[],
  scrollBehavior: () => ({ left: 0, top: 0 })
})

/**
 * 应始终保留的静态路由名称。
 *
 * 在路由实例创建后立即快照，列表只包含 remaining.ts 中的基础路由；后续由权限模块动态
 * 注册的路由不在该集合内，重置时会被移除。
 */
const staticRouteNameSet = new Set(
  router
    .getRoutes()
    .map((route) => route.name)
    .filter((name): name is NonNullable<typeof name> => Boolean(name))
)

/**
 * 移除当前会话已注册的动态路由，并保留所有静态基础路由。
 *
 * 不能通过手工白名单维护静态路由名称，否则首页、个人中心等新增静态路由可能被误删，进而
 * 导致左侧菜单或页签无法跳转。
 */
export const resetRouter = (): void => {
  router.getRoutes().forEach((route) => {
    const { name } = route
    if (name && !staticRouteNameSet.has(name)) {
      router.hasRoute(name) && router.removeRoute(name)
    }
  })
}

export const setupRouter = (app: App<Element>) => {
  app.use(router)
}

export default router
