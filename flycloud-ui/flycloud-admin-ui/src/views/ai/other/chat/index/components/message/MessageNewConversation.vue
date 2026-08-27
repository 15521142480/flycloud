<!-- 无聊天对话时，在 message 区域，可以新增对话 -->
<template>
  <div class="new-chat">
    <div class="box-center">
      <div class="tip">{{ t('auto.views.ai.chat.index.components.message.kc3697d69') }}</div>
      <div class="btns">
        <el-button type="primary" round @click="handlerNewChat">{{
          t('auto.views.ai.chat.index.components.message.kb3e55858')
        }}</el-button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
const { t } = useI18n()
const emits = defineEmits(['onNewConversation'])

/** 新建 conversation 聊天对话 */
const handlerNewChat = () => {
  emits('onNewConversation')
}
</script>
<style scoped lang="scss">
.new-chat {
  display: flex;
  flex-direction: row;
  justify-content: center;
  width: 100%;
  height: 100%;

  .box-center {
    display: flex;
    flex-direction: column;
    justify-content: center;

    .tip {
      font-size: 14px;
      color: #858585;
    }

    .btns {
      display: flex;
      flex-direction: row;
      justify-content: center;
      margin-top: 20px;
    }
  }
}
</style>
