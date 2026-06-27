<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item
        :label="t('auto.views.mall.promotion.diy.page.DiyPageForm.k4a2d79ff')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.mall.promotion.diy.page.DiyPageForm.ke41e27e6')"
        />
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.mall.promotion.diy.page.DiyPageForm.k57e709d9')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.promotion.diy.page.DiyPageForm.k81946fbf')"
        prop="previewPicUrls"
      >
        <UploadImgs v-model="formData.previewPicUrls" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.mall.promotion.diy.page.DiyPageForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.promotion.diy.page.DiyPageForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as DiyPageApi from '@/api/mall/promotion/diy/page'

/** 装修页面表单 */
const { t } = useI18n()
defineOptions({ name: 'DiyPageForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  remark: undefined,
  previewPicUrls: []
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.mall.promotion.diy.page.DiyPageForm.k29ea947c'),
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
      formData.value = await DiyPageApi.getDiyPage(id)
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
    const data = formData.value as unknown as DiyPageApi.DiyPageVO
    if (formType.value === 'create') {
      await DiyPageApi.createDiyPage(data)
      message.success(t('common.createSuccess'))
    } else {
      await DiyPageApi.updateDiyPage(data)
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
    remark: undefined,
    previewPicUrls: []
  }
  formRef.value?.resetFields()
}
</script>
