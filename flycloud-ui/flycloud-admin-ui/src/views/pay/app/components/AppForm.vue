<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="160px"
    >
      <el-form-item :label="t('auto.views.pay.app.components.AppForm.k6c19fe01')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.pay.app.components.AppForm.k445d8859')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.app.components.AppForm.k38f2da34')" prop="name">
        <el-input
          v-model="formData.appKey"
          :placeholder="t('auto.views.pay.app.components.AppForm.k7ad675b4')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.pay.app.components.AppForm.k6bbda1b1')" prop="status">
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
      <el-form-item
        :label="t('auto.views.pay.app.components.AppForm.k5eecce52')"
        prop="orderNotifyUrl"
      >
        <el-input
          v-model="formData.orderNotifyUrl"
          :placeholder="t('auto.views.pay.app.components.AppForm.kf2c2bae6')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.pay.app.components.AppForm.k038f38e3')"
        prop="refundNotifyUrl"
      >
        <el-input
          v-model="formData.refundNotifyUrl"
          :placeholder="t('auto.views.pay.app.components.AppForm.k912ea508')"
        />
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.pay.app.components.AppForm.k57e709d9')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.pay.app.components.AppForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.pay.app.components.AppForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as AppApi from '@/api/pay/app'
import { CommonStatusEnum } from '@/utils/constants'
const { t } = useI18n()
defineOptions({ name: 'PayAppForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  appKey: undefined,
  status: CommonStatusEnum.ENABLE,
  remark: undefined,
  orderNotifyUrl: undefined,
  refundNotifyUrl: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.pay.app.components.AppForm.kf6f9e61d'),
      trigger: 'blur'
    }
  ],
  appKey: [
    {
      required: true,
      message: t('auto.views.pay.app.components.AppForm.k8186d52a'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.pay.app.components.AppForm.k03991f81'),
      trigger: 'blur'
    }
  ],
  orderNotifyUrl: [
    {
      required: true,
      message: t('auto.views.pay.app.components.AppForm.k8de05039'),
      trigger: 'blur'
    }
  ],
  refundNotifyUrl: [
    {
      required: true,
      message: t('auto.views.pay.app.components.AppForm.k35a5b2a2'),
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
      formData.value = await AppApi.getApp(id)
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
    const data = formData.value as unknown as AppApi.AppVO
    if (formType.value === 'create') {
      await AppApi.createApp(data)
      message.success(t('common.createSuccess'))
    } else {
      await AppApi.updateApp(data)
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
    status: CommonStatusEnum.ENABLE,
    remark: undefined,
    orderNotifyUrl: undefined,
    refundNotifyUrl: undefined,
    appKey: undefined
  }
  formRef.value?.resetFields()
}
</script>
