<template>
  <Dialog v-model="dialogVisible" :title="t('auto.views.system.area.AreaForm.kc8c6d25f')">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item label="IP" prop="ip">
        <el-input
          v-model="formData.ip"
          :placeholder="t('auto.views.system.area.AreaForm.k800f3a5e')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.area.AreaForm.k67d2d797')" prop="result">
        <el-input
          v-model="formData.result"
          :placeholder="t('auto.views.system.area.AreaForm.k59333747')"
          readonly
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.system.area.AreaForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.area.AreaForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as AreaApi from '@/api/system/area'
const { t } = useI18n()
defineOptions({ name: 'SystemAreaForm' })

const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：提交的按钮禁用
const formData = ref({
  ip: '',
  result: undefined
})
const formRules = reactive({
  ip: [{ required: true, message: t('auto.views.system.area.AreaForm.k4ae61e2a'), trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  resetForm()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    formData.value.result = await AreaApi.getAreaByIp(formData.value.ip!.trim())
    message.success(t('auto.views.system.area.AreaForm.keabbc2c9'))
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    ip: '',
    result: undefined
  }
  formRef.value?.resetFields()
}
</script>
