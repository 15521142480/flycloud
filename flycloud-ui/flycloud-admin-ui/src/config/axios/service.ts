// @ts-nocheck
import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestHeaders,
  AxiosResponse,
  InternalAxiosRequestConfig
} from 'axios'

import { ElMessageBox, ElNotification } from 'element-plus'
import qs from 'qs'
import { config } from '@/config/axios/config'
import { getAccessToken, removeToken } from '@/utils/auth'
import { getErrorMessage } from './errorCode'

import { resetRouter } from '@/router'
import { deleteUserCache } from '@/hooks/web/useCache'
// const router = useRouter() // 路由
// import router from '@/router' // 路由
// import {useUserStore} from "@/store/modules/user";
// const userStore = useUserStore()

const { t } = useI18n()

const { result_code, request_timeout } = config

// 需要忽略的提示。忽略后，自动 Promise.reject('error')
const ignoreMsgs = [
  t('auto.config.axios.service.k6a0a80cf'), // 刷新令牌被删除时，不用提示
  t('auto.config.axios.service.k464111e9') // 使用刷新令牌，刷新获取新的访问令牌时，结果因为过期失败，此时需要忽略。否则，会导致继续 401，无法跳转到登出界面
]

// 是否显示重新登录
export const isRelogin = { show: false }

// Axios 无感知刷新令牌，参考 https://www.dashingdog.cn/article/11 与 https://segmentfault.com/a/1190000020210980 实现
// 请求队列
// let requestList: any[] = []
// // 是否正在刷新中
// let isRefreshToken = false

// 请求白名单，无须token的接口
const whiteList: string[] = ['/captcha/getCode', '/admin/auth/login', '/admin/auth/refresh-token']

/**
 * 创建axios实例
 */
const service: AxiosInstance = axios.create({
  // todo 忽略,目前使用代理方式
  // baseURL: base_url, // api 的 base_url

  timeout: request_timeout, // 请求超时时间
  withCredentials: false // 禁用 Cookie 等信息
})

/**
 * request拦截器
 */
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 是否需要设置 token
    let isToken = (config!.headers || {}).isToken === false
    whiteList.some((v) => {
      if (config.url) {
        config.url.indexOf(v) > -1
        return (isToken = false)
      }
    })

    // 每个请求加上授权token
    if (getAccessToken() && !isToken) {
      ;(config as Recordable).headers.Authorization = 'Bearer ' + getAccessToken()
    }

    const params = config.params || {}
    const data = config.data || false
    if (
      config.method?.toUpperCase() === 'POST' &&
      (config.headers as AxiosRequestHeaders)['Content-Type'] ===
        'application/x-www-form-urlencoded'
    ) {
      config.data = qs.stringify(data)
    }
    // get参数编码
    if (config.method?.toUpperCase() === 'GET' && params) {
      config.params = {}
      const paramsStr = qs.stringify(params, { allowDots: true })
      if (paramsStr) {
        config.url = config.url + '?' + paramsStr
      }
    }
    return config
  },
  (error: AxiosError) => {
    // Do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

/**
 * response 拦截器
 */
service.interceptors.response.use(

  // 响应状态码为：成功；即200、201、204
  async (response: AxiosResponse<any>) => {
    let { data } = response
    if (!data) {
      // 返回“[HTTP]请求没有返回值”;
      throw new Error()
    }
    // 二进制数据则直接返回，例如说 Excel 导出
    if (
      response.request.responseType === 'blob' ||
      response.request.responseType === 'arraybuffer'
    ) {
      // 注意：如果导出的响应为 json，说明可能失败了，不直接返回进行下载
      if (response.data.type !== 'application/json') {
        return response.data
      }
      data = await new Response(response.data).json()
    }

    // 处理code和msg
    const code = data.code ?? result_code // 不能用 ||, 因为0是假值
    const msg = data.msg || getErrorMessage('default')
    if (ignoreMsgs.indexOf(msg) !== -1) {
      // 如果是忽略的错误码，直接返回 msg 异常
      return Promise.reject(msg)
    } else if (code === 1) {
      ElNotification.error({ title: msg })
    } else if (code !== 0) {
      if (msg === t('auto.config.axios.service.k6a0a80cf')) {
        // hard coding：忽略这个提示，直接登出
        console.log(msg)
        return handleAuthorized()
      } else {
        ElNotification.error({ title: msg })
      }
      return Promise.reject('error')
    } else {
      return data
    }
  },


  // 响应为错误的情况
  (error: AxiosError) => {

    console.log('err' + error)
    const { response } = error
    const status: number = response!.status
    if (status === 401 || status === 403) {
      handleError(response!.status, response!.data as object)
    } else {
      let { message } = error
      if (message === 'Network Error') {
        message = t('sys.api.errorMessage')
      } else if (message.includes('timeout')) {
        message = t('sys.api.apiTimeoutMessage')
      } else if (message.includes('Request failed with status code')) {
        message = t('sys.api.apiRequestFailed') + message.substr(message.length - 3)
      }
      ElNotification.error(message)
    }

    return Promise.reject(error)
  }
)

/**
 * 处理错误的响应
 * @param status
 * @param data
 */
const handleError = (status: number, data) => {

  const msg = data.msg || getErrorMessage(status) || getErrorMessage('default')
  switch (status) {
    case 401: // 401: 未登录状态，跳转登录页
      ElNotification.error(msg)
      resetRouter() // 重置静态路由表
      deleteUserCache() // 删除用户缓存
      removeToken()
      isRelogin.show = false
      // 干掉token后再走一次路由让它过router.beforeEach的校验
      // window.location.href = window.location.href
      // router.replace({path: '/login'})
      window.location.reload()
      break

    case 403: // 403 无权限访问资源
      ElNotification.warning(msg)
      // window.location.reload()
      break

    // case 404: // 404请求不存在
    //   // tip('请求的资源不存在')
    //   break

    default:
    // console.log(other)
  }
}

/**
 * 刷新token
 */
// const refreshToken = async () => {
//   return await axios.post(base_url + '/system/auth/refresh-token?refreshToken=' + getRefreshToken())
// }

/**
 * 处理登录超时
 */
const handleAuthorized = () => {
  if (!isRelogin.show) {
    isRelogin.show = true
    ElMessageBox.confirm(t('sys.api.timeoutMessage'), t('common.confirmTitle'), {
      showCancelButton: false,
      closeOnClickModal: false,
      showClose: false,
      closeOnPressEscape: false,
      confirmButtonText: t('login.relogin'),
      type: 'warning'
    }).then(() => {
      resetRouter() // 重置静态路由表
      deleteUserCache() // 删除用户缓存
      removeToken()
      isRelogin.show = false
      // 干掉token后再走一次路由让它过router.beforeEach的校验
      window.location.href = window.location.href
    })
  }
  return Promise.reject(t('sys.api.timeoutMessage'))
}

export { service }
