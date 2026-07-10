<!-- 可付款的采购入库单列表 -->
<template>
  <Dialog
    :title="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k317b0f11')"
    v-model="dialogVisible"
    :appendToBody="true"
    :scroll="true"
    width="1080"
  >
    <ContentWrap>
      <!-- 搜索工作栏 -->
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="68px"
      >
        <el-form-item
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.ka9f67da2')"
          prop="no"
        >
          <el-input
            v-model="queryParams.no"
            :placeholder="
              t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.keb25b35c')
            "
            clearable
            @keyup.enter="handleQuery"
            class="!w-160px"
          />
        </el-form-item>
        <el-form-item
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k6cc98552')"
          prop="productId"
        >
          <el-select
            v-model="queryParams.productId"
            clearable
            filterable
            :placeholder="
              t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k59a0d3d1')
            "
            class="!w-160px"
          >
            <el-option
              v-for="item in productList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k661cec87')"
          prop="orderTime"
        >
          <el-date-picker
            v-model="queryParams.inTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            :start-placeholder="
              t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k1f291968')
            "
            :end-placeholder="
              t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.kf4b9b2b5')
            "
            :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
            class="!w-160px"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery"
            ><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button
          >
          <el-button @click="resetQuery"
            ><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button
          >
        </el-form-item>
      </el-form>
    </ContentWrap>

    <ContentWrap>
      <el-table
        v-loading="loading"
        :data="list"
        :show-overflow-tooltip="true"
        :stripe="true"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          width="30"
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k70b20820')"
          type="selection"
        />
        <el-table-column
          min-width="180"
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.ka9f67da2')"
          align="center"
          prop="no"
        />
        <el-table-column
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k703c9eb0')"
          align="center"
          prop="supplierName"
        />
        <el-table-column
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k90095856')"
          align="center"
          prop="productNames"
          min-width="200"
        />
        <el-table-column
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k661cec87')"
          align="center"
          prop="inTime"
          :formatter="dateFormatter2"
          width="120px"
        />
        <el-table-column
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k787ad1de')"
          align="center"
          prop="creatorName"
        />
        <el-table-column
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k20104358')"
          align="center"
          prop="totalPrice"
          :formatter="erpPriceTableColumnFormatter"
        />
        <el-table-column
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k986c161f')"
          align="center"
          prop="paymentPrice"
          :formatter="erpPriceTableColumnFormatter"
        />
        <el-table-column
          :label="t('auto.views.erp.purchase.in.components.PurchaseInPaymentEnableList.k3ddc8eaa')"
          align="center"
        >
          <template #default="scope">
            <span v-if="scope.row.paymentPrice === scope.row.totalPrice">0</span>
            <el-tag type="danger" v-else>
              {{ erpPriceInputFormatter(scope.row.totalPrice - scope.row.paymentPrice) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <Pagination
        v-model:limit="queryParams.pageSize"
        v-model:page="queryParams.pageNum"
        :total="total"
        @pagination="getList"
      />
    </ContentWrap>
    <template #footer>
      <el-button :disabled="!selectionList.length" type="primary" @click="submitForm">
        {{ t('extra.k008b8fcb') }}
      </el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { ElTable } from 'element-plus'
import { dateFormatter2 } from '@/utils/formatTime'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { PurchaseInApi, PurchaseInVO } from '@/api/erp/purchase/in'
const { t } = useI18n()
defineOptions({ name: 'PurchaseInPaymentEnableList' })

const list = ref<PurchaseInVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const loading = ref(false) // 列表的加载中
const dialogVisible = ref(false) // 弹窗的是否展示
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  no: undefined,
  productId: undefined,
  inTime: [],
  paymentEnable: true,
  supplierId: undefined
})
const queryFormRef = ref() // 搜索的表单
const productList = ref<ProductVO[]>([]) // 产品列表

/** 选中操作 */
const selectionList = ref<PurchaseInVO[]>([])
const handleSelectionChange = (rows: PurchaseInVO[]) => {
  selectionList.value = rows
}

/** 打开弹窗 */
const open = async (supplierId: string) => {
  dialogVisible.value = true
  await nextTick() // 等待，避免 queryFormRef 为空
  // 加载可入库的订单列表
  queryParams.supplierId = supplierId
  await resetQuery()
  // 加载产品列表
  productList.value = await ProductApi.getProductSimpleList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交选择 */
const emits = defineEmits<{
  (e: 'success', value: PurchaseInVO[]): void
}>()
const submitForm = () => {
  try {
    emits('success', selectionList.value)
  } finally {
    // 关闭弹窗
    dialogVisible.value = false
  }
}

/** 加载列表  */
const getList = async () => {
  loading.value = true
  try {
    const data = await PurchaseInApi.getPurchaseInPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  selectionList.value = []
  getList()
}
</script>
