import axios, { AxiosError } from 'axios'
import type { AxiosInstance } from 'axios'
import { ElNotification } from 'element-plus'
import qs from 'qs'
import { config } from '@/config/axios/config'
import { getAccessToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import { deleteUserCache } from '@/hooks/web/useCache'
import { useI18n } from '@/hooks/web/useI18n'

const { t } = useI18n()
const { result_code, request_timeout } = config

/** 初始化期间由路由守卫接管登录跳转，运行期间合并并发的未认证跳转。 */
export const isRelogin = { show: false }

/** 登录、注册和验证码请求不携带旧用户令牌，保留登录接口自身的 Basic 认证。 */
const whiteList = [
  '/captcha/getCode',
  '/captcha/getImageTextClickCaptcha',
  '/captcha/checkGetImageTextClickCaptcha',
  '/captcha/check',
  '/admin/auth/token',
  '/admin/auth/login',
  '/admin/auth/refresh-token',
  '/user/register'
]

/** 公共请求实例，业务响应仍返回 R 包装，由 request 封装统一解包 data。 */
const service: AxiosInstance = axios.create({
  timeout: request_timeout,
  withCredentials: false
})

/** 按请求用途添加令牌，不能覆盖调用方明确提供的认证信息。 */
service.interceptors.request.use((requestConfig) => {
  const path = requestConfig.url?.split('?')[0] || ''
  const skipToken =
    requestConfig.headers.isToken === false || whiteList.some((url) => path.endsWith(url))
  const token = getAccessToken()
  if (token && !skipToken && !requestConfig.headers.Authorization) {
    requestConfig.headers.Authorization = 'Bearer ' + token
  }

  if (
    requestConfig.method?.toUpperCase() === 'POST' &&
    requestConfig.headers['Content-Type'] === 'application/x-www-form-urlencoded'
  ) {
    requestConfig.data = qs.stringify(requestConfig.data)
  }
  if (requestConfig.method?.toUpperCase() === 'GET' && requestConfig.params) {
    const params = qs.stringify(requestConfig.params, { allowDots: true })
    requestConfig.params = {}
    if (params) {
      requestConfig.url += (requestConfig.url?.includes('?') ? '&' : '?') + params
    }
  }
  return requestConfig
})

/**
 * 读取服务端业务提示；未知响应结构使用本地国际化提示。
 *
 * @param data 服务端原始响应体
 * @param fallback 无可用提示时的默认文案
 * @returns 可展示给用户的错误提示
 */
const getResponseMessage = (data: unknown, fallback: string): string => {
  if (typeof data === 'object' && data !== null && 'msg' in data) {
    return typeof data.msg === 'string' && data.msg ? data.msg : fallback
  }
  return fallback
}

/**
 * 清理已失效会话并返回登录页；初始化中的导航由守卫处理，避免整页刷新或循环跳转。
 *
 * @param error 服务端返回的未认证错误
 */
const handleUnauthorized = (error: AxiosError): void => {
  const token = getAccessToken()
  const authorization = error.config?.headers.Authorization
  if (typeof authorization !== 'string' || !authorization.startsWith('Bearer ')) {
    // 登录接口的 Basic 认证失败、匿名验证码请求失败，都不代表原用户令牌失效。
    ElNotification.error({
      title: getResponseMessage(error.response?.data, t('sys.api.errMsg401'))
    })
    return
  }
  // 已处理的并发 401，以及重新登录后迟到的旧请求，都不能再次清理当前会话。
  if (authorization !== 'Bearer ' + token) {
    return
  }

  removeToken()
  deleteUserCache()
  resetRouter()
  ElNotification.error({ title: t('sys.api.timeoutMessage') })
  if (!isRelogin.show) {
    isRelogin.show = true
    const currentRoute = router.currentRoute.value
    const query =
      currentRoute.path === '/login' ? currentRoute.query : { redirect: currentRoute.fullPath }
    void router.replace({ path: '/login', query }).finally(() => {
      isRelogin.show = false
    })
  }
}

/** 校验 HTTP 与业务响应，所有失败都保留拒绝状态供调用方恢复 loading。 */
service.interceptors.response.use(
  async (response) => {
    let data = response.data
    if (!data) {
      throw new AxiosError(
        t('sys.api.errorMessage'),
        undefined,
        response.config,
        undefined,
        response
      )
    }
    if (['blob', 'arraybuffer'].includes(response.config.responseType || '')) {
      const contentType = String(response.headers['content-type'] || data.type || '')
      if (!contentType.includes('application/json')) {
        return data
      }
      data = await new Response(data).json()
    }

    const code = data.code ?? result_code
    if (code !== 0) {
      const message = getResponseMessage(data, t('sys.api.errorMessage'))
      ElNotification.error({ title: message })
      throw new AxiosError(message, String(code), response.config, undefined, response)
    }
    return data
  },
  (error: AxiosError) => {
    const status = error.response?.status
    if (status === 401) {
      handleUnauthorized(error)
    } else if (status === 403) {
      ElNotification.warning({
        title: getResponseMessage(error.response?.data, t('sys.api.errMsg403'))
      })
    } else {
      // 断网和超时没有 response，不能再次解引用异常而掩盖原始失败。
      const message =
        error.code === AxiosError.ECONNABORTED || error.code === AxiosError.ETIMEDOUT
          ? t('sys.api.apiTimeoutMessage')
          : status
            ? `${t('sys.api.apiRequestFailed')} (${status})`
            : t('sys.api.networkExceptionMsg')
      ElNotification.error({ title: message })
    }
    return Promise.reject(error)
  }
)

export { service }
