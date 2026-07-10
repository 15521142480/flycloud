<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="140px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.system.notify.template.NotifyTemplateForm.k9c0cf0a4')"
        prop="code"
      >
        <el-input
          v-model="formData.code"
          :placeholder="t('auto.views.system.notify.template.NotifyTemplateForm.k457d3fed')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.notify.template.NotifyTemplateForm.kbbc511d0')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.system.notify.template.NotifyTemplateForm.k359c9c67')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.notify.template.NotifyTemplateForm.kdc362463')"
        prop="content"
      >
        <el-input
          type="textarea"
          v-model="formData.content"
          :placeholder="t('auto.views.system.notify.template.NotifyTemplateForm.kb8138d5e')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.notify.template.NotifyTemplateForm.ke4e46c72')"
        prop="type"
      >
        <el-select
          v-model="formData.type"
          :placeholder="t('auto.views.system.notify.template.NotifyTemplateForm.k97f47b78')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.system.notify.template.NotifyTemplateForm.k6bbda1b1')"
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
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.system.notify.template.NotifyTemplateForm.k57e709d9')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.system.notify.template.NotifyTemplateForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.notify.template.NotifyTemplateForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as NotifyTemplateApi from '@/api/system/notify/template'
import { CommonStatusEnum } from '@/utils/constants'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型
const formData = ref<NotifyTemplateApi.NotifyTemplateVO>({
  id: undefined,
  name: '',
  code: '',
  content: '',
  type: undefined,
  params: '',
  status: CommonStatusEnum.ENABLE,
  remark: ''
})
const formRules = reactive({
  type: [
    {
      required: true,
      message: t('auto.views.system.notify.template.NotifyTemplateForm.k47a82cae'),
      trigger: 'change'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.system.notify.template.NotifyTemplateForm.k03991f81'),
      trigger: 'blur'
    }
  ],
  code: [
    {
      required: true,
      message: t('auto.views.system.notify.template.NotifyTemplateForm.kef6969d9'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      required: true,
      message: t('auto.views.system.notify.template.NotifyTemplateForm.k15011507'),
      trigger: 'blur'
    }
  ],
  content: [
    {
      required: true,
      message: t('auto.views.system.notify.template.NotifyTemplateForm.k901576f7'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: string) => {
  dialogVisible.value = true
  dialogTitle.value = type
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await NotifyTemplateApi.getNotifyTemplate(id)
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
  formLoading.value = true
  try {
    const data = formData.value as unknown as NotifyTemplateApi.NotifyTemplateVO
    if (formType.value === 'create') {
      await NotifyTemplateApi.createNotifyTemplate(data)
      message.success(t('auto.views.system.notify.template.NotifyTemplateForm.kcbdbd295'))
    } else {
      await NotifyTemplateApi.updateNotifyTemplate(data)
      message.success(t('auto.views.system.notify.template.NotifyTemplateForm.kf8913eb4'))
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
    name: '',
    code: '',
    content: '',
    type: undefined,
    params: '',
    status: CommonStatusEnum.ENABLE,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
