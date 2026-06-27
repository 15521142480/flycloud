<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="80px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.mall.product.brand.BrandForm.kaa073c76')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.mall.product.brand.BrandForm.kfae6109b')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.product.brand.BrandForm.k8ac21a67')" prop="picUrl">
        <UploadImg v-model="formData.picUrl" :limit="1" :is-show-tip="false" />
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.product.brand.BrandForm.kbd611ebc')" prop="sort">
        <el-input-number v-model="formData.sort" controls-position="right" :min="0" />
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.product.brand.BrandForm.k82629cda')" prop="status">
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
      <el-form-item :label="t('auto.views.mall.product.brand.BrandForm.kd5b08c94')">
        <el-input
          v-model="formData.description"
          type="textarea"
          :placeholder="t('auto.views.mall.product.brand.BrandForm.kc29415f3')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.mall.product.brand.BrandForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.product.brand.BrandForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { CommonStatusEnum } from '@/utils/constants'
import * as ProductBrandApi from '@/api/mall/product/brand'
const { t } = useI18n()
defineOptions({ name: 'ProductBrandForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  picUrl: '',
  status: CommonStatusEnum.ENABLE,
  description: ''
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.mall.product.brand.BrandForm.k114d5ec4'),
      trigger: 'blur'
    }
  ],
  picUrl: [
    {
      required: true,
      message: t('auto.views.mall.product.brand.BrandForm.kb44568fc'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.mall.product.brand.BrandForm.kd97ade74'),
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
      formData.value = await ProductBrandApi.getBrand(id)
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
    const data = formData.value as ProductBrandApi.BrandVO
    if (formType.value === 'create') {
      await ProductBrandApi.createBrand(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductBrandApi.updateBrand(data)
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
    picUrl: '',
    status: CommonStatusEnum.ENABLE,
    description: ''
  }
  formRef.value?.resetFields()
}
</script>
