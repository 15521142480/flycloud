<template>
  <div class="ai-learning-page">
    <aside class="learning-steps">
      <slot
        name="sidebar"
        :active-conversation-id="activeConversationId"
        :active-scope="activeScope"
        :active-step-id="activeStepId"
        :conversations="conversations"
        :create-conversation="createConversation"
        :learning-steps="learningSteps"
        :remove-conversation="removeConversation"
        :rename-conversation="renameConversation"
        :select-conversation="selectConversation"
        :select-step="selectStep"
      ></slot>
    </aside>
    <main class="chat-panel">
      <section ref="messagePanelRef" class="message-panel" aria-live="polite">
        <div v-if="messages.length === 0" class="empty-state"
          ><Icon icon="ep:connection" class="empty-icon" /><h2>{{ activeDefinition.emptyTitle }}</h2
          ><p>{{ activeDefinition.emptyDescription }}</p
          ><div class="example-actions"
            ><el-button
              v-for="example in activeDefinition.examples"
              :key="example"
              @click="fillExample(example)"
              >{{ example }}</el-button
            ></div
          ></div
        >
        <article v-for="item in messages" :key="item.id" class="message-row" :class="item.role"
          ><el-avatar
            v-if="item.role === 'user'"
            :src="getFilePreviewUrl(currentUserAvatar)"
            class="message-avatar user-avatar"
            alt="当前用户头像"
          /><div v-else class="message-avatar"><Icon icon="ep:cpu" /></div
          ><div class="message-content"
            ><div v-if="item.permission || item.toolNames.length" class="tool-meta"
              ><el-tag
                v-if="item.permission"
                class="permission-tag"
                size="small"
                :type="item.permission.granted ? 'success' : 'danger'"
                effect="light"
                >{{ item.permission.message }}</el-tag
              ><el-tag
                v-for="toolName in item.toolNames"
                :key="toolName"
                size="small"
                type="info"
                >{{ toolName }}</el-tag
              ></div
            ><div v-if="item.loading" class="loading-indicator"
              ><el-icon class="is-loading"><Loading /></el-icon><span>{{ loadingText }}</span></div
            ><div v-if="item.content" class="message-text">{{ item.content }}</div
            ><div v-if="hasTokenUsage(item.usage)" class="usage-meta"
              ><Icon icon="ep:data-analysis" /><span
                >Token 用量：输入 {{ item.usage.inputTokens }} · 输出
                {{ item.usage.outputTokens }} · 总计 {{ item.usage.totalTokens }}</span
              ></div
            ></div
          ></article
        >
      </section>
      <footer class="chat-input-area"
        ><form class="composer-form" @submit.prevent="sendMessage"
          ><el-input
            v-model="inputMessage"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6 }"
            maxlength="20000"
            show-word-limit
            resize="none"
            :placeholder="activeDefinition.placeholder"
            :disabled="sending"
            @keydown.enter.exact.prevent="sendMessage" /><div class="input-footer"
            ><span>Enter 发送，Shift + Enter 换行</span
            ><div class="chat-settings"
              ><el-select v-model="chatMode" class="chat-setting" size="small" :disabled="sending"
                ><el-option
                  v-for="mode in availableChatModes"
                  :key="mode.value"
                  :label="mode.label"
                  :value="mode.value" /></el-select
              ><el-select
                v-model="runtimeConfiguration.provider"
                class="chat-setting"
                size="small"
                disabled
                ><el-option
                  v-for="provider in runtimeConfiguration.providers"
                  :key="provider.value"
                  :label="provider.label"
                  :value="provider.value" /></el-select
              ><el-select
                v-model="runtimeConfiguration.chatModel"
                class="chat-model-setting"
                size="small"
                disabled
                ><el-option
                  :label="runtimeConfiguration.chatModel"
                  :value="runtimeConfiguration.chatModel" /></el-select
              ><el-button
                type="primary"
                native-type="submit"
                :loading="sending"
                :disabled="!inputMessage.trim()"
                >发送 <Icon icon="ep:promotion" class="send-icon" /></el-button></div></div></form
      ></footer>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Loading } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import avatarImg from '@/assets/imgs/avatar.png'
