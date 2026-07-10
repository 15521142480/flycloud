<template>
  <Dialog
    :title="t('auto.views.ai.chat.index.components.conversation.ka570c03b')"
    v-model="dialogVisible"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="130px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.ai.chat.index.components.conversation.k338affe3')"
        prop="systemMessage"
      >
        <el-input
          type="textarea"
          v-model="formData.systemMessage"
          rows="4"
          :placeholder="t('auto.views.ai.chat.index.components.conversation.kbb91e9c0')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.chat.index.components.conversation.k98fd0cbd')"
        prop="modelId"
      >
        <el-select
          v-model="formData.modelId"
          :placeholder="t('auto.views.ai.chat.index.components.conversation.k7330bf23')"
        >
          <el-option
            v-for="chatModel in chatModelList"
            :key="chatModel.id"
            :label="chatModel.name"
            :value="chatModel.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.chat.index.components.conversation.k80a0d1e0')"
        prop="temperature"
      >
        <el-input-number
          v-model="formData.temperature"
          :placeholder="t('auto.views.ai.chat.index.components.conversation.ka09e02d5')"
          :min="0"
          :max="2"
          :precision="2"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.chat.index.components.conversation.kd27629ce')"
        prop="maxTokens"
      >
        <el-input-number
          v-model="formData.maxTokens"
          :placeholder="t('auto.views.ai.chat.index.components.conversation.kbf01445b')"
          :min="0"
          :max="4096"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.chat.index.components.conversation.k3f4045aa')"
        prop="maxContexts"
      >
        <el-input-number
          v-model="formData.maxContexts"
          :placeholder="t('auto.views.ai.chat.index.components.conversation.k1da1d932')"
          :min="0"
          :max="20"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.ai.chat.index.components.conversation.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.ai.chat.index.components.conversation.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
// @ts-nocheck
import { CommonStatusEnum } from '@/utils/constants'
import { ChatModelApi, ChatModelVO } from '@/api/ai/model/chatModel'
import { ChatConversationApi, ChatConversationVO } from '@/api/ai/chat/conversation'
/** AI 聊天对话的更新表单 */
const { t } = useI18n()
defineOptions({ name: 'ChatConversationUpdateForm' })

const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined,
  systemMessage: undefined,
  modelId: undefined,
  temperature: undefined,
  maxTokens: undefined,
  maxContexts: undefined
})
const formRules = reactive({
  modelId: [
    {
      required: true,
      message: t('auto.views.ai.chat.index.components.conversation.kbe9ab121'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.ai.chat.index.components.conversation.k1318b551'),
      trigger: 'blur'
    }
  ],
  temperature: [
    {
      required: true,
      message: t('auto.views.ai.chat.index.components.conversation.k63a4c535'),
      trigger: 'blur'
    }
  ],
  maxTokens: [
    {
      required: true,
      message: t('auto.views.ai.chat.index.components.conversation.kd31fefed'),
      trigger: 'blur'
    }
  ],
  maxContexts: [
    {
      required: true,
      message: t('auto.views.ai.chat.index.components.conversation.k04095b73'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const chatModelList = ref([] as ChatModelVO[]) // 聊天模型列表

/** 打开弹窗 */
const open = async (id: string) => {
  dialogVisible.value = true
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await ChatConversationApi.getChatConversationMy(id)
      formData.value = Object.keys(formData.value).reduce((obj, key) => {
        if (data.hasOwnProperty(key)) {
          obj[key] = data[key]
        }
        return obj
      }, {})
    } finally {
      formLoading.value = false
    }
  }
  // 获得下拉数据
  chatModelList.value = await ChatModelApi.getChatModelSimpleList(CommonStatusEnum.ENABLE)
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ChatConversationVO
    await ChatConversationApi.updateChatConversationMy(data)
    message.success(t('auto.views.ai.chat.index.components.conversation.k2bd33532'))
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    systemMessage: undefined,
    modelId: undefined,
    temperature: undefined,
    maxTokens: undefined,
    maxContexts: undefined
  }
  formRef.value?.resetFields()
}
</script>
