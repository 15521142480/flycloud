import request from '@/config/axios'
import { getAccessToken } from '@/utils/auth'

const AI_BASE_URL = import.meta.env.VITE_AI_SERVER

/** AI 学习页面支持的聊天实现阶段。 */
export type AiChatStage = 'original' | 'spring' | 'tool' | 'memory' | 'rag' | 'agent' | 'unified'

/** 聊天请求。 */
export interface AiChatRequest {
  message: string
  conversationId?: string
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

/** RAG 命中的知识库片段。 */
export interface AiKnowledgeHit {
  id: string
  content: string
  score?: number
  metadata?: Record<string, unknown>
}

/** 普通聊天响应。 */
export interface AiChatResponse {
  conversationId?: string
  responseId: string
  model: string
  content: string
  permission?: AiPermission
  toolNames?: string[]
  knowledgeReferences?: AiKnowledgeHit[]
  usage?: AiUsage
}

/** RAG 测试接口响应。 */
export interface AiRagChatResponse {
  answer: AiChatResponse
  references: AiKnowledgeHit[]
}

/** Agent 测试接口响应。 */
export interface AiAgentChatResponse {
  response: AiChatResponse
  knowledgeReferences: AiKnowledgeHit[]
}

/** 文本向量化测试结果。 */
export interface AiEmbeddingResult {
  text: string
  dimensions: number
  vector: number[]
}

/** MCP Client 调用结果。 */
export interface AiMcpToolResult {
  toolName: string
  content: string
  error: boolean
}

/** 统一 SSE 聊天事件。 */
export interface AiStreamEvent {
  type: 'conversation' | 'delta' | 'permission' | 'completed' | 'error'
  delta?: string
  responseId?: string
  usage?: AiUsage
  message?: string
  permission?: AiPermission
  conversationId?: string
}

/** AI 会话列表项。 */
export interface AiConversationSummary {
  conversationId: string
  title: string
  lastMessageTime: string
  createTime: string
}

/** AI 历史消息。 */
export interface AiChatHistoryMessage {
  id: string
  role: 'user' | 'assistant' | 'system' | 'tool'
  content: string
  usage?: AiUsage
  permission?: AiPermission
  toolNames?: string[]
  knowledgeReferences?: AiKnowledgeHit[]
  status: 'generating' | 'completed' | 'failed'
  createTime: string
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
  tool: '/ai/tool/chat',
  memory: '/ai/demo/memory/chat',
  rag: '/ai/demo/rag/chat',
  agent: '/ai/demo/agent/chat',
  unified: '/ai/chat'
}

/** 发起一次性返回完整结果的聊天请求。 */
export const chat = (stage: AiChatStage, data: AiChatRequest) => {
  return request.post<AiChatResponse>({
    url: `/${AI_BASE_URL}${chatPaths[stage]}`,
    data
  })
}

/** 执行第 7 步 RAG 问答。 */
export const chatRag = (data: AiChatRequest) => {
  return request.post<AiRagChatResponse>({ url: `/${AI_BASE_URL}/ai/demo/rag/chat`, data })
}

/** 执行第 8 步 Agent 非流式问答。 */
export const chatAgent = (data: AiChatRequest) => {
  return request.post<AiAgentChatResponse>({ url: `/${AI_BASE_URL}/ai/demo/agent/chat`, data })
}

/** 执行第 5 步文本向量化。 */
export const embedText = (text: string) => {
  return request.post<AiEmbeddingResult>({
    url: `/${AI_BASE_URL}/ai/demo/embedding`,
    params: { text }
  })
}

/** 执行第 6 步 Qdrant 相似度检索。 */
export const searchVector = (query: string) => {
  return request.get<AiKnowledgeHit[]>({
    url: `/${AI_BASE_URL}/ai/demo/vector/search`,
    params: { query }
  })
}

/** 通过 MCP Client 查询用户。 */
export const queryMcpUser = (userId: number) => {
  return request.get<AiMcpToolResult>({
    url: `/${AI_BASE_URL}/ai/demo/mcp/user`,
    params: { userId }
  })
}

/** 通过 MCP Client 查询订单。 */
export const queryMcpOrder = (idOrNo: string) => {
  return request.get<AiMcpToolResult>({
    url: `/${AI_BASE_URL}/ai/demo/mcp/order`,
    params: { idOrNo }
  })
}

/** 获取后端当前生效的供应商和默认模型。 */
export const getChatRuntimeConfiguration = () => {
  return request.get<AiChatRuntimeConfiguration>({
    url: `/${AI_BASE_URL}/ai/chat/runtime-configuration`
  })
}

/** 查询当前用户的统一 AI 会话。 */
export const getConversations = () => {
  return request.get<AiConversationSummary[]>({ url: `/${AI_BASE_URL}/ai/chat/conversations` })
}

/** 查询一段统一 AI 会话的历史消息。 */
export const getConversationMessages = (conversationId: string) => {
  return request.get<AiChatHistoryMessage[]>({
    url: `/${AI_BASE_URL}/ai/chat/conversations/${conversationId}/messages`
  })
}

/** 删除当前用户的一段统一 AI 会话。 */
export const deleteConversation = (conversationId: string) => {
  return request.delete({ url: `/${AI_BASE_URL}/ai/chat/conversations/${conversationId}` })
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
  let terminalEventReceived = false
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
        const event = terminalEventReceived ? undefined : parseSseEvent(eventBlock, onEvent)
        if (event?.type === 'completed' || event?.type === 'error') {
          // 终态事件由后端的 SseEmitter.complete() 关闭连接。不能主动 cancel，
          // 否则网关会将客户端主动断开记录为 PrematureCloseException。
          terminalEventReceived = true
        }
        separatorIndex = buffer.indexOf('\n\n')
      }
    }
    if (!terminalEventReceived) {
      parseSseEvent(buffer, onEvent)
    }
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
