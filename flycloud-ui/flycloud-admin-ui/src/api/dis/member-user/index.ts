import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER
const BASE_URL = `/${SYS_BASE_URL}/test/product/member-user`

export interface MemberUserSearchVO {
  id: number
  mobile: string
  email: string
  status: number
  nickname: string
  name: string
  avatar: string
  tagIds: number[]
  postIds: number[]
  levelId: number
  groupId: number
  mark: string
  createTime: string
  updateTime: string
}

export const synchronize = () => request.post<string>({ url: `${BASE_URL}/synchronize` })
export const upgradeIndex = () => request.post<string>({ url: `${BASE_URL}/upgrade-index` })
export const getPage = (params: Record<string, any>) => request.get({ url: `${BASE_URL}/page`, params })
export const updateMemberUser = (data: Record<string, any>) => request.put({ url: BASE_URL, data })
