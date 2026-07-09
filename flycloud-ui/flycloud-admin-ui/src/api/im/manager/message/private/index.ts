import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerPrivateMessageVO {
  id: number
  clientMessageId?: string
  senderId: string
  senderNickname?: string
  receiverId: string
  receiverNickname?: string
  type: number
  content: string
  status: number
  receiptStatus: number
  sendTime: Date
  createTime: Date
}

// 获得私聊消息分页
export const getManagerPrivateMessagePage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/message/private/page`, params })
}

// 获得私聊消息详情
export const getManagerPrivateMessage = (id: number) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/message/private/get`, params: { id } })
}
