<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item
        :label="t('auto.views.mall.product.property.value.ValueForm.kabf37f9b')"
        prop="category"
      >
        <el-input v-model="formData.propertyId" disabled="" />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.product.property.value.ValueForm.k1be7ae4f')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.mall.product.property.value.ValueForm.kc2afb255')"
        />
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.mall.product.property.value.ValueForm.kac962cb9')"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.mall.product.property.value.ValueForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.product.property.value.ValueForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as PropertyApi from '@/api/mall/product/property'
const { t } = useI18n()
defineOptions({ name: 'ProductPropertyValueForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  propertyId: undefined,
  name: '',
  remark: ''
})
const formRules = reactive({
  propertyId: [
    {
      required: true,
      message: t('auto.views.mall.product.property.value.ValueForm.k62f18db5'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      required: true,
      message: t('auto.views.mall.product.property.value.ValueForm.kca898456'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, propertyId: number, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  formData.value.propertyId = propertyId
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await PropertyApi.getPropertyValue(id)
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
    const data = formData.value as PropertyApi.PropertyValueVO
    if (formType.value === 'create') {
      await PropertyApi.createPropertyValue(data)
      message.success(t('common.createSuccess'))
    } else {
      await PropertyApi.updatePropertyValue(data)
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
    propertyId: undefined,
    name: '',
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
