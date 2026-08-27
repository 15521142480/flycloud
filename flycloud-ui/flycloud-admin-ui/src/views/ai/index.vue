<template>
  <div class="ai-learning-page">
    <aside class="learning-steps" aria-label="AI 学习阶段">
      <div class="steps-header">
        <div class="steps-title">AI 管理</div>
        <div class="steps-subtitle">从模型调用到智能体的实践路线</div>
      </div>

      <button
        v-for="step in learningSteps"
        :key="step.id"
        class="step-item"
        :class="{ active: activeStepId === step.id, completed: step.completed, unavailable: !step.available }"
        type="button"
        @click="selectStep(step)"
      >
        <span class="step-index">{{ step.id }}</span>
        <span class="step-copy">
          <span class="step-name">{{ step.name }}</span>
          <span class="step-status">{{ step.status }}</span>
        </span>
        <Icon v-if="step.completed" icon="ep:circle-check-filled" class="step-icon" />
        <Icon v-else-if="step.available" icon="ep:arrow-right" class="step-icon" />
        <Icon v-else icon="ep:lock" class="step-icon" />
      </button>

      <div class="security-tip">
        <Icon icon="ep:shield-check" />
        <span>工具调用始终以当前登录用户身份进行服务端授权。</span>
      </div>
    </aside>

    <main class="chat-panel">
      <section ref="messagePanelRef" class="message-panel" aria-live="polite">
        <div v-if="messages.length === 0" class="empty-state">
          <Icon icon="ep:connection" class="empty-icon" />
          <h2>{{ activeStep.emptyTitle }}</h2>
          <p>{{ activeStep.emptyDescription }}</p>
          <div class="example-actions">
            <el-button v-for="example in activeStep.examples" :key="example" @click="fillExample(example)">
              {{ example }}
            </el-button>
          </div>
        </div>

        <article v-for="item in messages" :key="item.id" class="message-row" :class="item.role">
          <el-avatar
            v-if="item.role === 'user'"
            :src="getFilePreviewUrl(currentUserAvatar)"
            class="message-avatar user-avatar"
            alt="当前用户头像"
          />
          <div v-else class="message-avatar"><Icon icon="ep:cpu" /></div>
          <div class="message-content">
            <div class="message-role">{{ item.role === 'user' ? '我' : '飞翔云 AI' }}</div>
            <div v-if="item.loading" class="loading-indicator">
              <el-icon class="is-loading"><Loading /></el-icon>
              <span>{{ loadingText }}</span>
            </div>
            <div v-if="item.content" class="message-text">{{ item.content }}</div>
            <div v-if="item.permissionMessage" class="tool-meta">
              <el-tag size="small" type="success">{{ item.permissionMessage }}</el-tag>
              <el-tag v-for="toolName in item.toolNames" :key="toolName" size="small" type="info">
                {{ toolName }}
              </el-tag>
            </div>
          </div>
        </article>

      </section>

      <footer class="chat-input-area">
        <form class="composer-form" @submit.prevent="sendMessage">
            <el-input
              v-model="inputMessage"
              type="textarea"
              :autosize="{ minRows: 3, maxRows: 6 }"
              maxlength="20000"
              show-word-limit
              resize="none"
              :placeholder="activeStep.placeholder"
              :disabled="sending"
              @keydown.enter.exact.prevent="sendMessage"
            />
            <div class="input-footer">
              <span>Enter 发送，Shift + Enter 换行</span>
              <div class="chat-settings">
                <el-select v-model="chatMode" class="chat-setting" size="small" :disabled="sending">
                  <el-option v-for="mode in chatModes" :key="mode.value" :label="mode.label" :value="mode.value" />
                </el-select>
                <el-select v-model="runtimeConfiguration.provider" class="chat-setting" size="small" disabled>
                  <el-option
                    v-for="provider in runtimeConfiguration.providers"
                    :key="provider.value"
                    :label="provider.label"
                    :value="provider.value"
                  />
                </el-select>
                <el-select v-model="runtimeConfiguration.chatModel" class="chat-model-setting" size="small" disabled>
                  <el-option :label="runtimeConfiguration.chatModel" :value="runtimeConfiguration.chatModel" />
                </el-select>
                <el-button type="primary" native-type="submit" :loading="sending" :disabled="!inputMessage.trim()">
                  发送
                  <Icon icon="ep:promotion" class="send-icon" />
                </el-button>
              </div>
            </div>
        </form>
      </footer>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Loading } from '@element-plus/icons-vue'
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import avatarImg from '@/assets/imgs/avatar.png'
import { getFilePreviewUrl } from '@/components/UploadFile/src/useUpload'
import { useUserStore } from '@/store/modules/user'
import {
  chat,
  getChatRuntimeConfiguration,
  streamChat,
  type AiChatResponse,
  type AiChatStage,
  type AiStreamEvent
} from '@/api/ai/chat'

