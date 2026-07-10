<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.ke18bdddb')"
        prop="type"
      >
        <el-radio-group v-model="formData.type">
          <el-radio-button value="1">
            {{ t('auto.views.iot.product.detail.ThinkModelFunctionForm.k7d3c35c2') }}
          </el-radio-button>
          <el-radio-button value="2">
            {{ t('auto.views.iot.product.detail.ThinkModelFunctionForm.k4b81846f') }}
          </el-radio-button>
          <el-radio-button value="3">
            {{ t('auto.views.iot.product.detail.ThinkModelFunctionForm.k550e3280') }}
          </el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k785d1756')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k9cf7d887')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k85803da5')"
        prop="identifier"
      >
        <el-input
          v-model="formData.identifier"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.kbbe78bf6')"
          :disabled="formType === 'update'"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.kb031dc9a')"
        prop="type"
      >
        <el-select
          v-model="formData.property.dataType.type"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.ke7555c5f')"
          :disabled="formType === 'update'"
        >
          <el-option
            key="int"
            :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k87542693')"
            value="int"
          />
          <el-option
            key="float"
            :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k1095236e')"
            value="float"
          />
          <el-option
            key="double"
            :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.kcd570ebe')"
            value="double"
          />
          <!--          <el-option key="text" label="text (文本型)" value="text" />-->
          <!--          <el-option key="date" label="date (日期型)" value="date" />-->
          <!--          <el-option key="bool" label="bool (布尔型)" value="bool" />-->
          <!--          <el-option key="enum" label="enum (枚举型)" value="enum" />-->
          <!--          <el-option key="struct" label="struct (结构体)" value="struct" />-->
          <!--          <el-option key="array" label="array (数组)" value="array" />-->
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k472d7868')"
        prop="max"
      >
        <el-input
          v-model="formData.property.dataType.specs.min"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.ka3ff4b04')"
        />
        <span class="mx-2">~</span>
        <el-input
          v-model="formData.property.dataType.specs.max"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.kd4b337db')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.kac69f596')"
        prop="step"
      >
        <el-input
          v-model="formData.property.dataType.specs.step"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k8bd93b1f')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k8ec8c8c7')"
        prop="unit"
      >
        <el-input
          v-model="formData.property.dataType.specs.unit"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.kc3d7ef42')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k3b90c744')"
        prop="accessMode"
      >
        <el-radio-group v-model="formData.property.accessMode">
          <el-radio label="rw">{{
            t('auto.views.iot.product.detail.ThinkModelFunctionForm.k9400a0af')
          }}</el-radio>
          <el-radio label="r">{{
            t('auto.views.iot.product.detail.ThinkModelFunctionForm.kffc1d065')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.iot.product.detail.ThinkModelFunctionForm.k6652941e')"
        prop="property.description"
      >
        <el-input
          type="textarea"
          v-model="formData.property.description"
          :placeholder="t('auto.views.iot.product.detail.ThinkModelFunctionForm.kb741724d')"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.iot.product.detail.ThinkModelFunctionForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.iot.product.detail.ThinkModelFunctionForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
// @ts-nocheck
import { ProductVO } from '@/api/iot/product'
import { ThinkModelFunctionApi, ThinkModelFunctionVO } from '@/api/iot/thinkmodelfunction'
const { t } = useI18n()
const props = defineProps<{ product: ProductVO }>()

defineOptions({ name: 'ThinkModelFunctionForm' })
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  productId: undefined,
  productKey: undefined,
  identifier: undefined,
  name: undefined,
  description: undefined,
  type: '1',
  property: {
    identifier: undefined,
    name: undefined,
    accessMode: 'rw',
    required: true,
    dataType: {
      type: undefined,
      specs: {
        min: undefined,
        max: undefined,
        step: undefined,
        unit: undefined
      }
    },
    description: undefined // 添加这一行
  }
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.iot.product.detail.ThinkModelFunctionForm.ka389c3a1'),
      trigger: 'blur'
    },
    {
      pattern: /^[\u4e00-\u9fa5a-zA-Z0-9][\u4e00-\u9fa5a-zA-Z0-9\-_/\.]{0,29}$/,
      message: t('auto.views.iot.product.detail.ThinkModelFunctionForm.k7b1ca970'),
      trigger: 'blur'
    }
  ],
  type: [
    {
      required: true,
      message: t('auto.views.iot.product.detail.ThinkModelFunctionForm.ka1f77aa8'),
      trigger: 'blur'
    }
  ],
  identifier: [
    {
      required: true,
      message: t('auto.views.iot.product.detail.ThinkModelFunctionForm.k1c51b41a'),
      trigger: 'blur'
    },
    {
      pattern: /^[a-zA-Z0-9_]{1,50}$/,
      message: t('auto.views.iot.product.detail.ThinkModelFunctionForm.kb968d57e'),
      trigger: 'blur'
    },
    {
      validator: (rule, value, callback) => {
        const reservedKeywords = ['set', 'get', 'post', 'property', 'event', 'time', 'value']
        if (reservedKeywords.includes(value)) {
          callback(new Error(t('auto.views.iot.product.detail.ThinkModelFunctionForm.k9f3b3d00')))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  property: {
    dataType: {
      type: [
        {
          required: true,
          message: t('auto.views.iot.product.detail.ThinkModelFunctionForm.k9ba77a24'),
          trigger: 'blur'
        }
      ]
    },
    accessMode: [
      {
        required: true,
        message: t('auto.views.iot.product.detail.ThinkModelFunctionForm.k80e06b06'),
        trigger: 'blur'
      }
    ]
  }
})
const formRef = ref()

/** 打开弹窗 */
const open = async (type: string, id?: string) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ThinkModelFunctionApi.getThinkModelFunction(id)
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
    const data = formData.value as unknown as ThinkModelFunctionVO
    data.productId = props.product.id
    data.productKey = props.product.productKey
    if (formType.value === 'create') {
      await ThinkModelFunctionApi.createThinkModelFunction(data)
      message.success(t('common.createSuccess'))
    } else {
      await ThinkModelFunctionApi.updateThinkModelFunction(data)
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
    id: undefined,
    productId: undefined,
    productKey: undefined,
    identifier: undefined,
    name: undefined,
    description: undefined,
    type: '1', // todo @HAOHAO：看看枚举下
    property: {
      identifier: undefined,
      name: undefined,
      accessMode: 'rw',
      required: true,
      dataType: {
        type: undefined,
        specs: {
          min: undefined,
          max: undefined,
          step: undefined,
          unit: undefined
        }
      },
      description: undefined // 确保重置 description 字段
    }
  }
  formRef.value?.resetFields()
}
</script>
