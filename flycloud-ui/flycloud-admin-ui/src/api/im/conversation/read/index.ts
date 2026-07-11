import request from '@/config/axios'

const SYS_BASE_URL = import.meta.env.VITE_SYSTEM_SERVER

// IM 会话读位置 Response VO
export interface ImConversationReadRespVO {
  id: string // 读位置编号（增量拉取游标用）
  conversationType: number // 会话类型，参见 ImConversationType
  targetId: string // 会话目标编号
  messageId: string // 最大已读消息编号
  updateTime?: string // 后端 LocalDateTime，增量拉取器会转换为毫秒时间戳
}

// 增量拉取当前用户的会话读位置（重连 / 离线补偿）
export const pullMyConversationReadList = (params: {
  lastUpdateTime?: number
  lastId?: string
  limit: number
}) => {
  return request.get<ImConversationReadRespVO[]>({
    url: `/${SYS_BASE_URL}/im/conversation-read/pull`,
    params
  })
}
