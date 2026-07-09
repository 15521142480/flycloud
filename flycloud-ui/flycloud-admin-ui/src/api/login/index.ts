import request from '@/config/axios'
import { getRefreshToken } from '@/utils/auth'
import type { RegisterVO, UserLoginVO } from './types'
import type { ClickCaptchaVerifyDto } from '@/entity/auth'

import { service } from '@/config/axios/service'
import { Base64 } from 'js-base64'
import { rsaEncrypt } from '@/utils/crypto/rsa'

const AUTH_BASE_URL = import.meta.env.VITE_AUTH_SERVER
const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface SmsCodeVO {
  mobile: string
  scene: number
}

export interface SmsLoginVO {
  mobile: string
  code: string
}

// ============================================== 授权


// 登录
export const login = async (data: UserLoginVO) => {
  // const newData = data; // newData 只是 data 的引用（指向同一块内存）
  const newData = { ...data } // 浅拷贝
  newData.password = await rsaEncrypt(
    newData.password,
    import.meta.env.VITE_FLY_CLOUD_LOGIN_PASSWORD_PUBLIC_KEY
  )
  const method = 'POST'
  const params = null
  return service({
    url: `/${AUTH_BASE_URL}/admin/auth/token`,
    method: method,
    params: params,
    data: newData,
    headers: {
      'Content-Type': 'application/json',
      // 'Code-Key': newData.codeKey,
      // 'Code-Value': newData.code,
      captchaCode: newData.captchaCode,
      Authorization: `Basic ${Base64.encode(`fly:fly_secret`)}`
    }
  })
}

// 登出
export const loginOut = () => {
  return request.post({ url: `/${AUTH_BASE_URL}/admin/auth/logOut` })
}


// 获取验证码
export const getCodeApi = () => {
  return request.get({ url: `/${AUTH_BASE_URL}/captcha/getCode` })
}

// 获取图文点选验证码‌
export const getImageTextClickCaptchaApi = () => {
  return request.post({ url: `/${AUTH_BASE_URL}/captcha/getImageTextClickCaptcha` })
}

// 验证图文点选验证码‌
export const checkImageTextClickCaptchaApi = (data: ClickCaptchaVerifyDto) => {
  return request.post({ url: `/${AUTH_BASE_URL}/captcha/checkGetImageTextClickCaptcha`, data })
}

// 注册
export const register = async (data: RegisterVO) => {
  const newData = { ...data }
  newData.password = await rsaEncrypt(
    newData.password,
    import.meta.env.VITE_FLY_CLOUD_LOGIN_PASSWORD_PUBLIC_KEY
  )
  return request.post({ url: `/${SYS_BASE_URL}/user/register`, data: newData })
}

// 刷新访问令牌
export const refreshToken = () => {
  return request.post({
    url: `/${AUTH_BASE_URL}/admin/auth/refresh-token?refreshToken=` + getRefreshToken()
  })
}

// 获取登录验证码
export const sendSmsCode = (data: SmsCodeVO) => {
  return request.post({ url: `/${AUTH_BASE_URL}/auth/send-sms-code`, data })
}

// 短信验证码登录
export const smsLogin = (data: SmsLoginVO) => {
  return request.post({ url: `/${AUTH_BASE_URL}/auth/sms-login`, data })
}

// 社交快捷登录，使用 code 授权码
export function socialLogin(type: string, code: string, state: string) {
  return request.post({
    url: `/${AUTH_BASE_URL}/auth/social-login`,
    data: {
      type,
      code,
      state
    }
  })
}

// 社交授权的跳转
// export const socialAuthRedirect = (type: number, redirectUri: string) => {
//   return request.get({
//     url: `/${AUTH_BASE_URL}/auth/social-auth-redirect?type=` + type + '&redirectUri=' + redirectUri
//   })
// }

// 获取验证图片以及 token
export const getCode = (data) => {
  return request.postOriginal({ url: `/${AUTH_BASE_URL}/captcha/getCode`, data })
}

// 滑动或者点选验证
export const reqCheck = (data) => {
  return request.postOriginal({ url: `/${AUTH_BASE_URL}/captcha/check`, data })
}

// ============================================== 用户

// 获取用户信息(基本、权限、菜单)
export const getUserInfoApi = () => {
  return request.get({ url: `/${SYS_BASE_URL}/user/getUserInfo` })
}

// 使用租户名，获得租户编号
export const getTenantIdByName = (name: string) => {
  return request.get({ url: `/${AUTH_BASE_URL}/tenant/get-id-by-name?name=` + name })
}
