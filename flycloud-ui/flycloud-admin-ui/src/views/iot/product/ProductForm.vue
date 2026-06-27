<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.iot.product.ProductForm.kabc0ac79')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.iot.product.ProductForm.k8d76e9ae')"
        />
      </el-form-item>

      <el-form-item :label="t('auto.views.iot.product.ProductForm.k47a991d1')" prop="deviceType">
        <el-select
          v-model="formData.deviceType"
          :placeholder="t('auto.views.iot.product.ProductForm.kd3646054')"
          :disabled="formType === 'update'"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_PRODUCT_DEVICE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        v-if="formData.deviceType === 0 || formData.deviceType === 2"
        :label="t('auto.views.iot.product.ProductForm.k17878d79')"
        prop="netType"
      >
        <el-select
          v-model="formData.netType"
          :placeholder="t('auto.views.iot.product.ProductForm.k8fbc7ed8')"
          :disabled="formType === 'update'"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_NET_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        v-if="formData.deviceType === 1"
        :label="t('auto.views.iot.product.ProductForm.k7eabdba0')"
        prop="protocolType"
      >
        <el-select
          v-model="formData.protocolType"
          :placeholder="t('auto.views.iot.product.ProductForm.k9a0bb9c7')"
          :disabled="formType === 'update'"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_PROTOCOL_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('auto.views.iot.product.ProductForm.k44f68d95')" prop="dataFormat">
        <el-select
          v-model="formData.dataFormat"
          :placeholder="t('auto.views.iot.product.ProductForm.kb7e1dd81')"
          :disabled="formType === 'update'"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_DATA_FORMAT)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item :label="t('auto.views.iot.product.ProductForm.kf151fa68')" prop="validateType">
        <el-radio-group v-model="formData.validateType" :disabled="formType === 'update'">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.IOT_VALIDATE_TYPE)"
            :key="dict.value"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item :label="t('auto.views.iot.product.ProductForm.k03917006')" prop="description">
        <el-input
          type="textarea"
          v-model="formData.description"
          :placeholder="t('auto.views.iot.product.ProductForm.k886d8ca8')"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.iot.product.ProductForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.iot.product.ProductForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ProductApi, ProductVO } from '@/api/iot/product'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
const { t } = useI18n()
defineOptions({ name: 'IoTProductForm' })
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  name: undefined,
  id: undefined,
  productKey: undefined,
  protocolId: undefined,
  categoryId: undefined,
  description: undefined,
  validateType: undefined,
  status: undefined,
  deviceType: undefined,
  netType: undefined,
  protocolType: undefined,
  dataFormat: undefined
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.iot.product.ProductForm.kcbfa98d4'), trigger: 'blur' }
  ],
  deviceType: [
    {
      required: true,
      message: t('auto.views.iot.product.ProductForm.k2203061c'),
      trigger: 'change'
    }
  ],
  netType: [
    {
      // TODO @haohao：0、1、/2 最好前端也枚举下；另外，这里的 required 可以直接设置为 true。然后表单那些 v-if。只要不存在，它自动就不校验了哈
      required: formData.deviceType === 0 || formData.deviceType === 2,
      message: t('auto.views.iot.product.ProductForm.kb6216470'),
      trigger: 'change'
    }
  ],
  protocolType: [
    {
      required: formData.deviceType === 1,
      message: t('auto.views.iot.product.ProductForm.kc3412822'),
      trigger: 'change'
    }
  ],
  dataFormat: [
    {
      required: true,
      message: t('auto.views.iot.product.ProductForm.k52032ef9'),
      trigger: 'change'
    }
  ],
  validateType: [
    {
      required: true,
      message: t('auto.views.iot.product.ProductForm.k61458e83'),
      trigger: 'change'
    }
  ]
})
const formRef = ref()

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ProductApi.getProduct(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open, close: () => (dialogVisible.value = false) })

/** 提交表单 */
const emit = defineEmits(['success'])
const submitForm = async () => {
  await formRef.value.validate()
  formLoading.value = true
  try {
    const data = formData.value as unknown as ProductVO
    if (formType.value === 'create') {
      await ProductApi.createProduct(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductApi.updateProduct(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false // 确保关闭弹框
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    name: undefined,
    id: undefined,
    productKey: undefined,
    protocolId: undefined,
    categoryId: undefined,
    description: undefined,
    validateType: undefined,
    status: undefined,
    deviceType: undefined,
    netType: undefined,
    protocolType: undefined,
    dataFormat: undefined
  }
  formRef.value?.resetFields()
}
</script>
