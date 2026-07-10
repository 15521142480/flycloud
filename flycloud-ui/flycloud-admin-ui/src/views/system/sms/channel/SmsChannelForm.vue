<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="130px"
    >
      <el-form-item
        :label="t('auto.views.system.sms.channel.SmsChannelForm.kf57fae84')"
        prop="signature"
      >
        <el-input
          v-model="formData.signature"
          :placeholder="t('auto.views.system.sms.channel.SmsChannelForm.k41738330')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.sms.channel.SmsChannelForm.k82233330')"
        prop="code"
      >
        <el-select
          v-model="formData.code"
          clearable
          :placeholder="t('auto.views.system.sms.channel.SmsChannelForm.k8e6c552b')"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.sms.channel.SmsChannelForm.k5691b379')">
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
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.system.sms.channel.SmsChannelForm.k57e709d9')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.sms.channel.SmsChannelForm.k4df60a58')"
        prop="apiKey"
      >
        <el-input
          v-model="formData.apiKey"
          :placeholder="t('auto.views.system.sms.channel.SmsChannelForm.k2e483068')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.sms.channel.SmsChannelForm.kae001512')"
        prop="apiSecret"
      >
        <el-input
          v-model="formData.apiSecret"
          :placeholder="t('auto.views.system.sms.channel.SmsChannelForm.k971dddcf')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.sms.channel.SmsChannelForm.kbe9e0cf3')"
        prop="callbackUrl"
      >
        <el-input
          v-model="formData.callbackUrl"
          :placeholder="t('auto.views.system.sms.channel.SmsChannelForm.k015a2ff7')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.system.sms.channel.SmsChannelForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.sms.channel.SmsChannelForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions, getStrDictOptions } from '@/utils/dict'
import * as SmsChannelApi from '@/api/system/sms/smsChannel'
import { CommonStatusEnum } from '@/utils/constants'
const { t } = useI18n()
defineOptions({ name: 'SystemSmsChannelForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  signature: '',
  code: '',
  status: CommonStatusEnum.ENABLE,
  remark: '',
  apiKey: '',
  apiSecret: '',
  callbackUrl: ''
})
const formRules = reactive({
  signature: [
    {
      required: true,
      message: t('auto.views.system.sms.channel.SmsChannelForm.k7347d5bb'),
      trigger: 'blur'
    }
  ],
  code: [
    {
      required: true,
      message: t('auto.views.system.sms.channel.SmsChannelForm.k080ef4cf'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.system.sms.channel.SmsChannelForm.k09f77553'),
      trigger: 'blur'
    }
  ],
  apiKey: [
    {
      required: true,
      message: t('auto.views.system.sms.channel.SmsChannelForm.kf58009f8'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

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
      formData.value = await SmsChannelApi.getSmsChannel(id)
      console.log(formData)
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
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as SmsChannelApi.SmsChannelVO
    if (formType.value === 'create') {
      await SmsChannelApi.createSmsChannel(data)
      message.success(t('common.createSuccess'))
    } else {
      await SmsChannelApi.updateSmsChannel(data)
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
    signature: '',
    code: '',
    status: CommonStatusEnum.ENABLE,
    remark: '',
    apiKey: '',
    apiSecret: '',
    callbackUrl: ''
  }
  formRef.value?.resetFields()
}
</script>