defineOptions({ name: 'AiManagement' })

interface LearningStep {
  id: number
  name: string
  status: string
  description: string
  completed: boolean
  available: boolean
  chatStage: AiChatStage
  placeholder: string
  emptyTitle: string
  emptyDescription: string
  examples: string[]
}

type ChatMode = 'normal' | 'stream'

interface ChatMessage {
  id: number
  role: 'user' | 'assistant'
  content: string
  permissionMessage?: string
  toolNames: string[]
  loading?: boolean
}

const message = useMessage()
const userStore = useUserStore()
const messagePanelRef = ref<HTMLElement>()
const inputMessage = ref('')
const sending = ref(false)
const activeStepId = ref(3)
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

const chatModes: Array<{ value: ChatMode; label: string; icon: string }> = [
  { value: 'stream', label: '流式聊天', icon: 'ep:connection' },
  { value: 'normal', label: '普通聊天', icon: 'ep:chat-dot-round' }
]

const learningSteps: LearningStep[] = [
  {
    id: 1,
    name: '原生 HTTP 调模型 API',
    status: '已完成',
    description: '已完成 HTTP、JSON、流式响应等原生接口实践。',
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
    description: '已完成 Spring AI 多供应商模型调用实践。',
    completed: true,
    available: true,
    chatStage: 'spring',
    placeholder: '请输入问题，例如：解释 Spring AI 的价值',
    emptyTitle: '开始 Spring AI 模型聊天',
    emptyDescription: '通过 Spring AI ChatClient 调用当前配置的模型。',
    examples: ['解释 Spring AI 的价值', '用三点说明什么是 Tool Calling']
  },
  {
    id: 3,
    name: 'Tool Calling / 工具调用',
    status: '当前学习',
    description: '模型可调用后端业务工具，订单数据在服务端按当前登录用户二次授权。',
    completed: false,
    available: true,
    chatStage: 'tool',
    placeholder: '请输入问题，例如：查询订单 ID 10001 的信息',
    emptyTitle: '开始一次受控的业务查询',
    emptyDescription: '例如：查询用户 ID 1 的信息；或查询订单 ID 10001 的信息。',
    examples: ['查询用户 ID 1 的信息', '查询订单 ID 10001 的信息']
  },
  {
    id: 4,
    name: 'Chat Memory / 多轮对话',
    status: '待学习',
    description: '下一阶段将实现持久化上下文记忆。',
    completed: false,
    available: false,
    chatStage: 'spring',
    placeholder: '',
    emptyTitle: '',
    emptyDescription: '',
    examples: []
  },
  {
    id: 5,
    name: 'Embedding / 向量数据库',
    status: '待学习',
    description: '下一阶段将实现文本向量化。',
    completed: false,
    available: false,
    chatStage: 'spring',
    placeholder: '',
    emptyTitle: '',
    emptyDescription: '',
    examples: []
  },
  {
    id: 6,
    name: 'Vector Store',
    status: '待学习',
    description: '下一阶段将实现向量存储。',
    completed: false,
    available: false,
    chatStage: 'spring',
    placeholder: '',
    emptyTitle: '',
    emptyDescription: '',
    examples: []
  },
  {
    id: 7,
    name: 'RAG',
    status: '待学习',
    description: '下一阶段将实现检索增强生成。',
    completed: false,
    available: false,
    chatStage: 'spring',
    placeholder: '',
    emptyTitle: '',
    emptyDescription: '',
    examples: []
  },
  {
    id: 8,
    name: 'Agent',
    status: '待学习',
    description: '下一阶段将实现多步骤自主任务执行。',
    completed: false,
    available: false,
    chatStage: 'spring',
    placeholder: '',
    emptyTitle: '',
    emptyDescription: '',
    examples: []
  },
  {
    id: 9,
    name: 'MCP',
    status: '待学习',
    description: '下一阶段将接入模型上下文协议。',
    completed: false,
    available: false,
    chatStage: 'spring',
    placeholder: '',
    emptyTitle: '',
    emptyDescription: '',
    examples: []
  }
]

