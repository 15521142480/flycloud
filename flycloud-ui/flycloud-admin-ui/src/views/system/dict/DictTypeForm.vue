<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item :label="t('auto.views.system.dict.DictTypeForm.k32bc8312')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.system.dict.DictTypeForm.ka33e8748')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.dict.DictTypeForm.k6dde52f3')" prop="type">
        <el-input
          v-model="formData.type"
          :disabled="typeof formData.id !== 'undefined'"
          :placeholder="t('auto.views.system.dict.DictTypeForm.k6d4589a3')"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
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
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.system.dict.DictTypeForm.kac962cb9')"
          type="textarea"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.system.dict.DictTypeForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.dict.DictTypeForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as DictTypeApi from '@/api/system/dict/dict.type'
import { CommonStatusEnum } from '@/utils/constants'
import { saveOrUpdate } from '@/api/system/dict/dict.type'
const { t } = useI18n()
defineOptions({ name: 'SystemDictTypeForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  type: '',
  status: CommonStatusEnum.ENABLE,
  remark: ''
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.system.dict.DictTypeForm.k0ab545c1'), trigger: 'blur' }
  ],
  type: [
    { required: true, message: t('auto.views.system.dict.DictTypeForm.k8295d679'), trigger: 'blur' }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.system.dict.DictTypeForm.k1318b551'),
      trigger: 'change'
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
      formData.value = await DictTypeApi.getDictType(id)
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
    const data = formData.value as DictTypeApi.DictTypeVO
    if (formType.value === 'create') {
      await DictTypeApi.saveOrUpdate(data)
      message.success(t('common.createSuccess'))
    } else {
      await DictTypeApi.saveOrUpdate(data)
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
    type: '',
    name: '',
    status: CommonStatusEnum.ENABLE,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>