import { getFilePreviewUrl } from '@/components/UploadFile/src/useUpload'
import { useUserStore } from '@/store/modules/user'
import {
  chat,
  chatAgent,
  chatRag,
  deleteConversation,
  embedText,
  getChatRuntimeConfiguration,
  getConversationMessages,
  getConversations,
  queryMcpOrder,
  queryMcpUser,
  renameConversation as renameConversationApi,
  searchVector,
  streamChat,
  type AiChatResponse,
  type AiChatStage,
  type AiConversationSummary,
  type AiPermission,
  type AiStreamEvent,
  type AiUsage
} from '@/api/ai/chat'

defineOptions({ name: 'AiWorkspace' })
type ChatMode = 'normal' | 'stream'
type ChatScope = 'unified' | 'laboratory'
interface AiWorkspaceProps {
  initialScope?: ChatScope
}
type DemoAction = 'embedding' | 'vector' | 'mcp'
const props = withDefaults(defineProps<AiWorkspaceProps>(), { initialScope: 'unified' })
interface ChatDefinition {
  name: string
  chatStage: AiChatStage
  placeholder: string
  emptyTitle: string
  emptyDescription: string
  examples: string[]
  demoAction?: DemoAction
}
interface LearningStep extends ChatDefinition {
  id: number
  status: string
  completed: boolean
  available: boolean
}
interface ChatMessage {
  id: string
  role: 'user' | 'assistant'
  content: string
  permission?: AiPermission
  toolNames: string[]
  usage?: AiUsage
  loading?: boolean
}
const message = useMessage()
const userStore = useUserStore()
const messagePanelRef = ref<HTMLElement>()
const inputMessage = ref('')
const sending = ref(false)
const activeScope = ref<ChatScope>(props.initialScope)
const activeStepId = ref(4)
const activeConversationId = ref<string>()
const conversations = ref<AiConversationSummary[]>([])
const messages = ref<ChatMessage[]>([])
const chatMode = ref<ChatMode>('stream')
const currentUserAvatar = computed(() => userStore.user.avatar || avatarImg)
const runtimeConfiguration = reactive({
  provider: 'dashscope',
  chatModel: 'qwen-plus',
  providers: [
    { value: 'openai', label: 'OpenAI' },
    { value: 'deepseek', label: 'DeepSeek' },
    { value: 'dashscope', label: '阿里云百炼' }
  ]
})
const chatModes: Array<{ value: ChatMode; label: string }> = [
  { value: 'stream', label: '流式聊天' },
  { value: 'normal', label: '普通聊天' }
]
const unifiedDefinition: ChatDefinition = {
  name: 'AI 助手',
  chatStage: 'unified',
  placeholder: '请输入问题，例如：查询订单 10001',
  emptyTitle: '开始 AI 助手',
  emptyDescription: '自动使用会话记忆，并在需要时调用已接入的受控业务工具。',
  examples: ['查询订单 ID M202607040355023193520 的信息', '查询用户 ID 2 的信息', '查询订单 ID 2073133434168320001，并根据公司退款规则判断能否退款']
}
const learningSteps: LearningStep[] = [
  {
    id: 1,
    name: '原生 HTTP 调模型 API',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'original',
    placeholder: '请输入问题，例如：用一句话介绍飞翔云',
    emptyTitle: '开始原生 HTTP 模型聊天',
    emptyDescription: '通过 JDK HttpClient 直接调用当前配置的模型接口。',
    examples: ['用一句话介绍飞翔云', '解释什么是 SSE 流式响应']
  },
  {
    id: 2,
    name: 'Spring AI 调模型',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'spring',
    placeholder: '请输入问题，例如：解释 Spring AI 的价值',
    emptyTitle: '开始 Spring AI 模型聊天',
    emptyDescription: '通过 Spring AI ChatClient 调用当前配置的模型。',
    examples: ['解释 Spring AI 的价值']
  },
  {
    id: 3,
    name: 'Tool Calling / 工具调用',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'tool',
    placeholder: '请输入问题，例如：查询订单 ID 10001 的信息',
    emptyTitle: '开始一次受控的业务查询',
    emptyDescription: '模型可调用用户和订单工具，服务端会按当前登录用户二次授权。',
    examples: ['查询用户 ID 2 的信息', '查询订单 ID 2073133434168320001 的信息']
  },
  {
    id: 4,
    name: 'Chat Memory / 多轮对话',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'memory',
    placeholder: '请输入问题，例如：先介绍一个订单，再问这个订单是谁购买的',
    emptyTitle: '开始 Chat Memory 多轮对话',
    emptyDescription: '完整历史保存到 MySQL，最近上下文由 Spring AI ChatMemory 保存到 Redis。',
    examples: ['查询订单 ID 2073133434168320001 的信息', '这个订单是谁买的？']
  },
  {
    id: 5,
    name: 'Embedding / 向量化',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'memory',
    placeholder: '请输入文本，后续可在 Embedding 测试接口观察向量结果',
    emptyTitle: 'Embedding / 向量化',
    emptyDescription: '已接入百炼 text-embedding-v4 与正式向量化链路。',
    examples: ['飞翔云是一体化智能协同平台'],
    demoAction: 'embedding'
  },
  {
    id: 6,
    name: 'Vector Store',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'memory',
    placeholder: '请输入检索词，后续可在 Vector Store 测试接口观察检索结果',
    emptyTitle: 'Vector Store',
    emptyDescription: '已使用 Qdrant 保存并检索知识库向量。',
    examples: ['退款规定'],
    demoAction: 'vector'
  },
  {
    id: 7,
    name: 'RAG',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'rag',
    placeholder: '请输入问题，例如：公司的退款规定是什么？',
    emptyTitle: 'RAG',
    emptyDescription: '从 Qdrant 检索相关知识后，再由模型基于知识回答。',
    examples: ['公司的退款规定是什么？']
  },
  {
    id: 8,
    name: 'Agent',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'agent',
    placeholder: '请输入多步骤任务，例如：查询订单并根据退款规则判断是否可退款',
    emptyTitle: 'Agent',
    emptyDescription: '可组合知识检索与受控业务工具执行多步骤任务。',
    examples: ['查询订单 ID 2073133434168320001，并根据公司退款规则判断能否退款']
  },
  {
    id: 9,
    name: 'MCP',
    status: '已完成',
    completed: true,
    available: true,
    chatStage: 'memory',
    placeholder: '请输入 MCP 测试问题',
    emptyTitle: 'MCP',
    emptyDescription: '已通过 Spring AI MCP 暴露并调用用户、订单查询工具。',
    examples: ['查询用户 ID 2 的信息', '查询订单 ID 2073133434168320001 的信息'],
    demoAction: 'mcp'
  }
]
const activeStep = computed(
  () => learningSteps.find((step) => step.id === activeStepId.value) || learningSteps[3]
)
const activeDefinition = computed<ChatDefinition>(() =>
  activeScope.value === 'unified' ? unifiedDefinition : activeStep.value
)
/** 仅暴露后端当前已提供流式接口的聊天模式，避免向不存在的流式地址发送请求。 */
const availableChatModes = computed(() =>
  activeDefinition.value.demoAction || activeDefinition.value.chatStage === 'rag'
    ? chatModes.filter((item) => item.value === 'normal')
    : chatModes
)
const loadingText = computed(() =>
  activeDefinition.value.demoAction
    ? '正在执行实验接口…'
    : ['tool', 'unified', 'memory', 'agent'].includes(activeDefinition.value.chatStage)
    ? '正在选择工具并加载会话上下文…'
    : '正在请求模型服务…'
)
/** 新建正式会话；首次发送时由后端分配会话编号。 */
const createConversation = () => {
  activeScope.value = 'unified'
  activeConversationId.value = undefined
  messages.value = []
  inputMessage.value = ''
  chatMode.value = 'stream'
}
/** 选择可用实验步骤。 */
const selectStep = (step: LearningStep) => {
  if (!step.available) {
    message.info(`${step.name}尚未进入开发阶段`)
    return
  }
  activeScope.value = 'laboratory'
  activeStepId.value = step.id
  activeConversationId.value = undefined
  messages.value = []
  inputMessage.value = ''
  chatMode.value = ['embedding', 'vector', 'mcp', 'rag'].includes(step.demoAction || '')
    ? 'normal'
    : 'stream'
}
/** 载入正式会话的完整历史。 */
const selectConversation = async (conversationId: string) => {
  if (sending.value) return
  activeScope.value = 'unified'
  activeConversationId.value = conversationId
  const history = await getConversationMessages(conversationId)
  messages.value = history
    .filter((item) => item.role === 'user' || item.role === 'assistant')
    .map((item) => ({
      id: item.id,
      role: item.role as 'user' | 'assistant',
      content: item.content,
      permission: item.permission,
      toolNames: item.toolNames || [],
      usage: item.usage,
      loading: item.status === 'generating'
    }))
  await scrollToBottom()
}
/** 删除一段正式会话。 */
const removeConversation = async (conversationId: string) => {
  if (sending.value) {
    message.warning('当前正在生成回答，请结束后再删除会话')
    return
  }
  try {
    await message.delConfirm('删除后将无法恢复该对话及其完整聊天记录，确定删除吗？', '删除对话')
  } catch {
    return
  }
  await deleteConversation(conversationId)
  if (activeConversationId.value === conversationId) createConversation()
  await loadConversations()
  message.success('对话已删除')
}
/** 重命名当前用户的一段正式会话。 */
const renameConversation = async (conversation: AiConversationSummary) => {
  if (sending.value) {
    message.warning('当前正在生成回答，请结束后再重命名会话')
    return
  }
  try {
    const { value } = await ElMessageBox.prompt('请输入新的会话名称', '重命名会话', {
      inputValue: conversation.title,
      inputValidator: (input: string) => {
        const title = input.trim()
        return (title.length > 0 && title.length <= 40) || '会话名称应为 1 至 40 个字符'
      },
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    await renameConversationApi(conversation.conversationId, value.trim())
    await loadConversations()
    message.success('会话已重命名')
  } catch {
    // 用户取消时无需提示。
  }
}
/** 填入示例消息。 */ const fillExample = (example: string) => {
  inputMessage.value = example
}
/** 按当前正式入口或实验步骤发送消息。 */
const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || sending.value) return
  const stage = activeDefinition.value.chatStage
  const request = {
    message: content,
    ...(stage === 'unified' || stage === 'memory'
      ? { conversationId: activeConversationId.value }
      : {})
  }
  messages.value.push({ id: `user-${Date.now()}`, role: 'user', content, toolNames: [] })
  const assistantMessage = reactive<ChatMessage>({
    id: `assistant-${Date.now()}`,
    role: 'assistant',
    content: '',
    toolNames: [],
    loading: true
  })
  messages.value.push(assistantMessage)
  inputMessage.value = ''
  sending.value = true
  await scrollToBottom()
  try {
    if (activeDefinition.value.demoAction) {
      assistantMessage.content = await executeNonChatDemo(activeDefinition.value.demoAction, content)
    } else if (stage === 'rag') {
      const response = await chatRag(request)
      Object.assign(assistantMessage, toAssistantMessage(response.answer))
    } else if (stage === 'agent' && chatMode.value === 'normal') {
      const response = await chatAgent(request)
      Object.assign(assistantMessage, toAssistantMessage(response.response))
    } else if (chatMode.value === 'normal') {
      const response = await chat(stage, request)
      Object.assign(assistantMessage, toAssistantMessage(response))
      applyConversationId(response.conversationId)
    } else await streamChat(stage, request, (event) => applyStreamEvent(assistantMessage, event))
  } catch (error) {
    assistantMessage.content = error instanceof Error ? error.message : '模型请求失败，请稍后重试'
    message.error(assistantMessage.content)
  } finally {
    assistantMessage.loading = false
    sending.value = false
    if (stage === 'unified' || stage === 'memory') await loadConversations()
    await scrollToBottom()
  }
}
/** 调用不以聊天协议返回的独立学习接口。 */
const executeNonChatDemo = async (action: DemoAction, content: string): Promise<string> => {
  if (action === 'embedding') {
    const result = await embedText(content)
    return `向量维度：${result.dimensions}\n向量前 10 维：${result.vector.slice(0, 10).join(', ')}`
  }
  if (action === 'vector') {
    const hits = await searchVector(content)
    return hits.length
      ? hits.map((item, index) => `${index + 1}. 相似度 ${item.score ?? '-'}\n${item.content}`).join('\n\n')
      : '未检索到相似知识片段。'
  }
  return executeMcpDemo(content)
}
/** 解析 MCP 演示输入，并调用对应的 MCP Client 接口。 */
const executeMcpDemo = async (content: string): Promise<string> => {
  const identifier = content.match(/M[0-9A-Za-z]+|\d+/i)?.[0]
  if (!identifier) throw new Error('MCP 测试请输入用户 ID 或订单 ID / 流水号')
  const result = content.includes('订单')
    ? await queryMcpOrder(identifier)
    : await queryMcpUser(Number(identifier))
  if (result.error) throw new Error(result.content)
  return result.content
}
/** 转换普通响应。 */ const toAssistantMessage = (
  response: AiChatResponse
): Partial<ChatMessage> => ({
  content: response.content,
  permission: response.permission,
  toolNames: response.toolNames || [],
  usage: response.usage
})
/** 应用后端统一 SSE 事件。 */ const applyStreamEvent = (
  assistantMessage: ChatMessage,
  event: AiStreamEvent
) => {
  if (event.type === 'conversation') {
    applyConversationId(event.conversationId)
    return
  }
  if (event.type === 'permission') {
    assistantMessage.permission = event.permission
    return
  }
  if (event.type === 'delta') {
    assistantMessage.content += event.delta || ''
    void scrollToBottom()
    return
  }
  if (event.type === 'completed') {
    assistantMessage.usage = event.usage
    return
  }
  if (event.type === 'error') assistantMessage.content = event.message || '模型流式响应失败'
}
/** 保存服务端新建的会话编号。 */ const applyConversationId = (conversationId?: string) => {
  if (conversationId && ['unified', 'memory'].includes(activeDefinition.value.chatStage))
    activeConversationId.value = conversationId
}
/** 判断 Token 是否有效。 */ const hasTokenUsage = (usage?: AiUsage) =>
  Boolean(usage && usage.totalTokens > 0)
/** 加载当前模型运行配置。 */ const loadRuntimeConfiguration = async () => {
  try {
    const configuration = await getChatRuntimeConfiguration()
    runtimeConfiguration.provider = configuration.provider
    runtimeConfiguration.chatModel = configuration.chatModel
    runtimeConfiguration.providers = configuration.providers
  } catch {
    /* 保留内置展示值 */
  }
}
/** 加载当前用户的会话列表。 */ const loadConversations = async () => {
  try {
    conversations.value = await getConversations()
  } catch {
    /* Axios 已统一处理错误 */
  }
}
/** 滚动消息区到底部。 */ const scrollToBottom = async () => {
  await nextTick()
  if (messagePanelRef.value) messagePanelRef.value.scrollTop = messagePanelRef.value.scrollHeight
}
onMounted(() => {
  void loadRuntimeConfiguration()
  if (props.initialScope === 'unified') void loadConversations()
})
</script>

<style scoped lang="scss">
@use '../styles/workspace.scss';
</style>
