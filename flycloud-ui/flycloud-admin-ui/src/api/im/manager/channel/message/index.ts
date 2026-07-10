import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImManagerChannelMessageVO {
  id: string
  channelId: string
  channelName?: string
  materialId: string
  materialTitle?: string
  materialCoverUrl?: string
  type: number
  content?: string
  receiverUserIds?: string[]
  sendTime?: Date
}

export interface ImManagerChannelMessageSendReqVO {
  materialId: string
  receiverUserIds?: string[]
}

// 立即推送频道消息
export const sendManagerChannelMessage = (data: ImManagerChannelMessageSendReqVO) => {
  return request.post({ url: `/${SYS_BASE_URL}/im/manager/channel-message/send`, data })
}

// 删除频道消息
export const deleteManagerChannelMessage = (id: string) => {
  return request.delete({
    url: `/${SYS_BASE_URL}/im/manager/channel-message/delete`,
    params: { id }
  })
}

// 获得频道消息分页
export const getManagerChannelMessagePage = (params: PageParam) => {
  return request.get({ url: `/${SYS_BASE_URL}/im/manager/channel-message/page`, params })
}
