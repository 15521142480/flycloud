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
      <header class="chat-header">
        <div>
          <div class="chat-title">{{ activeStep.name }}</div>
          <div class="chat-description">{{ activeStep.description }}</div>
        </div>
        <el-tag type="success" effect="light">已接入</el-tag>
      </header>

      <section ref="messagePanelRef" class="message-panel" aria-live="polite">
        <div v-if="messages.length === 0" class="empty-state">
          <Icon icon="ep:connection" class="empty-icon" />
          <h2>开始一次受控的业务查询</h2>
          <p>例如：查询用户 ID 1 的信息；或查询订单 ID 10001 的信息。</p>
          <div class="example-actions">
            <el-button @click="fillExample('查询用户 ID 1 的信息')">查询用户</el-button>
            <el-button @click="fillExample('查询订单 ID 10001 的信息')">查询订单</el-button>
          </div>
        </div>

        <article v-for="item in messages" :key="item.id" class="message-row" :class="item.role">
          <div class="message-avatar">
            <Icon :icon="item.role === 'user' ? 'ep:user' : 'ep:cpu'" />
          </div>
          <div class="message-content">
            <div class="message-role">{{ item.role === 'user' ? '我' : '飞翔云 AI' }}</div>
            <div class="message-text">{{ item.content }}</div>
            <div v-if="item.permissionMessage" class="tool-meta">
              <el-tag size="small" type="success">{{ item.permissionMessage }}</el-tag>
              <el-tag v-for="toolName in item.toolNames" :key="toolName" size="small" type="info">
                {{ toolName }}
              </el-tag>
            </div>
          </div>
        </article>

        <article v-if="sending" class="message-row assistant">
          <div class="message-avatar"><Icon icon="ep:cpu" /></div>
          <div class="message-content loading-content">
            <div class="message-role">飞翔云 AI</div>
            <el-icon class="is-loading"><Loading /></el-icon>
            <span>正在选择工具并校验业务权限…</span>
          </div>
        </article>
      </section>

      <footer class="chat-input-area">
        <form @submit.prevent="sendMessage">
          <el-input
            v-model="inputMessage"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6 }"
            maxlength="20000"
            show-word-limit
            resize="none"
            placeholder="请输入问题，例如：查询订单 ID 10001 的信息"
            :disabled="sending"
            @keydown.enter.exact.prevent="sendMessage"
          />
          <div class="input-footer">
            <span>Enter 发送，Shift + Enter 换行</span>
            <el-button type="primary" native-type="submit" :loading="sending" :disabled="!inputMessage.trim()">
              发送
              <Icon icon="ep:promotion" class="send-icon" />
            </el-button>
          </div>
        </form>
      </footer>
    </main>
  </div>
</template>

<script setup lang="ts">
import { Loading } from '@element-plus/icons-vue'
import { nextTick, ref } from 'vue'
import { toolCallingChat, type AiToolCallingChatResponse } from '@/api/ai/tool'

defineOptions({ name: 'AiManagement' })

interface LearningStep {
  id: number
  name: string
  status: string
  description: string
  completed: boolean
  available: boolean
}

interface ChatMessage {
  id: number
  role: 'user' | 'assistant'
  content: string
  permissionMessage?: string
  toolNames: string[]
}

const message = useMessage()
const messagePanelRef = ref<HTMLElement>()
const inputMessage = ref('')
const sending = ref(false)
const activeStepId = ref(3)
const messages = ref<ChatMessage[]>([])

const learningSteps: LearningStep[] = [
  {
    id: 1,
    name: '原生 HTTP 调模型 API',
    status: '已完成',
    description: '已完成 HTTP、JSON、流式响应等原生接口实践。',
    completed: true,
    available: false
  },
  {
    id: 2,
    name: 'Spring AI 调模型',
    status: '已完成',
    description: '已完成 Spring AI 多供应商模型调用实践。',
    completed: true,
    available: false
  },
  {
    id: 3,
    name: 'Tool Calling / 工具调用',
    status: '当前学习',
    description: '模型可调用后端业务工具，订单数据在服务端按当前登录用户二次授权。',
    completed: false,
    available: true
  },
  {
    id: 4,
    name: 'Chat Memory / 多轮对话',
    status: '待学习',
    description: '下一阶段将实现持久化上下文记忆。',
    completed: false,
    available: false
  },
  {
    id: 5,
    name: 'Embedding / 向量数据库',
    status: '待学习',
    description: '下一阶段将实现文本向量化。',
    completed: false,
    available: false
  },
  {
    id: 6,
    name: 'Vector Store',
    status: '待学习',
    description: '下一阶段将实现向量存储。',
    completed: false,
    available: false
  },
  {
    id: 7,
    name: 'RAG',
    status: '待学习',
    description: '下一阶段将实现检索增强生成。',
    completed: false,
    available: false
  },
  {
    id: 8,
    name: 'Agent',
    status: '待学习',
    description: '下一阶段将实现多步骤自主任务执行。',
    completed: false,
    available: false
  },
  {
    id: 9,
    name: 'MCP',
    status: '待学习',
    description: '下一阶段将接入模型上下文协议。',
    completed: false,
    available: false
  }
]

const activeStep = learningSteps.find((step) => step.id === activeStepId.value) || learningSteps[2]

/** 选择学习阶段。 */
const selectStep = (step: LearningStep) => {
  if (!step.available) {
    message.info(`${step.name}尚未进入开发阶段`)
    return
  }
  activeStepId.value = step.id
}

/** 填入示例问题。 */
const fillExample = (example: string) => {
  inputMessage.value = example
}

/** 发送 Tool Calling 聊天消息。 */
const sendMessage = async () => {
  const content = inputMessage.value.trim()
  if (!content || sending.value) {
    return
  }
  messages.value.push({ id: Date.now(), role: 'user', content, toolNames: [] })
  inputMessage.value = ''
  sending.value = true
  await scrollToBottom()
  try {
    const response = await toolCallingChat({ message: content })
    messages.value.push(toAssistantMessage(response))
  } catch {
    // Axios 统一拦截器已展示后端错误；此处保持对话区域可继续使用。
  } finally {
    sending.value = false
    await scrollToBottom()
  }
}

/** 转换服务端 Tool Calling 响应为页面消息。 */
const toAssistantMessage = (response: AiToolCallingChatResponse): ChatMessage => {
  return {
    id: Date.now() + 1,
    role: 'assistant',
    content: response.content,
    permissionMessage: response.permissionMessage,
    toolNames: response.toolNames || []
  }
}

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
  height: calc(100vh - 145px);
  min-height: 620px;
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

.chat-header {
  display: flex;
  min-height: 78px;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.message-panel {
  flex: 1;
  padding: 24px max(24px, 7%);
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
  max-width: 920px;
  margin: 0 auto 22px;

  &.user {
    flex-direction: row-reverse;

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

.loading-content {
  display: flex;
  align-items: center;
  gap: 8px;

  .message-role {
    width: 100%;
    margin-bottom: 0;
  }
}

.chat-input-area {
  padding: 16px max(24px, 7%) 20px;
  background: #fff;
  border-top: 1px solid var(--el-border-color-lighter);
}

.input-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
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
}
</style>
