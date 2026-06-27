<template>
  <Dialog
    v-model="dialogVisible"
    :max-height="500"
    :scroll="true"
    :title="dialogTitle"
    :width="800"
  >
    <Form ref="formRef" v-loading="formLoading" :rules="rules" :schema="allSchemas.formSchema" />
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.system.mail.template.MailTemplateForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.mail.template.MailTemplateForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as MailTemplateApi from '@/api/system/mail/template'
import { allSchemas, rules } from './template.data'
const { t } = useI18n()
defineOptions({ name: 'SystemMailTemplateForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await MailTemplateApi.getMailTemplate(id)
      formRef.value.setValues(data)
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
  const valid = await formRef.value.getElFormRef().validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formRef.value.formModel as MailTemplateApi.MailTemplateVO
    if (formType.value === 'create') {
      await MailTemplateApi.createMailTemplate(data)
      message.success(t('common.createSuccess'))
    } else {
      await MailTemplateApi.updateMailTemplate(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}
</script>
