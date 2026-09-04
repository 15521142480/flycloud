<template>
  <AiWorkspace>
    <template #sidebar="workspace">
    <section class="unified-chat-nav">
      <div class="unified-nav-heading">
        <div class="unified-heading-row">
          <div class="steps-title">AI 助手</div>
          <el-button text type="primary" @click="openDemo">
            <Icon icon="ep:guide" /> 助手实验室过程
          </el-button>
        </div>
        <div class="steps-subtitle">会话、记忆与业务能力的正式入口</div>
      </div>
      <button
        class="conversation-item"
        :class="{ active: workspace.activeScope === 'unified' && !workspace.activeConversationId }"
        type="button"
        @click="workspace.createConversation()"
      >
        <Icon icon="ep:chat-dot-round" /> 开始新会话
      </button>
      <div class="conversation-section-title">最近</div>
      <button
        v-for="conversation in workspace.conversations"
        :key="conversation.conversationId"
        class="conversation-item"
        :class="{
          active:
            workspace.activeScope === 'unified' &&
            workspace.activeConversationId === conversation.conversationId
        }"
        type="button"
        @click="workspace.selectConversation(conversation.conversationId)"
      >
        <Icon icon="ep:chat-line-round" />
        <span>{{ conversation.title }}</span>
        <span class="conversation-actions">
          <el-tooltip content="重命名" placement="top">
            <Icon
              class="conversation-action rename-conversation"
              icon="ep:edit-pen"
              @click.stop="workspace.renameConversation(conversation)"
            />
          </el-tooltip>
          <el-tooltip content="删除" placement="top">
            <Icon
              class="conversation-action delete-conversation"
              icon="ep:delete"
              @click.stop="workspace.removeConversation(conversation.conversationId)"
            />
          </el-tooltip>
        </span>
      </button>
    </section>
    <div class="security-tip">
      <Icon icon="ep:shield-check" />
      <span>AI助手会根据当前用户身份执行受控工具授权。</span>
    </div>
    </template>
  </AiWorkspace>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import AiWorkspace from './components/AiWorkspace.vue'

defineOptions({ name: 'AiManagement' })

const router = useRouter()
/** 打开独立的 AI 实验室过程页面。 */
const openDemo = () => router.push('/ai/demo')
</script>

<style scoped lang="scss">
@use './styles/workspace.scss';
</style>
