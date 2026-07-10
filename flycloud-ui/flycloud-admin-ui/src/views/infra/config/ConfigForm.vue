<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item :label="t('auto.views.infra.config.ConfigForm.kbccaaf74')" prop="category">
        <el-input
          v-model="formData.category"
          :placeholder="t('auto.views.infra.config.ConfigForm.k48e15c7f')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.config.ConfigForm.k480d10db')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.infra.config.ConfigForm.k6d4589a3')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.config.ConfigForm.k1d474e55')" prop="key">
        <el-input
          v-model="formData.key"
          :placeholder="t('auto.views.infra.config.ConfigForm.k03ca4e81')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.config.ConfigForm.k5d50206a')" prop="value">
        <el-input
          v-model="formData.value"
          :placeholder="t('auto.views.infra.config.ConfigForm.k5e91cadb')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.config.ConfigForm.kb23168b4')" prop="visible">
        <el-radio-group v-model="formData.visible">
          <el-radio
            v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
            :key="dict.value as string"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.infra.config.ConfigForm.kac962cb9')"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.infra.config.ConfigForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.infra.config.ConfigForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getBoolDictOptions } from '@/utils/dict'
import * as ConfigApi from '@/api/infra/config'
const { t } = useI18n()
defineOptions({ name: 'InfraConfigForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  category: '',
  name: '',
  key: '',
  value: '',
  visible: true,
  remark: ''
})
const formRules = reactive({
  category: [
    { required: true, message: t('auto.views.infra.config.ConfigForm.kaadbaf7b'), trigger: 'blur' }
  ],
  name: [
    { required: true, message: t('auto.views.infra.config.ConfigForm.kc0cd0690'), trigger: 'blur' }
  ],
  key: [
    { required: true, message: t('auto.views.infra.config.ConfigForm.k3b301863'), trigger: 'blur' }
  ],
  value: [
    { required: true, message: t('auto.views.infra.config.ConfigForm.k795bd301'), trigger: 'blur' }
  ],
  visible: [
    { required: true, message: t('auto.views.infra.config.ConfigForm.k697ac8ed'), trigger: 'blur' }
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
      formData.value = await ConfigApi.getConfig(id)
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
    const data = formData.value as ConfigApi.ConfigVO
    if (formType.value === 'create') {
      await ConfigApi.createConfig(data)
      message.success(t('common.createSuccess'))
    } else {
      await ConfigApi.updateConfig(data)
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
    category: '',
    name: '',
    key: '',
    value: '',
    visible: true,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
