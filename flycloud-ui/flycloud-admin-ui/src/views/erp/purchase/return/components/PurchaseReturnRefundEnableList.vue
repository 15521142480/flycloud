<!-- 可退款的采购退货单列表 -->
<template>
  <Dialog
    :title="t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k64234b46')"
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
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k227ee7e4')
          "
          prop="no"
        >
          <el-input
            v-model="queryParams.no"
            :placeholder="
              t(
                'auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k5f251d68'
              )
            "
            clearable
            @keyup.enter="handleQuery"
            class="!w-160px"
          />
        </el-form-item>
        <el-form-item
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k6cc98552')
          "
          prop="productId"
        >
          <el-select
            v-model="queryParams.productId"
            clearable
            filterable
            :placeholder="
              t(
                'auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k59a0d3d1'
              )
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
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k11f23e81')
          "
          prop="orderTime"
        >
          <el-date-picker
            v-model="queryParams.returnTime"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            :start-placeholder="
              t(
                'auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k1f291968'
              )
            "
            :end-placeholder="
              t(
                'auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.kf4b9b2b5'
              )
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
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k70b20820')
          "
          type="selection"
        />
        <el-table-column
          min-width="180"
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k227ee7e4')
          "
          align="center"
          prop="no"
        />
        <el-table-column
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k703c9eb0')
          "
          align="center"
          prop="supplierName"
        />
        <el-table-column
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k90095856')
          "
          align="center"
          prop="productNames"
          min-width="200"
        />
        <el-table-column
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k11f23e81')
          "
          align="center"
          prop="returnTime"
          :formatter="dateFormatter2"
          width="120px"
        />
        <el-table-column
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.k787ad1de')
          "
          align="center"
          prop="creatorName"
        />
        <el-table-column
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.kde1d144b')
          "
          align="center"
          prop="totalPrice"
          :formatter="erpPriceTableColumnFormatter"
        />
        <el-table-column
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.ke626b428')
          "
          align="center"
          prop="refundPrice"
          :formatter="erpPriceTableColumnFormatter"
        />
        <el-table-column
          :label="
            t('auto.views.erp.purchase.return.components.PurchaseReturnRefundEnableList.kfff7e95a')
          "
          align="center"
        >
          <template #default="scope">
            <span v-if="scope.row.refundPrice === scope.row.totalPrice">0</span>
            <el-tag type="danger" v-else>
              {{ erpPriceInputFormatter(scope.row.totalPrice - scope.row.refundPrice) }}
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
        确 定
      </el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { ElTable } from 'element-plus'
import { dateFormatter2 } from '@/utils/formatTime'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { PurchaseReturnApi, PurchaseReturnVO } from '@/api/erp/purchase/return'
import { SaleReturnVO } from '@/api/erp/sale/return'
const { t } = useI18n()
defineOptions({ name: 'PurchaseInPaymentEnableList' })

const list = ref<PurchaseReturnVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const loading = ref(false) // 列表的加载中
const dialogVisible = ref(false) // 弹窗的是否展示
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  no: undefined,
  productId: undefined,
  returnTime: [],
  refundEnable: true,
  supplierId: undefined
})
const queryFormRef = ref() // 搜索的表单
const productList = ref<ProductVO[]>([]) // 产品列表

/** 选中操作 */
const selectionList = ref<SaleReturnVO[]>([])
const handleSelectionChange = (rows: SaleReturnVO[]) => {
  selectionList.value = rows
}

/** 打开弹窗 */
const open = async (supplierId: number) => {
  dialogVisible.value = true
  await nextTick() // 等待，避免 queryFormRef 为空
  // 加载列表
  queryParams.supplierId = supplierId
  await resetQuery()
  // 加载产品列表
  productList.value = await ProductApi.getProductSimpleList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交选择 */
const emits = defineEmits<{
  (e: 'success', value: SaleReturnVO[]): void
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
    const data = await PurchaseReturnApi.getPurchaseReturnPage(queryParams)
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
