<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.erp.purchase.in.index.ka9f67da2')" prop="no">
        <el-input
          v-model="queryParams.no"
          :placeholder="t('auto.views.erp.purchase.in.index.keb25b35c')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.k6cc98552')" prop="productId">
        <el-select
          v-model="queryParams.productId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.purchase.in.index.k59a0d3d1')"
          class="!w-240px"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.k661cec87')" prop="inTime">
        <el-date-picker
          v-model="queryParams.inTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.erp.purchase.in.index.k1f291968')"
          :end-placeholder="t('auto.views.erp.purchase.in.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.k703c9eb0')" prop="supplierId">
        <el-select
          v-model="queryParams.supplierId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.purchase.in.index.kdbd21f67')"
          class="!w-240px"
        >
          <el-option
            v-for="item in supplierList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.k55914e1c')" prop="warehouseId">
        <el-select
          v-model="queryParams.warehouseId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.purchase.in.index.k7aaa3bbb')"
          class="!w-240px"
        >
          <el-option
            v-for="item in warehouseList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.k787ad1de')" prop="creator">
        <el-select
          v-model="queryParams.creator"
          clearable
          filterable
          :placeholder="t('auto.views.erp.purchase.in.index.k953a49d5')"
          class="!w-240px"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.k969d961c')" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          :placeholder="t('auto.views.erp.purchase.in.index.k58b223d6')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.k573e8d23')" prop="accountId">
        <el-select
          v-model="queryParams.accountId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.purchase.in.index.k0cf19ca0')"
          class="!w-240px"
        >
          <el-option
            v-for="item in accountList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.kb6959bb1')" prop="paymentStatus">
        <el-select
          v-model="queryParams.paymentStatus"
          :placeholder="t('auto.views.erp.purchase.in.index.k933ea51e')"
          clearable
          class="!w-240px"
        >
          <el-option :label="t('auto.views.erp.purchase.in.index.k059ea2d4')" value="0" />
          <el-option :label="t('auto.views.erp.purchase.in.index.ka66b7457')" value="1" />
          <el-option :label="t('auto.views.erp.purchase.in.index.kc5a22e0c')" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.purchase.in.index.ka62215e6')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.erp.purchase.in.index.k630ed508')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_AUDIT_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="queryParams.remark"
          :placeholder="t('auto.views.erp.purchase.in.index.k57e709d9')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"
          ><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button
        >
        <el-button @click="resetQuery"
          ><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button
        >
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['erp:purchase-in:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k4b1c854c') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:purchase-in:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k6fce4937') }}
        </el-button>
        <el-button
          type="danger"
          plain
          @click="handleDelete(selectionList.map((item) => item.id))"
          v-hasPermi="['erp:purchase-in:delete']"
          :disabled="selectionList.length === 0"
        >
          <Icon icon="ep:delete" class="mr-5px" /> {{ t('extra.kda15a0e7') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="list"
      :stripe="true"
      :show-overflow-tooltip="true"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        width="30"
        :label="t('auto.views.erp.purchase.in.index.k70b20820')"
        type="selection"
      />
      <el-table-column
        min-width="180"
        :label="t('auto.views.erp.purchase.in.index.ka9f67da2')"
        align="center"
        prop="no"
      />
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.k90095856')"
        align="center"
        prop="productNames"
        min-width="200"
      />
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.k703c9eb0')"
        align="center"
        prop="supplierName"
      />
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.k661cec87')"
        align="center"
        prop="inTime"
        :formatter="dateFormatter2"
        width="120px"
      />
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.k787ad1de')"
        align="center"
        prop="creatorName"
      />
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.kc2bdcf08')"
        align="center"
        prop="totalCount"
        :formatter="erpCountTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.k20104358')"
        align="center"
        prop="totalPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.k986c161f')"
        align="center"
        prop="paymentPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column :label="t('auto.views.erp.purchase.in.index.k3ddc8eaa')" align="center">
        <template #default="scope">
          <span v-if="scope.row.paymentPrice === scope.row.totalPrice">0</span>
          <el-tag type="danger" v-else>
            {{ erpPriceInputFormatter(scope.row.totalPrice - scope.row.paymentPrice) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.erp.purchase.in.index.ka62215e6')"
        align="center"
        fixed="right"
        width="90"
        prop="status"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_AUDIT_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.operation')" align="center" fixed="right" width="220">
        <template #default="scope">
          <el-button
            link
            @click="openForm('detail', scope.row.id)"
            v-hasPermi="['erp:purchase-in:list']"
          >
            {{ t('action.detail') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['erp:purchase-in:saveOrUpdate']"
            :disabled="scope.row.status === 20"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleUpdateStatus(scope.row.id, 20)"
            v-hasPermi="['erp:purchase-in:update-status']"
            v-if="scope.row.status === 10"
          >
            {{ t('auto.views.erp.finance.payment.index.k5ce60cb7') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleUpdateStatus(scope.row.id, 10)"
            v-hasPermi="['erp:purchase-in:update-status']"
            v-else
          >
            {{ t('auto.views.erp.finance.payment.index.k5e6e19f7') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete([scope.row.id])"
            v-hasPermi="['erp:purchase-in:delete']"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <PurchaseInForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import { PurchaseInApi, PurchaseInVO } from '@/api/erp/purchase/in'
import PurchaseInForm from './PurchaseInForm.vue'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { UserVO } from '@/api/system/user'
import * as UserApi from '@/api/system/user'
import {
  erpCountTableColumnFormatter,
  erpPriceInputFormatter,
  erpPriceTableColumnFormatter
} from '@/utils'
import { WarehouseApi, WarehouseVO } from '@/api/erp/stock/warehouse'
import { AccountApi, AccountVO } from '@/api/erp/finance/account'
import { SupplierApi, SupplierVO } from '@/api/erp/purchase/supplier'

/** ERP 销售入库列表 */
const { t } = useI18n()
defineOptions({ name: 'ErpPurchaseIn' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<PurchaseInVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  no: undefined,
  supplierId: undefined,
  productId: undefined,
  warehouseId: undefined,
  inTime: [],
  orderNo: undefined,
  paymentStatus: undefined,
  accountId: undefined,
  status: undefined,
  remark: undefined,
  creator: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const productList = ref<ProductVO[]>([]) // 产品列表
const supplierList = ref<SupplierVO[]>([]) // 供应商列表
const userList = ref<UserVO[]>([]) // 用户列表
const warehouseList = ref<WarehouseVO[]>([]) // 仓库列表
const accountList = ref<AccountVO[]>([]) // 账户列表

/** 查询列表 */
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

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (ids: number[]) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await PurchaseInApi.deletePurchaseIn(ids)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
    selectionList.value = selectionList.value.filter((item) => !ids.includes(item.id))
  } catch {}
}

/** 审批/反审批操作 */
const handleUpdateStatus = async (id: number, status: number) => {
  try {
    // 审批的二次确认
    await message.confirm(
      t('extra.k46ad5e71', {
        p0:
          status === 20
            ? t('auto.views.erp.purchase.in.index.k5ce60cb7')
            : t('auto.views.erp.purchase.in.index.k5e6e19f7')
      })
    )
    // 发起审批
    await PurchaseInApi.updatePurchaseInStatus(id, status)
    message.success(
      t('extra.k9f796ac3', {
        p0:
          status === 20
            ? t('auto.views.erp.purchase.in.index.k5ce60cb7')
            : t('auto.views.erp.purchase.in.index.k5e6e19f7')
      })
    )
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await PurchaseInApi.exportPurchaseIn(queryParams)
    download.excel(data, t('auto.views.erp.purchase.in.index.kddcf1792'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 选中操作 */
const selectionList = ref<PurchaseInVO[]>([])
const handleSelectionChange = (rows: PurchaseInVO[]) => {
  selectionList.value = rows
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载产品、仓库列表、供应商
  productList.value = await ProductApi.getProductSimpleList()
  supplierList.value = await SupplierApi.getSupplierSimpleList()
  userList.value = await UserApi.getSimpleUserList()
  warehouseList.value = await WarehouseApi.getWarehouseSimpleList()
  accountList.value = await AccountApi.getAccountSimpleList()
})
// TODO 芋艿：可优化功能：列表界面，支持导入
// TODO 芋艿：可优化功能：详情界面，支持打印
</script>
