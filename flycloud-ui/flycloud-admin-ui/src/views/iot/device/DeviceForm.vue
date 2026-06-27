<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.iot.device.DeviceForm.k6cc98552')" prop="productId">
        <el-select
          v-model="formData.productId"
          :placeholder="t('auto.views.iot.device.DeviceForm.k59a0d3d1')"
          :disabled="formType === 'update'"
          clearable
        >
          <el-option
            v-for="product in products"
            :key="product.id"
            :label="product.name"
            :value="product.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="DeviceName" prop="deviceName">
        <el-input
          v-model="formData.deviceName"
          :placeholder="t('auto.views.iot.device.DeviceForm.k58560dae')"
          :disabled="formType === 'update'"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.iot.device.DeviceForm.k90f8a65c')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.iot.device.DeviceForm.k8167b2a5')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.iot.device.DeviceForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.iot.device.DeviceForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { DeviceApi, DeviceVO } from '@/api/iot/device'
import { ProductApi } from '@/api/iot/product'

/** IoT 设备 表单 */
const { t } = useI18n()
defineOptions({ name: 'IoTDeviceForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  productId: undefined,
  deviceName: undefined,
  name: undefined
})
const formRules = reactive({
  productId: [
    { required: true, message: t('auto.views.iot.device.DeviceForm.k0b3cde2b'), trigger: 'blur' }
  ],
  deviceName: [
    {
      pattern: /^[a-zA-Z0-9_.\-:@]{4,32}$/,
      message: t('auto.views.iot.device.DeviceForm.k0afa8db7'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      validator: (rule, value, callback) => {
        if (value === undefined || value === null) {
          callback()
          return
        }
        const length = value.replace(/[\u4e00-\u9fa5\u3040-\u30ff]/g, 'aa').length
        if (length < 4 || length > 64) {
          callback(new Error(t('auto.views.iot.device.DeviceForm.k6ad24cc4')))
        } else if (!/^[\u4e00-\u9fa5\u3040-\u30ff_a-zA-Z0-9]+$/.test(value)) {
          callback(new Error(t('auto.views.iot.device.DeviceForm.keff06894')))
        } else {
          callback()
        }
      },
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
      formData.value = await DeviceApi.getDevice(id)
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
    const data = formData.value as unknown as DeviceVO
    if (formType.value === 'create') {
      await DeviceApi.createDevice(data)
      message.success(t('common.createSuccess'))
    } else {
      await DeviceApi.updateDevice(data)
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
    productId: undefined,
    deviceName: undefined,
    name: undefined
  }
  formRef.value?.resetFields()
}

/** 查询字典下拉列表 */
const products = ref()
const getProducts = async () => {
  products.value = await ProductApi.getSimpleProductList()
}

onMounted(() => {
  getProducts()
})
</script>
