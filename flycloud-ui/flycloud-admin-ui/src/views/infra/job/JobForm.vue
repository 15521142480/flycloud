<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.infra.job.JobForm.k2e304698')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.infra.job.JobForm.k5169b9ec')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.JobForm.kec311980')" prop="handlerName">
        <el-input
          :readonly="formData.id !== undefined"
          v-model="formData.handlerName"
          :placeholder="t('auto.views.infra.job.JobForm.kec878692')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.JobForm.kddc1dd16')" prop="handlerParam">
        <el-input
          v-model="formData.handlerParam"
          :placeholder="t('auto.views.infra.job.JobForm.k906243e5')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.JobForm.kf6b43ffc')" prop="cronExpression">
        <crontab v-model="formData.cronExpression" />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.JobForm.k7a35a7d3')" prop="retryCount">
        <el-input
          v-model="formData.retryCount"
          :placeholder="t('auto.views.infra.job.JobForm.k90627d66')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.JobForm.kf86d661b')" prop="retryInterval">
        <el-input
          v-model="formData.retryInterval"
          :placeholder="t('auto.views.infra.job.JobForm.k7370c353')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.JobForm.k4bdf128c')" prop="monitorTimeout">
        <el-input
          v-model="formData.monitorTimeout"
          :placeholder="t('auto.views.infra.job.JobForm.k4cbb677f')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitForm" :loading="formLoading">{{
        t('auto.views.infra.job.JobForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.infra.job.JobForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as JobApi from '@/api/infra/job'
const { t } = useI18n()
defineOptions({ name: 'JobForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  handlerName: '',
  handlerParam: '',
  cronExpression: '',
  retryCount: undefined,
  retryInterval: undefined,
  monitorTimeout: undefined
})
const formRules = reactive({
  name: [{ required: true, message: t('auto.views.infra.job.JobForm.kd6e6aac0'), trigger: 'blur' }],
  handlerName: [
    { required: true, message: t('auto.views.infra.job.JobForm.kf015bf26'), trigger: 'blur' }
  ],
  cronExpression: [
    { required: true, message: t('auto.views.infra.job.JobForm.kf04b3f74'), trigger: 'blur' }
  ],
  retryCount: [
    { required: true, message: t('auto.views.infra.job.JobForm.k8e23c67d'), trigger: 'blur' }
  ],
  retryInterval: [
    { required: true, message: t('auto.views.infra.job.JobForm.kc21f2129'), trigger: 'blur' }
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
      formData.value = await JobApi.getJob(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交按钮 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as JobApi.JobVO
    if (formType.value === 'create') {
      await JobApi.createJob(data)
      message.success(t('common.createSuccess'))
    } else {
      await JobApi.updateJob(data)
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
    name: '',
    handlerName: '',
    handlerParam: '',
    cronExpression: '',
    retryCount: undefined,
    retryInterval: undefined,
    monitorTimeout: undefined
  }
  formRef.value?.resetFields()
}
</script>