const activeStep = computed(() => learningSteps.find((step) => step.id === activeStepId.value) || learningSteps[2])

const loadingText = computed(() =>
  activeStep.value.chatStage === 'tool' ? '正在选择工具并校验业务权限…' : '正在请求模型服务…'
)

/** 选择学习阶段。 */
const selectStep = (step: LearningStep) => {
  if (!step.available) {
    message.info(`${step.name}尚未进入开发阶段`)
    return
  }
  if (activeStepId.value === step.id) {
    return
  }
  activeStepId.value = step.id
  chatMode.value = 'stream'
  messages.value = []
  inputMessage.value = ''
}

/** 填入示例问题。 */
const fillExample = (example: string) => {
  inputMessage.value = example
}

/** 按当前学习阶段和聊天模式发送消息。 */
const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || sending.value) {
    return
  }
  messages.value.push({ id: Date.now(), role: 'user', content, toolNames: [] })
  const assistantMessage = reactive<ChatMessage>({
    id: Date.now() + 1,
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
    if (chatMode.value === 'normal') {
      const response = await chat(activeStep.value.chatStage, { message: content })
      Object.assign(assistantMessage, toAssistantMessage(response))
    } else {
      await sendStreamMessage(content, assistantMessage)
    }
  } catch (error) {
    assistantMessage.content = error instanceof Error ? error.message : '模型请求失败，请稍后重试'
    if (chatMode.value === 'stream') {
      message.error(error instanceof Error ? error.message : '流式聊天请求失败')
    }
  } finally {
    assistantMessage.loading = false
    sending.value = false
    await scrollToBottom()
  }
}

/** 转换服务端普通聊天响应为页面消息。 */
const toAssistantMessage = (response: AiChatResponse): ChatMessage => {
  return {
    id: Date.now() + 1,
    role: 'assistant',
    content: response.content,
    permissionMessage: response.permissionMessage,
    toolNames: response.toolNames || []
  }
}

/** 发起流式聊天并将 SSE 增量实时写入当前助手消息。 */
const sendStreamMessage = async (content: string, assistantMessage: ChatMessage) => {
  await streamChat(activeStep.value.chatStage, { message: content }, (event) =>
    applyStreamEvent(assistantMessage, event)
  )
}

/** 应用服务端统一 SSE 聊天事件。 */
const applyStreamEvent = (assistantMessage: ChatMessage, event: AiStreamEvent) => {
  if (event.type === 'delta') {
    assistantMessage.content += event.delta || ''
    void scrollToBottom()
    return
  }
  if (event.type === 'error') {
    assistantMessage.content = event.message || '模型流式响应失败'
  }
}

/** 加载后端当前生效的 AI 运行配置。 */
const loadRuntimeConfiguration = async () => {
  try {
    const configuration = await getChatRuntimeConfiguration()
    runtimeConfiguration.provider = configuration.provider
    runtimeConfiguration.chatModel = configuration.chatModel
    runtimeConfiguration.providers = configuration.providers
  } catch {
    // 页面保留内置展示值；Axios 已统一提示后端错误，不影响已有聊天接口继续使用。
  }
}

onMounted(() => {
  void loadRuntimeConfiguration()
})

/** 将消息区域滚动至底部。 */
const scrollToBottom = async () => {
  await nextTick()
  if (messagePanelRef.value) {
    messagePanelRef.value.scrollTop = messagePanelRef.value.scrollHeight
  }
}
</script>

<style scoped lang="scss">
.ai-learning-page {
  display: flex;
  height: calc(
    100vh - var(--top-tool-height) - var(--tags-view-height) - var(--app-footer-height) -
      var(--app-content-padding) - var(--app-content-padding)
  );
  min-height: 0;
  overflow: hidden;
  background: #fff;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 10px;
}

.learning-steps {
  display: flex;
  width: 310px;
  flex: 0 0 310px;
  flex-direction: column;
  padding: 22px 14px;
  overflow-y: auto;
  background: #f7f9fc;
  border-right: 1px solid var(--el-border-color-lighter);
}

.steps-header {
  padding: 0 10px 18px;
}

.steps-title,
.chat-title {
  color: var(--el-text-color-primary);
  font-size: 18px;
  font-weight: 600;
}

.steps-subtitle,
.chat-description {
  margin-top: 7px;
  color: var(--el-text-color-secondary);
  font-size: 13px;
  line-height: 1.5;
}

