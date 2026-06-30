import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerGroupRequestVO {
  id: number
  groupId: number
  groupName?: string
  userId: number
  userNickname?: string
  inviterUserId?: number
  inviterNickname?: string
  applyContent?: string
  addSource?: number
  handleResult: number
  handleUserId?: number
  handleNickname?: string
  handleContent?: string
  handleTime?: Date
  createTime: Date
}

// 获得加群申请分页
export const getManagerGroupRequestPage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/group-request/page`, params })
}
