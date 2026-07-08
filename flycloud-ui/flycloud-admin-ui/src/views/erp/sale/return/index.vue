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
      <el-form-item :label="t('auto.views.erp.sale.return.index.k227ee7e4')" prop="no">
        <el-input
          v-model="queryParams.no"
          :placeholder="t('auto.views.erp.sale.return.index.k5f251d68')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.sale.return.index.k6cc98552')" prop="productId">
        <el-select
          v-model="queryParams.productId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.sale.return.index.k59a0d3d1')"
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
      <el-form-item :label="t('auto.views.erp.sale.return.index.k11f23e81')" prop="outTime">
        <el-date-picker
          v-model="queryParams.outTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.erp.sale.return.index.k1f291968')"
          :end-placeholder="t('auto.views.erp.sale.return.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.sale.return.index.kf2068706')" prop="customerId">
        <el-select
          v-model="queryParams.customerId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.sale.return.index.kf8789ed0')"
          class="!w-240px"
        >
          <el-option
            v-for="item in customerList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.sale.return.index.k55914e1c')" prop="warehouseId">
        <el-select
          v-model="queryParams.warehouseId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.sale.return.index.k7aaa3bbb')"
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
      <el-form-item :label="t('auto.views.erp.sale.return.index.k787ad1de')" prop="creator">
        <el-select
          v-model="queryParams.creator"
          clearable
          filterable
          :placeholder="t('auto.views.erp.sale.return.index.k953a49d5')"
          class="!w-240px"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.sale.return.index.k969d961c')" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          :placeholder="t('auto.views.erp.sale.return.index.k58b223d6')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.sale.return.index.k573e8d23')" prop="accountId">
        <el-select
          v-model="queryParams.accountId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.sale.return.index.k0cf19ca0')"
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
      <el-form-item :label="t('auto.views.erp.sale.return.index.k7d1f4b47')" prop="refundStatus">
        <el-select
          v-model="queryParams.refundStatus"
          :placeholder="t('auto.views.erp.sale.return.index.k4d97c509')"
          clearable
          class="!w-240px"
        >
          <el-option :label="t('auto.views.erp.sale.return.index.kbbd1ee81')" value="0" />
          <el-option :label="t('auto.views.erp.sale.return.index.kbe707265')" value="1" />
          <el-option :label="t('auto.views.erp.sale.return.index.ka95437c8')" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.sale.return.index.ka62215e6')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.erp.sale.return.index.k630ed508')"
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
          :placeholder="t('auto.views.erp.sale.return.index.k57e709d9')"
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
          v-hasPermi="['erp:sale-return:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.kf01d3b5e') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:sale-return:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k910506ee') }}
        </el-button>
        <el-button
          type="danger"
          plain
          @click="handleDelete(selectionList.map((item) => item.id))"
          v-hasPermi="['erp:sale-return:delete']"
          :disabled="selectionList.length === 0"
        >
          <Icon icon="ep:delete" class="mr-5px" /> {{ t('extra.kb1faed07') }}
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
        :label="t('auto.views.erp.sale.return.index.k70b20820')"
        type="selection"
      />
      <el-table-column
        min-width="180"
        :label="t('auto.views.erp.sale.return.index.k227ee7e4')"
        align="center"
        prop="no"
      />
      <el-table-column
        :label="t('auto.views.erp.sale.return.index.k90095856')"
        align="center"
        prop="productNames"
        min-width="200"
      />
      <el-table-column
        :label="t('auto.views.erp.sale.return.index.kf2068706')"
        align="center"
        prop="customerName"
      />
      <el-table-column
        :label="t('auto.views.erp.sale.return.index.k11f23e81')"
        align="center"
        prop="returnTime"
        :formatter="dateFormatter2"
        width="120px"
      />
      <el-table-column
        :label="t('auto.views.erp.sale.return.index.k787ad1de')"
        align="center"
        prop="creatorName"
      />
      <el-table-column
        :label="t('auto.views.erp.sale.return.index.kc2bdcf08')"
        align="center"
        prop="totalCount"
        :formatter="erpCountTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.sale.return.index.kde1d144b')"
        align="center"
        prop="totalPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.sale.return.index.ke626b428')"
        align="center"
        prop="refundPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column :label="t('auto.views.erp.sale.return.index.kfff7e95a')" align="center">
        <template #default="scope">
          <span v-if="scope.row.refundPrice === scope.row.totalPrice">0</span>
          <el-tag type="danger" v-else>
            {{ erpPriceInputFormatter(scope.row.totalPrice - scope.row.refundPrice) }}
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
            v-hasPermi="['erp:sale-return:list']"
          >
            {{ t('action.detail') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['erp:sale-return:saveOrUpdate']"
            :disabled="scope.row.status === 20"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleUpdateStatus(scope.row.id, 20)"
            v-hasPermi="['erp:sale-return:update-status']"
            v-if="scope.row.status === 10"
          >
            {{ t('auto.views.erp.finance.payment.index.k5ce60cb7') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleUpdateStatus(scope.row.id, 10)"
            v-hasPermi="['erp:sale-return:update-status']"
            v-else
          >
            {{ t('auto.views.erp.finance.payment.index.k5e6e19f7') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete([scope.row.id])"
            v-hasPermi="['erp:sale-return:delete']"
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
  <SaleReturnForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import { SaleReturnApi, SaleReturnVO } from '@/api/erp/sale/return'
import SaleReturnForm from './SaleReturnForm.vue'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { UserVO } from '@/api/system/user'
import * as UserApi from '@/api/system/user'
import {
  erpCountTableColumnFormatter,
  erpPriceInputFormatter,
  erpPriceTableColumnFormatter
} from '@/utils'
import { CustomerApi, CustomerVO } from '@/api/erp/sale/customer'
import { WarehouseApi, WarehouseVO } from '@/api/erp/stock/warehouse'
import { AccountApi, AccountVO } from '@/api/erp/finance/account'

/** ERP 销售退货列表 */
const { t } = useI18n()
defineOptions({ name: 'ErpSaleReturn' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<SaleReturnVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  no: undefined,
  customerId: undefined,
  productId: undefined,
  warehouseId: undefined,
  returnTime: [],
  orderNo: undefined,
  accountId: undefined,
  status: undefined,
  remark: undefined,
  creator: undefined,
  refundStatus: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const productList = ref<ProductVO[]>([]) // 产品列表
const customerList = ref<CustomerVO[]>([]) // 客户列表
const userList = ref<UserVO[]>([]) // 用户列表
const warehouseList = ref<WarehouseVO[]>([]) // 仓库列表
const accountList = ref<AccountVO[]>([]) // 账户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await SaleReturnApi.getSaleReturnPage(queryParams)
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
    await SaleReturnApi.deleteSaleReturn(ids)
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
      t('extra.kdf92d27b', {
        p0:
          status === 20
            ? t('auto.views.erp.sale.return.index.k5ce60cb7')
            : t('auto.views.erp.sale.return.index.k5e6e19f7')
      })
    )
    // 发起审批
    await SaleReturnApi.updateSaleReturnStatus(id, status)
    message.success(
      t('extra.kbefe029b', {
        p0:
          status === 20
            ? t('auto.views.erp.sale.return.index.k5ce60cb7')
            : t('auto.views.erp.sale.return.index.k5e6e19f7')
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
    const data = await SaleReturnApi.exportSaleReturn(queryParams)
    download.excel(data, t('auto.views.erp.sale.return.index.keffb177f'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 选中操作 */
const selectionList = ref<SaleReturnVO[]>([])
const handleSelectionChange = (rows: SaleReturnVO[]) => {
  selectionList.value = rows
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载产品、仓库列表、客户
  productList.value = await ProductApi.getProductSimpleList()
  customerList.value = await CustomerApi.getCustomerSimpleList()
  userList.value = await UserApi.getSimpleUserList()
  warehouseList.value = await WarehouseApi.getWarehouseSimpleList()
  accountList.value = await AccountApi.getAccountSimpleList()
})
// TODO 芋艿：可优化功能：列表界面，支持导入
// TODO 芋艿：可优化功能：详情界面，支持打印
</script>
