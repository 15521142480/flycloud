import request from '@/config/axios'
import {rsaEncrypt} from "@/utils/crypto/rsa";
import {useCache, CACHE_KEY} from "@/hooks/web/useCache";
const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER


export interface ProfileVO {
  id: number
  username: string
  name: string
  dept: {
    id: number
    name: string
  }
  roles: {
    id: number
    name: string
  }[]
  posts: {
    id: number
    name: string
  }[]
  socialUsers: {
    type: number
    openid: string
  }[]
  email: string
  mobile: string
  sex: number
  avatar: string
  status: number
  remark: string
  loginIp: string
  loginDate: Date
  createTime: Date
}

export interface UserProfileUpdateReqVO {
  name: string
  email: string
  mobile: string
  sex: number
}

// 查询用户个人信息
export const getUserProfile = () => {
  return request.get({ url: `/${SYS_BASE_URL}/user/get`})
}

// 修改用户个人信息
export const updateUserProfile = (data: UserProfileUpdateReqVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/user/saveOrUpdate`, data })
}

// 用户密码重置
export const updateUserPassword = async (oldPassword: string, newPassword: string) => {
  const { wsCache } = useCache()
  const userInfo = wsCache.get(CACHE_KEY.USER)
  const data = {
    id: userInfo.user.id,
    password: newPassword
  }
  data.password = await rsaEncrypt(
    newPassword,
    import.meta.env.VITE_FLY_CLOUD_LOGIN_PASSWORD_PUBLIC_KEY
  )
  return request.post({
    url: `/${SYS_BASE_URL}/user/customResetPassword`, data
  })
}

// 用户头像上传
export const uploadAvatar = (data) => {
  return request.upload({ url: '/user/profile/update-avatar', data: data })
}