.step-item {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 10px;
  padding: 12px 10px;
  margin-bottom: 4px;
  color: var(--el-text-color-regular);
  text-align: left;
  cursor: pointer;
  background: transparent;
  border: 0;
  border-radius: 8px;

  &:hover:not(.unavailable),
  &.active {
    color: var(--el-color-primary);
    background: #ecf5ff;
  }

  &.unavailable {
    cursor: not-allowed;
    opacity: 0.56;
  }
}

.step-index {
  display: inline-flex;
  width: 24px;
  height: 24px;
  flex: 0 0 24px;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  background: #e8edf5;
  border-radius: 50%;

  .active &,
  .completed & {
    color: #fff;
    background: var(--el-color-primary);
  }
}

.step-copy {
  display: flex;
  min-width: 0;
  flex: 1;
  flex-direction: column;
  gap: 3px;
}

.step-name {
  overflow: hidden;
  font-size: 14px;
  font-weight: 500;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.step-status {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.step-icon {
  font-size: 16px;
}

.security-tip {
  display: flex;
  gap: 8px;
  padding: 12px;
  margin-top: auto;
  color: #4b617e;
  font-size: 12px;
  line-height: 1.55;
  background: #eef6ff;
  border-radius: 8px;
}

.chat-panel {
  display: flex;
  min-width: 0;
  flex: 1;
  flex-direction: column;
}

.message-panel {
  flex: 1;
  padding: 24px max(24px, 3.5%);
  overflow-y: auto;
  background: linear-gradient(180deg, #fbfcff 0%, #fff 180px);
}

.empty-state {
  max-width: 560px;
  padding-top: 13vh;
  margin: auto;
  color: var(--el-text-color-secondary);
  text-align: center;

  h2 {
    margin: 16px 0 10px;
    color: var(--el-text-color-primary);
    font-size: 20px;
  }

  p {
    margin: 0;
    line-height: 1.7;
  }
}

.empty-icon {
  color: var(--el-color-primary);
  font-size: 40px;
}

.example-actions {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 22px;
}

.message-row {
  display: flex;
  gap: 12px;
  width: min(920px, 76%);
  margin-bottom: 22px;

  &.assistant {
    margin-right: auto;
    margin-left: max(24px, 3.5%);
  }

  &.user {
    flex-direction: row-reverse;
    margin-right: max(24px, 3.5%);
    margin-left: auto;

    .message-content {
      background: #ecf5ff;
    }
  }
}

.message-avatar {
  display: inline-flex;
  width: 32px;
  height: 32px;
  flex: 0 0 32px;
  align-items: center;
  justify-content: center;
  color: #fff;
  background: var(--el-color-primary);
  border-radius: 50%;

  .assistant & {
    background: #4f6b95;
  }
}

.user-avatar {
  overflow: hidden;
  background: transparent;
}

.message-content {
  min-width: 0;
  padding: 12px 14px;
  background: #f5f7fa;
  border-radius: 10px;
}

.message-role {
  margin-bottom: 6px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.message-text {
  color: var(--el-text-color-primary);
  line-height: 1.75;
  white-space: pre-wrap;
  word-break: break-word;
}

.tool-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 10px;
}

.loading-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
  color: var(--el-text-color-regular);
  line-height: 1.75;
  white-space: nowrap;
}

.chat-input-area {
  padding: 16px 24px 20px;
  background: #fff;
  border-top: 1px solid var(--el-border-color-lighter);
}

.composer-form {
  min-width: 0;
  flex: 1;
}

.input-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
}

.chat-settings {
  display: flex;
  align-items: center;
  gap: 8px;
}

.chat-setting {
  width: 116px;
}

.chat-model-setting {
  width: 132px;
}

.send-icon {
  margin-left: 5px;
}

@media (max-width: 900px) {
  .ai-learning-page {
    height: auto;
    min-height: 0;
    flex-direction: column;
  }

  .learning-steps {
    width: auto;
    flex-basis: auto;
    border-right: 0;
    border-bottom: 1px solid var(--el-border-color-lighter);
  }

  .security-tip {
    display: none;
  }

  .message-panel {
    min-height: 420px;
  }

  .input-footer {
    align-items: flex-start;
    gap: 10px;
    flex-direction: column;
  }

  .chat-settings {
    width: 100%;
    flex-wrap: wrap;
    justify-content: flex-end;
  }
}
</style>
