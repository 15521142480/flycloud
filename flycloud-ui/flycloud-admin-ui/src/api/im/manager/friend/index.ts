import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerFriendVO {
  id: number
  userId: number
  userNickname?: string
  friendUserId: number
  friendNickname?: string
  displayName?: string
  addSource?: number
  silent: boolean
  pinned: boolean
  blocked: boolean
  status: number
  addTime?: Date
  deleteTime?: Date
  createTime: Date
}

// 获得好友关系分页
export const getManagerFriendPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/friend/page`, params })
}
