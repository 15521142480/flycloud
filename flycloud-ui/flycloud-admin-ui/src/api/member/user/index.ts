import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface UserVO {
  id?: string
  avatar?: string
  birthday?: number
  createTime?: number
  loginDate?: number
  loginIp?: string
  mark?: string
  mobile?: string
  email?: string
  password?: string
  name?: string
  nickname?: string
  registerIp?: string
  sex?: number
  status?: number
  areaId?: string
  areaName?: string
  tagIds?: string[]
  groupId?: string
  levelId?: string
  levelName?: string | null
  point?: number | null
  totalPoint?: number | null
  experience?: number | null
}

export interface UserLevelUpdateReqVO {
  id: string
  levelId: string
  reason: string
}

export interface UserPointUpdateReqVO {
  id: string
  point: number
}

// 查询会员用户列表
export const getUserPage = async (params) => {
  return await request.get<PageResult<UserVO[]>>({
    url: `/${SYS_BASE_URL}/admin/member/user/page`,
    params
  })
}

// 查询会员用户详情
export const getUser = async (id: string) => {
  return await request.get<UserVO>({ url: `/${SYS_BASE_URL}/admin/member/user/get?id=` + id })
}

// 修改会员用户
export const updateUser = async (data: UserVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/admin/member/user/update`, data })
}

// 修改会员用户等级
export const updateUserLevel = async (data: UserLevelUpdateReqVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/admin/member/user/update-level`, data })
}

// 修改会员用户积分
export const updateUserPoint = async (data: UserPointUpdateReqVO) => {
  return await request.put({ url: `/${SYS_BASE_URL}/admin/member/user/update-point`, data })
}
