<template>
  <!-- 核销对话框 -->
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.mall.trade.order.form.OrderPickUpForm.kbf7d8d6b')"
    width="35%"
  >
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item
        prop="pickUpVerifyCode"
        :label="t('auto.views.mall.trade.order.form.OrderPickUpForm.k270938f1')"
      >
        <el-input
          v-model="formData.pickUpVerifyCode"
          :placeholder="t('auto.views.mall.trade.order.form.OrderPickUpForm.k0d1d198f')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" :disabled="formLoading" @click="getOrderByPickUpVerifyCode">
        {{ t('extra.k09df509f') }}
      </el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.trade.order.form.OrderPickUpForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
  <!-- 核销确认对话框 -->
  <Dialog v-model="detailDialogVisible" title="订单详情" width="55%">
    <TradeOrderDetail v-if="orderDetails.id" :id="orderDetails.id" :show-pick-up="false" />
    <template #footer>
      <el-button type="primary" :disabled="formLoading" @click="submitForm"> 确认核销 </el-button>
      <el-button @click="detailDialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as TradeOrderApi from '@/api/mall/trade/order'
import { OrderVO } from '@/api/mall/trade/order'
import { DeliveryTypeEnum, TradeOrderStatusEnum } from '@/utils/constants'
import TradeOrderDetail from '@/views/mall/trade/order/detail/index.vue'

/** 订单核销表单 */
const { t } = useI18n()
defineOptions({ name: 'OrderPickUpForm' })

const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const detailDialogVisible = ref(false) // 详情弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formRules = reactive({
  pickUpVerifyCode: [
    {
      required: true,
      message: t('auto.views.mall.trade.order.form.OrderPickUpForm.k021ac487'),
      trigger: 'blur'
    }
  ]
})
const formData = ref({
  pickUpVerifyCode: '' // 核销码
})
const formRef = ref() // 表单 Ref
const orderDetails = ref<OrderVO>({})

/** 打开弹窗 */
const open = async () => {
  resetForm()
  dialogVisible.value = true
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 提交请求
  formLoading.value = true
  try {
    await TradeOrderApi.pickUpOrderByVerifyCode(formData.value.pickUpVerifyCode)
    message.success(t('auto.views.mall.trade.order.form.OrderPickUpForm.k6a980a8c'))
    detailDialogVisible.value = false
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success', true)
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    pickUpVerifyCode: '' // 核销码
  }
  formRef.value?.resetFields()
}

/** 查询核销码对应的订单 */
const getOrderByPickUpVerifyCode = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return

  formLoading.value = true
  const data = await TradeOrderApi.getOrderByPickUpVerifyCode(formData.value.pickUpVerifyCode)
  formLoading.value = false
  if (data?.deliveryType !== DeliveryTypeEnum.PICK_UP.type) {
    message.error(t('auto.views.mall.trade.order.form.OrderPickUpForm.kd21f8810'))
    return
  }
  if (data?.status !== TradeOrderStatusEnum.UNDELIVERED.status) {
    message.error(t('auto.views.mall.trade.order.form.OrderPickUpForm.k26304882'))
    return
  }
  orderDetails.value = data
  // 显示详情对话框
  detailDialogVisible.value = true
}
</script>
