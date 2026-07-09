import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface SignInRecordVO {
  id: number
  userId: string
  day: number
  point: number
}

// 查询用户签到积分列表
export const getSignInRecordPage = async (params) => {
  return await request.get({ url: `/${SYS_BASE_URL}/admin/member/sign-in/record/page`, params })
}
