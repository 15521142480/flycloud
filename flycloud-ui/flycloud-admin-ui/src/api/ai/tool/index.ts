import request from '@/config/axios'

const AI_BASE_URL = import.meta.env.VITE_AI_SERVER

/** Tool Calling 聊天请求。 */
export interface AiToolCallingChatRequest {
  message: string
  model?: string
  maxOutputTokens?: number
}

/** Tool Calling 聊天响应。 */
export interface AiToolCallingChatResponse {
  responseId: string
  model: string
  content: string
  permissionMessage?: string
  toolNames: string[]
  usage?: {
    promptTokens: number
    completionTokens: number
    totalTokens: number
  }
}

/** 发起 Tool Calling 聊天请求。 */
export const toolCallingChat = (data: AiToolCallingChatRequest) => {
  return request.post<AiToolCallingChatResponse>({
    url: `/${AI_BASE_URL}/ai/tool/chat`,
    data
  })
}
