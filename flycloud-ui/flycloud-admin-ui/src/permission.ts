import router, { resetRouter } from './router'
import type { RouteRecordRaw } from 'vue-router'
import { isAxiosError } from 'axios'
import { isRelogin } from '@/config/axios/service'
import { getAccessToken, removeToken } from '@/utils/auth'
import { deleteUserCache } from '@/hooks/web/useCache'
import { useTitle } from '@/hooks/web/useTitle'
import { useNProgress } from '@/hooks/web/useNProgress'
import { usePageLoading } from '@/hooks/web/usePageLoading'
import { useDictStoreWithOut } from '@/store/modules/dict'
import { useUserStoreWithOut } from '@/store/modules/user'
import { usePermissionStoreWithOut } from '@/store/modules/permission'

const { start, done } = useNProgress()

const { loadStart, loadDone } = usePageLoading()

/** 公开页面无需加载用户、字典和菜单，残留 Token 不能阻止进入登录页。 */
const whiteList = [
  '/login',
  '/social-login',
  '/auth-redirect',
  '/bind',
  '/register',
  '/oauthLogin/gitee'
]

/** 初始化失败时允许展示的静态错误页，避免错误页再次触发同一失败请求。 */
const recoveryPaths = ['/403', '/404', '/500']

/**
 * 导航前验证当前会话并初始化字典和菜单；失败时进入登录页或可重试的错误页。
 */
router.beforeEach(async (to, from, next) => {
  start()
  loadStart()
  const userStore = useUserStoreWithOut()
  const accessToken = getAccessToken()
  if (!accessToken || to.path === '/login') {
    userStore.resetState()
  }
  if (whiteList.includes(to.path) || recoveryPaths.includes(to.path)) {
    next()
    return
  }
  if (!accessToken) {
    next({ path: '/login', query: { redirect: to.fullPath }, replace: true })
    return
  }

  const dictStore = useDictStoreWithOut()
  const permissionStore = usePermissionStoreWithOut()
  const needsInitialization = !userStore.getIsSetUser
  isRelogin.show = true
  try {
    if (needsInitialization) {
      // 每次页面重新打开都先向服务端验证会话，浏览器里的用户缓存不能证明 Token 仍有效。
      await userStore.setUserInfoAction(true)
    }
    if (!dictStore.getIsSetDict) {
      await dictStore.setDictMap()
    }
    if (needsInitialization) {
      await permissionStore.generateRoutes()
      resetRouter()
      permissionStore.getAddRouters.forEach((route) => {
        router.addRoute(route as unknown as RouteRecordRaw)
      })
      // Vue Router 已解码 query，直接保留完整地址，避免重复解码导致参数丢失或异常。
      const redirect = from.query.redirect
      const redirectPath =
        typeof redirect === 'string' && redirect.startsWith('/') && !redirect.startsWith('//')
          ? redirect
          : to.fullPath
      const target = router.resolve(redirectPath)
      next({ path: target.path, query: target.query, hash: target.hash, replace: true })
    } else {
      next()
    }
  } catch (error: unknown) {
    if (getAccessToken() && getAccessToken() !== accessToken) {
      // 重新登录后迟到的旧初始化请求不能清理新会话，重新验证当前令牌即可。
      next({ path: to.path, query: to.query, hash: to.hash, replace: true })
      return
    }
    userStore.resetState()
    resetRouter()
    const status = isAxiosError(error) ? error.response?.status : undefined
    if (status === 401 || !getAccessToken()) {
      removeToken()
      deleteUserCache()
      next({ path: '/login', query: { redirect: to.fullPath }, replace: true })
    } else {
      // 服务故障和无权限不等于会话失效；保留 Token，错误页可返回首页重新加载。
      next({
        path: status === 403 ? '/403' : '/500',
        query: { redirect: to.fullPath },
        replace: true
      })
    }
  } finally {
    isRelogin.show = false
  }
})

/** 导航完成或被取消时结束进度条和页面加载状态。 */
router.afterEach((to) => {
  useTitle(to?.meta?.title as string)
  done() // 结束Progress
  loadDone()
})

/** 异步组件等导航异常也必须释放加载状态，避免遗留全屏遮罩。 */
router.onError(() => {
  isRelogin.show = false
  done()
  loadDone()
})
