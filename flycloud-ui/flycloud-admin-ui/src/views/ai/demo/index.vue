<template>
  <AiWorkspace initial-scope="laboratory">
    <template #sidebar="workspace">
    <section class="demo-nav">
      <div>
        <div class="steps-title">AI 实验室过程</div>
        <div class="steps-subtitle">按步骤学习并验证 AI 能力</div>
      </div>
      <el-button text type="primary" @click="openUnified">
        <Icon icon="ep:chat-dot-round" /> AI助手
      </el-button>
    </section>
    <section class="laboratory-steps" aria-label="AI 实验室过程">
      <button
        v-for="step in workspace.learningSteps"
        :key="step.id"
        class="step-item"
        :class="{
          active: workspace.activeScope === 'laboratory' && workspace.activeStepId === step.id,
          completed: step.completed
        }"
        type="button"
        @click="workspace.selectStep(step)"
      >
        <span class="step-index">{{ step.id }}</span>
        <span class="step-copy">
          <span class="step-name">{{ step.name }}</span>
          <span class="step-status">{{ step.status }}</span>
        </span>
        <Icon icon="ep:circle-check-filled" class="step-icon" />
      </button>
    </section>
    <div class="security-tip">
      <Icon icon="ep:shield-check" />
      <span>实验接口复用统一的模型、工具、知识库与权限能力。</span>
    </div>
    </template>
  </AiWorkspace>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import AiWorkspace from '../components/AiWorkspace.vue'

defineOptions({ name: 'AiDemo' })

const router = useRouter()
/** 返回正式AI助手页面。 */
const openUnified = () => router.push('/ai')
</script>

<style scoped lang="scss">
@use '../styles/workspace.scss';

.laboratory-steps {
  border-top: 1px solid var(--el-border-color-lighter);
}
</style>
