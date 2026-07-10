<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k275b3ed8')"
        prop="platform"
      >
        <el-select
          v-model="formData.platform"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.ke0377170')"
          clearable
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.AI_PLATFORM)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k0b2b2cbe')"
        prop="keyId"
      >
        <el-select
          v-model="formData.keyId"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.k15ee76c1')"
          clearable
        >
          <el-option
            v-for="apiKey in apiKeyList"
            :key="apiKey.id"
            :label="apiKey.name"
            :value="apiKey.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.chatModel.ChatModelForm.ka11691fb')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.k02d5516c')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k3a818387')"
        prop="model"
      >
        <el-input
          v-model="formData.model"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.kf34bb3b2')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.chatModel.ChatModelForm.kc155fdb2')" prop="sort">
        <el-input-number
          v-model="formData.sort"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.k6618640d')"
          class="!w-1/1"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k6bbda1b1')"
        prop="status"
      >
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k80a0d1e0')"
        prop="temperature"
      >
        <el-input-number
          v-model="formData.temperature"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.ka09e02d5')"
          :min="0"
          :max="2"
          :precision="2"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.kd27629ce')"
        prop="maxTokens"
      >
        <el-input-number
          v-model="formData.maxTokens"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.kbf01445b')"
          :min="0"
          :max="4096"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatModel.ChatModelForm.k3f4045aa')"
        prop="maxContexts"
      >
        <el-input-number
          v-model="formData.maxContexts"
          :placeholder="t('auto.views.ai.model.chatModel.ChatModelForm.k1da1d932')"
          :min="0"
          :max="20"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.ai.model.chatModel.ChatModelForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.ai.model.chatModel.ChatModelForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ChatModelApi, ChatModelVO } from '@/api/ai/model/chatModel'
import { ApiKeyApi, ApiKeyVO } from '@/api/ai/model/apiKey'
import { CommonStatusEnum } from '@/utils/constants'
import { DICT_TYPE, getIntDictOptions, getStrDictOptions } from '@/utils/dict'

/** API 聊天模型 表单 */
const { t } = useI18n()
defineOptions({ name: 'ChatModelForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  keyId: undefined,
  name: undefined,
  model: undefined,
  platform: undefined,
  sort: undefined,
  status: CommonStatusEnum.ENABLE,
  temperature: undefined,
  maxTokens: undefined,
  maxContexts: undefined
})
const formRules = reactive({
  keyId: [
    {
      required: true,
      message: t('auto.views.ai.model.chatModel.ChatModelForm.k89b701f6'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      required: true,
      message: t('auto.views.ai.model.chatModel.ChatModelForm.k59d6e625'),
      trigger: 'blur'
    }
  ],
  model: [
    {
      required: true,
      message: t('auto.views.ai.model.chatModel.ChatModelForm.kf1181405'),
      trigger: 'blur'
    }
  ],
  platform: [
    {
      required: true,
      message: t('auto.views.ai.model.chatModel.ChatModelForm.kab2643c9'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.ai.model.chatModel.ChatModelForm.k3218602a'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.ai.model.chatModel.ChatModelForm.k1318b551'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const apiKeyList = ref([] as ApiKeyVO[]) // API 密钥列表

/** 打开弹窗 */
const open = async (type: string, id?: string) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ChatModelApi.getChatModel(id)
    } finally {
      formLoading.value = false
    }
  }
  // 获得下拉数据
  apiKeyList.value = await ApiKeyApi.getApiKeySimpleList(CommonStatusEnum.ENABLE)
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
    const data = formData.value as unknown as ChatModelVO
    if (formType.value === 'create') {
      await ChatModelApi.createChatModel(data)
      message.success(t('common.createSuccess'))
    } else {
      await ChatModelApi.updateChatModel(data)
      message.success(t('common.updateSuccess'))
    }
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
    keyId: undefined,
    name: undefined,
    model: undefined,
    platform: undefined,
    sort: undefined,
    status: CommonStatusEnum.ENABLE,
    temperature: undefined,
    maxTokens: undefined,
    maxContexts: undefined
  }
  formRef.value?.resetFields()
}
</script>
