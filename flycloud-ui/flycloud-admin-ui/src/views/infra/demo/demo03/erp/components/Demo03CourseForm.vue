<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.infra.demo.demo03.erp.components.k364bd1bf')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.infra.demo.demo03.erp.components.k010c1585')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.infra.demo.demo03.erp.components.kda335e20')"
        prop="score"
      >
        <el-input
          v-model="formData.score"
          :placeholder="t('auto.views.infra.demo.demo03.erp.components.kcdc49d02')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.infra.demo.demo03.erp.components.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.infra.demo.demo03.erp.components.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
// @ts-nocheck
import * as Demo03StudentApi from '@/api/infra/demo/demo03/erp'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  studentId: undefined,
  name: undefined,
  score: undefined
})
const formRules = reactive({
  studentId: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo03.erp.components.kd16396dc'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo03.erp.components.k46f3776c'),
      trigger: 'blur'
    }
  ],
  score: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo03.erp.components.ke012aef4'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: string, studentId: string) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  formData.value.studentId = studentId
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await Demo03StudentApi.getDemo03Course(id)
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
    const data = formData.value
    if (formType.value === 'create') {
      await Demo03StudentApi.createDemo03Course(data)
      message.success(t('common.createSuccess'))
    } else {
      await Demo03StudentApi.updateDemo03Course(data)
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
    studentId: undefined,
    name: undefined,
    score: undefined
  }
  formRef.value?.resetFields()
}
</script>
