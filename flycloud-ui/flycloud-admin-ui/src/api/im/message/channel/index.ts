import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

export interface ImChannelMessageRespVO {
  id: string
  clientMessageId?: string
  channelId: string
  materialId: string
  type: number
  content: string
  receiptStatus?: number
  sendTime: string
}

// 拉取当前用户应收的频道消息（离线增量）；按 minId 游标分页
export const pullChannelMessageList = (
  params: { minId: string; size?: number },
  signal?: AbortSignal
) => {
  return request.get<ImChannelMessageRespVO[]>({
    url: `/${SYS_BASE_URL}/im/channel/message/pull`,
    params,
    signal
  })
}

// 上报频道消息已读位置；切到频道会话或拉到新消息后调
export const readChannelMessages = (channelId: string, messageId: string) => {
  return request.put({
    url: `/${SYS_BASE_URL}/im/channel/message/read`,
    params: { channelId, messageId }
  })
}
