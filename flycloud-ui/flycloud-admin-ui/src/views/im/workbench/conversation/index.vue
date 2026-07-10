<template>
  <!-- 菜单内嵌版：放在后台 Layout 内容区，复用 IM 主壳的初始化、WebSocket 和全局弹层 -->
  <div class="im-workbench-conversation">
    <ImHomeShell embedded>
      <keep-alive>
        <component :is="activePage === 'conversation' ? ConversationPage : ContactPage" />
      </keep-alive>
    </ImHomeShell>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import ImHomeShell from '../../home/index.vue'
import ConversationPage from '../../home/pages/conversation/index.vue'
import ContactPage from '../../home/pages/contact/index.vue'
import { provideImNavigation, type ImNavigationPage } from '../../home/composables/useImNavigation'

defineOptions({ name: 'ImWorkbenchConversation' })

/** 菜单内嵌版的内部页签状态，不映射为浏览器路由。 */
const activePage = ref<ImNavigationPage>('conversation')

// 在工作台页提供导航上下文，使插槽中的会话页、通讯录页和主壳工具栏共用同一内部页签状态。
provideImNavigation({
  embedded: true,
  activePage: computed(() => activePage.value),
  changePage: handlePageChange
})

/** 工具栏请求切换内部页面，保持当前后台菜单地址不变。 */
function handlePageChange(page: ImNavigationPage) {
  activePage.value = page
}
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
