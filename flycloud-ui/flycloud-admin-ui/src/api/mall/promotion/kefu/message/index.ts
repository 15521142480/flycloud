import request from '@/config/axios'

const MALL_BASE_URL = import.meta.env.VITE_MALL_SERVER

export interface KeFuMessageRespVO {
  id: number // 编号
  conversationId: number // 会话编号
  senderId: number // 发送人编号
  senderAvatar: string // 发送人头像
  senderType: number // 发送人类型
  receiverId: number // 接收人编号
  receiverType: number // 接收人类型
  contentType: number // 消息类型
  content: string // 消息
  readStatus: boolean // 是否已读
  createTime: Date // 创建时间
}

// 客服会话 API
export const KeFuMessageApi = {
  // 发送客服消息
  sendKeFuMessage: async (data: any) => {
    return await request.post({
      url: `/${MALL_BASE_URL}/promotion/kefu-message/send`,
      data
    })
  },
  // 更新客服消息已读状态
  updateKeFuMessageReadStatus: async (conversationId: number) => {
    return await request.put({
      url:
        `/${MALL_BASE_URL}/promotion/kefu-message/update-read-status?conversationId=` +
        conversationId
    })
  },
  // 获得消息列表（流式加载）
  getKeFuMessageList: async (params: any) => {
    return await request.get({ url: `/${MALL_BASE_URL}/promotion/kefu-message/list`, params })
  }
}
