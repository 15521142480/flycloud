import type { RouteRecordRaw } from 'vue-router'
import router, { resetRouter } from '@/router'
import { usePermissionStoreWithOut } from '@/store/modules/permission'
import { useUserStoreWithOut } from '@/store/modules/user'

/**
 * 刷新当前登录用户的权限、菜单缓存和动态路由。
 *
 * <p>本方法只刷新当前浏览器会话的左侧菜单。修改其他用户的角色后，其他用户的已登录浏览器
 * 无法被前端主动推送刷新，需在其下一次导航、手动刷新或重新登录时重新拉取权限。</p>
 */
export const refreshCurrentUserAuthorization = async (): Promise<void> => {
  const currentFullPath = router.currentRoute.value.fullPath
  const userStore = useUserStoreWithOut()
  const permissionStore = usePermissionStoreWithOut()

  // 先移除旧动态路由，再强制请求当前用户最新的权限和菜单数据。
  resetRouter()
  await userStore.refreshUserInfoAction()
  await permissionStore.generateRoutes()
  permissionStore.getAddRouters.forEach((route) => {
    router.addRoute(route as RouteRecordRaw)
  })

  // 若当前页面权限已被移除，跳转首页；否则重新解析当前路由以刷新导航状态。
  const target = router.resolve(currentFullPath)
  await router.replace(target.name === '404Page' ? '/' : currentFullPath)
}
