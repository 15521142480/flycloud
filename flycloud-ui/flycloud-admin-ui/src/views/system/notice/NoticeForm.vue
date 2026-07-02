<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="800">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item :label="t('auto.views.system.notice.NoticeForm.k547bc796')" prop="title">
        <el-input
          v-model="formData.title"
          :placeholder="t('auto.views.system.notice.NoticeForm.kd2630772')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.notice.NoticeForm.k7d23960d')" prop="content">
        <Editor v-model="formData.content" height="150px" directory="editor/system" />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.notice.NoticeForm.kcd0d7920')" prop="type">
        <el-select
          v-model="formData.type"
          clearable
          :placeholder="t('auto.views.system.notice.NoticeForm.kc57d6eb0')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_NOTICE_TYPE)"
            :key="parseInt(dict.value as any)"
            :label="dict.label"
            :value="parseInt(dict.value as any)"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="formData.status"
          clearable
          :placeholder="t('auto.views.system.notice.NoticeForm.kdba277df')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="parseInt(dict.value as any)"
            :label="dict.label"
            :value="parseInt(dict.value as any)"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.system.notice.NoticeForm.k582cc3a4')"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.system.notice.NoticeForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.notice.NoticeForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { CommonStatusEnum } from '@/utils/constants'
import * as NoticeApi from '@/api/system/notice'
const { t } = useI18n()
defineOptions({ name: 'SystemNoticeForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  title: '',
  type: undefined,
  content: '',
  status: CommonStatusEnum.ENABLE,
  remark: ''
})
const formRules = reactive({
  title: [
    { required: true, message: t('auto.views.system.notice.NoticeForm.k90d0db80'), trigger: 'blur' }
  ],
  type: [
    {
      required: true,
      message: t('auto.views.system.notice.NoticeForm.k53fcb4d2'),
      trigger: 'change'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.system.notice.NoticeForm.k1318b551'),
      trigger: 'change'
    }
  ],
  content: [
    { required: true, message: t('auto.views.system.notice.NoticeForm.kcae920db'), trigger: 'blur' }
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
      formData.value = await NoticeApi.getNotice(id)
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
    const data = formData.value as unknown as NoticeApi.NoticeVO
    if (formType.value === 'create') {
      await NoticeApi.createNotice(data)
      message.success(t('common.createSuccess'))
    } else {
      await NoticeApi.updateNotice(data)
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
    title: '',
    type: undefined,
    content: '',
    status: CommonStatusEnum.ENABLE,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
