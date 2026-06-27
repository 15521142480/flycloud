<template>
  <Dialog
    :title="t('auto.views.mall.trade.brokerage.withdraw.BrokerageWithdrawRejectForm.kfe945e5a')"
    v-model="dialogVisible"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.mall.trade.brokerage.withdraw.BrokerageWithdrawRejectForm.k53ff81a7')"
        prop="auditReason"
      >
        <el-input
          v-model="formData.auditReason"
          type="textarea"
          :placeholder="
            t('auto.views.mall.trade.brokerage.withdraw.BrokerageWithdrawRejectForm.kc1e638e9')
          "
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.mall.trade.brokerage.withdraw.BrokerageWithdrawRejectForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.trade.brokerage.withdraw.BrokerageWithdrawRejectForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as BrokerageWithdrawApi from '@/api/mall/trade/brokerage/withdraw'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined,
  auditReason: undefined
})
const formRules = reactive({
  auditReason: [
    {
      required: true,
      message: t('auto.views.mall.trade.brokerage.withdraw.BrokerageWithdrawRejectForm.kb154b552'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  resetForm()
  formData.value.id = id
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
    const data = formData.value as unknown as BrokerageWithdrawApi.BrokerageWithdrawVO
    await BrokerageWithdrawApi.rejectBrokerageWithdraw(data)
    message.success(
      t('auto.views.mall.trade.brokerage.withdraw.BrokerageWithdrawRejectForm.k038b13ed')
    )
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
    auditReason: undefined
  }
  formRef.value?.resetFields()
}
</script>
