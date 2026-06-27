<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.k62d23172')"
    width="25%"
  >
    <el-form ref="formRef" v-loading="formLoading" :model="formData" label-width="100px">
      <el-form-item :label="t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.kf0e3ba5c')">
        <el-input v-model="formData.payPrice" disabled />
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.k62d23172')">
        <el-input-number v-model="formData.adjustPrice" :precision="2" :step="0.1" class="w-100%" />
        <el-tag class="ml-10px" type="warning">{{
          t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.k4cc37c50')
        }}</el-tag>
      </el-form-item>
      <el-form-item :label="t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.k40e373d6')">
        <el-input v-model="formData.newPayPrice" disabled />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as TradeOrderApi from '@/api/mall/trade/order'
import { convertToInteger, floatToFixed2, formatToFraction } from '@/utils'
import { cloneDeep } from 'lodash-es'
const { t } = useI18n()
defineOptions({ name: 'OrderUpdatePriceForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined, // 订单编号
  adjustPrice: 0, // 订单调价
  payPrice: '', // 应付金额(总)
  newPayPrice: '' // 调价后应付金额(总)
})
watch(
  () => formData.value.adjustPrice,
  (adjustPrice: number | string) => {
    const numMatch = formData.value.payPrice.match(/\d+(\.\d+)?/)
    if (numMatch) {
      const payPriceNum = parseFloat(numMatch[0])
      adjustPrice = typeof adjustPrice === 'string' ? parseFloat(adjustPrice) : adjustPrice
      formData.value.newPayPrice =
        (payPriceNum + adjustPrice).toFixed(2) +
        t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.k622f3c5a')
    }
  }
)

const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (row: TradeOrderApi.OrderVO) => {
  resetForm()
  formData.value.id = row.id!
  // 设置数据
  formData.value.adjustPrice = formatToFraction(row.adjustPrice!)
  formData.value.payPrice =
    floatToFixed2(row.payPrice!) +
    t('auto.views.mall.trade.order.form.OrderUpdatePriceForm.k622f3c5a')
  formData.value.newPayPrice = formData.value.payPrice
  dialogVisible.value = true
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 提交请求
  formLoading.value = true
  try {
    const data = cloneDeep(unref(formData))
    data.adjustPrice = convertToInteger(data.adjustPrice)
    delete data.payPrice
    delete data.newPayPrice
    await TradeOrderApi.updateOrderPrice(data)
    message.success(t('common.updateSuccess'))
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
    id: undefined, // 订单编号
    adjustPrice: 0, // 订单调价
    payPrice: '', // 应付金额(总)
    newPayPrice: '' // 调价后应付金额(总)
  }
  formRef.value?.resetFields()
}
</script>
