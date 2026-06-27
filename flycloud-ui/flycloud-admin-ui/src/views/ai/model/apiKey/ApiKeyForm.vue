<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.ai.model.apiKey.ApiKeyForm.k275b3ed8')" prop="platform">
        <el-select
          v-model="formData.platform"
          :placeholder="t('auto.views.ai.model.apiKey.ApiKeyForm.ke0377170')"
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
      <el-form-item :label="t('auto.views.ai.model.apiKey.ApiKeyForm.k1be7ae4f')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.ai.model.apiKey.ApiKeyForm.kc2afb255')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.apiKey.ApiKeyForm.k0d1965e1')" prop="apiKey">
        <el-input
          v-model="formData.apiKey"
          :placeholder="t('auto.views.ai.model.apiKey.ApiKeyForm.k9a7744ec')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.apiKey.ApiKeyForm.kc1d074da')" prop="url">
        <el-input
          v-model="formData.url"
          :placeholder="t('auto.views.ai.model.apiKey.ApiKeyForm.k7a6f2ddf')"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
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
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.ai.model.apiKey.ApiKeyForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.ai.model.apiKey.ApiKeyForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE, getStrDictOptions } from '@/utils/dict'
import { ApiKeyApi, ApiKeyVO } from '@/api/ai/model/apiKey'
import { CommonStatusEnum } from '@/utils/constants'

/** AI API 密钥 表单 */
const { t } = useI18n()
defineOptions({ name: 'ApiKeyForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  apiKey: undefined,
  platform: undefined,
  url: undefined,
  status: CommonStatusEnum.ENABLE
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.ai.model.apiKey.ApiKeyForm.kca898456'),
      trigger: 'blur'
    }
  ],
  apiKey: [
    {
      required: true,
      message: t('auto.views.ai.model.apiKey.ApiKeyForm.k45bebb39'),
      trigger: 'blur'
    }
  ],
  platform: [
    {
      required: true,
      message: t('auto.views.ai.model.apiKey.ApiKeyForm.kacb834db'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.ai.model.apiKey.ApiKeyForm.k1318b551'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ApiKeyApi.getApiKey(id)
    } finally {
      formLoading.value = false
    }
  }
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
    const data = formData.value as unknown as ApiKeyVO
    if (formType.value === 'create') {
      await ApiKeyApi.createApiKey(data)
      message.success(t('common.createSuccess'))
    } else {
      await ApiKeyApi.updateApiKey(data)
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
    name: undefined,
    apiKey: undefined,
    platform: undefined,
    url: undefined,
    status: CommonStatusEnum.ENABLE
  }
  formRef.value?.resetFields()
}
</script>
