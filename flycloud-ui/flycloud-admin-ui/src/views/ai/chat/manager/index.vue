<template>
  <ContentWrap>
    <el-tabs>
      <el-tab-pane :label="t('auto.views.ai.chat.manager.index.k13d00fbc')">
        <ChatConversationList />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.ai.chat.manager.index.k45073998')">
        <ChatMessageList />
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>
</template>

<script setup lang="ts">
import ChatConversationList from './ChatConversationList.vue'
import ChatMessageList from './ChatMessageList.vue'
/** AI 聊天对话 列表 */
const { t } = useI18n()
defineOptions({ name: 'AiChatManager' })
</script>
