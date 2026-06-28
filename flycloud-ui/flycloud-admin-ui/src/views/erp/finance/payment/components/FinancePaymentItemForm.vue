<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    v-loading="formLoading"
    label-width="0px"
    :inline-message="true"
    :disabled="disabled"
  >
    <el-table :data="formData" show-summary :summary-method="getSummaries" class="-mt-10px">
      <el-table-column :label="t('common.index')" type="index" align="center" width="60" />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.components.FinancePaymentItemForm.k0dbf919b')"
        min-width="200"
      >
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.bizNo" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k20104358')"
        prop="totalPrice"
        fixed="right"
        min-width="100"
      >
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.totalPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k986c161f')"
        prop="paidPrice"
        fixed="right"
        min-width="100"
      >
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.paidPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k215ea20c')"
        prop="paymentPrice"
        fixed="right"
        min-width="115"
      >
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.paymentPrice`" class="mb-0px!">
            <el-input-number
              v-model="row.paymentPrice"
              controls-position="right"
              :precision="2"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('common.remark')" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.remark`" class="mb-0px!">
            <el-input
              v-model="row.remark"
              :placeholder="t('auto.views.crm.business.BusinessForm.k57e709d9')"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" :label="t('common.operation')" width="60">
        <template #default="{ $index }">
          <el-button @click="handleDelete($index)" link>—</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3" v-if="!disabled">
    <el-button @click="handleOpenPurchaseIn" round>{{ t('extra.kbff9e22e') }}</el-button>
    <el-button @click="handleOpenPurchaseReturn" round>{{ t('extra.k70c61c87') }}</el-button>
  </el-row>

  <!-- 可付款的【采购入库单】列表 -->
  <PurchaseInPaymentEnableList
    ref="purchaseInPaymentEnableListRef"
    @success="handleAddPurchaseIn"
  />
  <!-- 可付款的【采购入库单】列表 -->
  <PurchaseReturnRefundEnableList
    ref="purchaseReturnRefundEnableListRef"
    @success="handleAddPurchaseReturn"
  />
</template>
<script setup lang="ts">
import { ProductVO } from '@/api/erp/product/product'
import { erpPriceInputFormatter, getSumValue } from '@/utils'
import PurchaseInPaymentEnableList from '@/views/erp/purchase/in/components/PurchaseInPaymentEnableList.vue'
import PurchaseReturnRefundEnableList from '@/views/erp/purchase/return/components/PurchaseReturnRefundEnableList.vue'
import { PurchaseInVO } from '@/api/erp/purchase/in'
import { ErpBizType } from '@/utils/constants'
import { PurchaseReturnVO } from '@/api/erp/purchase/return'
const { t } = useI18n()
const props = defineProps<{
  items: undefined
  supplierId: undefined
  disabled: false
}>()
const message = useMessage()

const formLoading = ref(false) // 表单的加载中
const formData = ref([])
const formRules = reactive({
  paymentPrice: [
    {
      required: true,
      message: t('auto.views.erp.finance.payment.components.FinancePaymentItemForm.k2257eae5'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref([]) // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表

/** 初始化设置入库项 */
watch(
  () => props.items,
  async (val) => {
    formData.value = val
  },
  { immediate: true }
)

/** 合计 */
const getSummaries = (param: SummaryMethodProps) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index: number) => {
    if (index === 0) {
      sums[index] = t('auto.views.erp.finance.payment.components.FinancePaymentItemForm.k92bcbf71')
      return
    }
    if (['totalPrice', 'paidPrice', 'paymentPrice'].includes(column.property)) {
      const sum = getSumValue(data.map((item) => Number(item[column.property])))
      sums[index] = erpPriceInputFormatter(sum)
    } else {
      sums[index] = ''
    }
  })
  return sums
}

/** 新增【采购入库】按钮操作 */
const purchaseInPaymentEnableListRef = ref()
const handleOpenPurchaseIn = () => {
  if (!props.supplierId) {
    message.error(t('auto.views.erp.finance.payment.components.FinancePaymentItemForm.k38eae8d5'))
    return
  }
  purchaseInPaymentEnableListRef.value.open(props.supplierId)
}
const handleAddPurchaseIn = (rows: PurchaseInVO[]) => {
  rows.forEach((row) => {
    formData.value.push({
      bizId: row.id,
      bizType: ErpBizType.PURCHASE_IN,
      bizNo: row.no,
      totalPrice: row.totalPrice,
      paidPrice: row.paymentPrice,
      paymentPrice: row.totalPrice - row.paymentPrice
    })
  })
}

/** 新增【采购退货】按钮操作 */
const purchaseReturnRefundEnableListRef = ref()
const handleOpenPurchaseReturn = () => {
  if (!props.supplierId) {
    message.error(t('auto.views.erp.finance.payment.components.FinancePaymentItemForm.k38eae8d5'))
    return
  }
  purchaseReturnRefundEnableListRef.value.open(props.supplierId)
}
const handleAddPurchaseReturn = (rows: PurchaseReturnVO[]) => {
  rows.forEach((row) => {
    formData.value.push({
      bizId: row.id,
      bizType: ErpBizType.PURCHASE_RETURN,
      bizNo: row.no,
      totalPrice: -row.totalPrice,
      paidPrice: -row.refundPrice,
      paymentPrice: -row.totalPrice + row.refundPrice
    })
  })
}

/** 删除按钮操作 */
const handleDelete = (index: number) => {
  formData.value.splice(index, 1)
}

/** 表单校验 */
const validate = () => {
  return formRef.value.validate()
}
defineExpose({ validate })
</script>
