import request from '@/config/axios'

// AI 聊天对话 VO
export interface ChatConversationVO {
  id: number // ID 编号
  userId: number // 用户编号
  title: string // 对话标题
  pinned: boolean // 是否置顶
  roleId: number // 角色编号
  modelId: number // 模型编号
  model: string // 模型标志
  temperature: number // 温度参数
  maxTokens: number // 单条回复的最大 Token 数量
  maxContexts: number // 上下文的最大 Message 数量
  createTime?: Date // 创建时间
  // 额外字段
  systemMessage?: string // 角色设定
  modelName?: string // 模型名字
  roleAvatar?: string // 角色头像
  modelMaxTokens?: string // 模型的单条回复的最大 Token 数量
  modelMaxContexts?: string // 模型的上下文的最大 Message 数量
}

// AI 聊天对话 API
export const ChatConversationApi = {
  // 获得【我的】聊天对话
  getChatConversationMy: async (id: number) => {
    return await request.get({ url: `/ai/chat/conversation/get-my/${id}` })
  },

  // 新增【我的】聊天对话
  createChatConversationMy: async (data?: ChatConversationVO) => {
    return await request.post({ url: `/ai/chat/conversation/create-my`, data })
  },

  // 更新【我的】聊天对话
  updateChatConversationMy: async (data: ChatConversationVO) => {
    return await request.put({ url: `/ai/chat/conversation/update-my`, data })
  },

  // 删除【我的】聊天对话
  deleteChatConversationMy: async (id: string) => {
    return await request.delete({ url: `/ai/chat/conversation/delete-my/${id}` })
  },

  // 删除【我的】所有对话，置顶除外
  deleteChatConversationMyByUnpinned: async () => {
    return await request.delete({ url: `/ai/chat/conversation/delete-by-unpinned` })
  },

  // 获得【我的】聊天对话列表
  getChatConversationMyList: async () => {
    return await request.get({ url: `/ai/chat/conversation/my-list` })
  },

  // 获得对话分页
  getChatConversationPage: async (params: any) => {
    return await request.get({ url: `/ai/chat/conversation/page`, params })
  },

  // 管理员删除消息
  deleteChatConversationByAdmin: async (id: number) => {
    return await request.delete({ url: `/ai/chat/conversation/delete-by-admin/${id}` })
  }
}
