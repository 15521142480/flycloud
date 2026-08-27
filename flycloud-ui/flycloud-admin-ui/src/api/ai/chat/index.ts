import request from '@/config/axios'
import { getAccessToken } from '@/utils/auth'

const AI_BASE_URL = import.meta.env.VITE_AI_SERVER

/** AI 学习页面支持的聊天实现阶段。 */
export type AiChatStage = 'original' | 'spring' | 'tool'

/** 聊天请求。 */
export interface AiChatRequest {
  message: string
  model?: string
  maxOutputTokens?: number
}

/** 工具调用的资源权限结果。 */
export interface AiPermission {
  granted: boolean
  message: string
}

/** 模型本次调用的 Token 用量。 */
export interface AiUsage {
  inputTokens: number
  outputTokens: number
  totalTokens: number
}

/** 普通聊天响应。 */
export interface AiChatResponse {
  responseId: string
  model: string
  content: string
  permission?: AiPermission
  toolNames?: string[]
  usage?: AiUsage
}

/** 统一 SSE 聊天事件。 */
export interface AiStreamEvent {
  type: 'delta' | 'permission' | 'completed' | 'error'
  delta?: string
  responseId?: string
  usage?: AiUsage
  message?: string
  permission?: AiPermission
}

/** AI 聊天页面运行配置。 */
export interface AiChatRuntimeConfiguration {
  provider: string
  providerName: string
  chatModel: string
  providers: Array<{
    value: string
    label: string
  }>
}

const chatPaths: Record<AiChatStage, string> = {
  original: '/ai/original/chat',
  spring: '/ai/spring/chat',
  tool: '/ai/tool/chat'
}

/** 发起一次性返回完整结果的聊天请求。 */
export const chat = (stage: AiChatStage, data: AiChatRequest) => {
  return request.post<AiChatResponse>({
    url: `/${AI_BASE_URL}${chatPaths[stage]}`,
    data
  })
}

/** 获取后端当前生效的供应商和默认模型。 */
export const getChatRuntimeConfiguration = () => {
  return request.get<AiChatRuntimeConfiguration>({
    url: `/${AI_BASE_URL}/ai/chat/runtime-configuration`
  })
}

/**
 * 发起 POST SSE 流式聊天请求。
 *
 * 原生 EventSource 只支持 GET，项目聊天接口为携带 JSON 请求体且需要 Authorization 的 POST，
 * 因此使用 Fetch 读取 SSE 数据流，并继续沿用后端统一的 delta、completed、error 事件协议。
 */
export const streamChat = async (
  stage: AiChatStage,
  data: AiChatRequest,
  onEvent: (event: AiStreamEvent) => void
): Promise<void> => {
  const token = getAccessToken()
  const response = await fetch(`/${AI_BASE_URL}${chatPaths[stage]}/stream`, {
    method: 'POST',
    headers: {
      Accept: 'text/event-stream',
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {})
    },
    body: JSON.stringify(data)
  })

  if (!response.ok || !response.body) {
    const error = await readErrorMessage(response)
    throw new Error(error)
  }

  const reader = response.body.getReader()
  const decoder = new TextDecoder()
  let buffer = ''
  try {
    while (true) {
      const { done, value } = await reader.read()
      if (done) {
        break
      }
      buffer += decoder.decode(value, { stream: true })
      buffer = buffer.replace(/\r\n/g, '\n')
      let separatorIndex = buffer.indexOf('\n\n')
      while (separatorIndex >= 0) {
        const eventBlock = buffer.slice(0, separatorIndex)
        buffer = buffer.slice(separatorIndex + 2)
        const event = parseSseEvent(eventBlock, onEvent)
        if (event?.type === 'completed' || event?.type === 'error') {
          await reader.cancel()
          return
        }
        separatorIndex = buffer.indexOf('\n\n')
      }
    }
    parseSseEvent(buffer, onEvent)
  } finally {
    reader.releaseLock()
  }
}

/** 解析单个 SSE 事件块。 */
const parseSseEvent = (
  eventBlock: string,
  onEvent: (event: AiStreamEvent) => void
): AiStreamEvent | undefined => {
  if (!eventBlock.trim()) {
    return undefined
  }
  const data = eventBlock
    .split('\n')
    .filter((line) => line.startsWith('data:'))
    .map((line) => line.slice(5).trimStart())
    .join('\n')
  if (!data) {
    return undefined
  }
  try {
    const event = JSON.parse(data) as AiStreamEvent
    onEvent(event)
    return event
  } catch {
    // 非项目协议的 SSE 数据不参与页面渲染，避免将网关诊断文本误作为模型回答展示。
    return undefined
  }
}

/** 从失败响应中尽可能提取后端错误信息。 */
const readErrorMessage = async (response: Response): Promise<string> => {
  try {
    const body = (await response.json()) as { msg?: string; message?: string }
    return body.msg || body.message || `流式聊天请求失败（HTTP ${response.status}）`
  } catch {
    return `流式聊天请求失败（HTTP ${response.status}）`
  }
}
