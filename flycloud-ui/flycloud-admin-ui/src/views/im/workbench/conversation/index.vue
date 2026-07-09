<template>
  <!-- 菜单内嵌版：放在后台 Layout 内容区，复用 IM 主壳的初始化、WebSocket 和全局弹层 -->
  <div class="im-workbench-conversation">
    <ImHomeShell embedded :show-tool-bar="false">
      <ConversationPage />
    </ImHomeShell>
  </div>
</template>

<script lang="ts" setup>
import ImHomeShell from '../../home/index.vue'
import ConversationPage from '../../home/pages/conversation/index.vue'

defineOptions({ name: 'ImWorkbenchConversation' })
</script>

<style scoped lang="scss">
.im-workbench-conversation {
  width: 100%;
  height: calc(
    100vh - var(--top-tool-height) - var(--tags-view-height) - var(--app-footer-height) -
      var(--app-content-padding) - var(--app-content-padding)
  );
  min-height: 560px;
  overflow: hidden;
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 4px;
}
</style>
