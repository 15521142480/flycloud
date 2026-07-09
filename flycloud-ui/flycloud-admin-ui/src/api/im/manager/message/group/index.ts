import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerGroupMessageVO {
  id: number
  clientMessageId?: string
  groupId: number
  groupName?: string
  senderId: string
  senderNickname?: string
  type: number
  content: string
  status: number
  atUserIds?: string[]
  // 与 atUserIds 同长度；后端对找不到 / 已删除的成员返回 null，UI 用 `?.[idx] || userId` 回退到 userId 渲染
  atUserNicknames?: (string | null)[]
  receiptStatus: number
  sendTime: Date
  createTime: Date
}

// 获得群聊消息分页
export const getManagerGroupMessagePage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/message/group/page`, params })
}

// 获得群聊消息详情
export const getManagerGroupMessage = (id: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/message/group/get`, params: { id } })
}
