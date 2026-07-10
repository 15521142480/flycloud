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
      <el-form-item :label="t('auto.views.erp.finance.payment.index.k9d7a3f87')" prop="no">
        <el-input
          v-model="queryParams.no"
          :placeholder="t('auto.views.erp.finance.payment.index.ke5796e91')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.finance.payment.index.k334ddb69')" prop="paymentTime">
        <el-date-picker
          v-model="queryParams.paymentTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.erp.finance.payment.index.k1f291968')"
          :end-placeholder="t('auto.views.erp.finance.payment.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.finance.payment.index.k703c9eb0')" prop="supplierId">
        <el-select
          v-model="queryParams.supplierId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.finance.payment.index.kdbd21f67')"
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
      <el-form-item :label="t('auto.views.erp.finance.payment.index.k787ad1de')" prop="creator">
        <el-select
          v-model="queryParams.creator"
          clearable
          filterable
          :placeholder="t('auto.views.erp.finance.payment.index.k953a49d5')"
          class="!w-240px"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.erp.finance.payment.index.kb385b17a')"
        prop="financeUserId"
      >
        <el-select
          v-model="queryParams.financeUserId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.finance.payment.index.k31b8c129')"
          class="!w-240px"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.finance.payment.index.k48eb67df')" prop="accountId">
        <el-select
          v-model="queryParams.accountId"
          clearable
          filterable
          :placeholder="t('auto.views.erp.finance.payment.index.k8926bd63')"
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
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.erp.finance.payment.index.kdba277df')"
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
          :placeholder="t('auto.views.erp.finance.payment.index.k57e709d9')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.finance.payment.index.kedfd7658')" prop="bizNo">
        <el-input
          v-model="queryParams.bizNo"
          :placeholder="t('auto.views.erp.finance.payment.index.k39bf13ae')"
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
          v-hasPermi="['erp:finance-payment:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k96632235') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:finance-payment:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k7f184177') }}
        </el-button>
        <el-button
          type="danger"
          plain
          @click="handleDelete(selectionList.map((item) => item.id))"
          v-hasPermi="['erp:finance-payment:delete']"
          :disabled="selectionList.length === 0"
        >
          <Icon icon="ep:delete" class="mr-5px" /> {{ t('extra.k2482c529') }}
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
        :label="t('auto.views.erp.finance.payment.index.k70b20820')"
        type="selection"
      />
      <el-table-column
        min-width="180"
        :label="t('auto.views.erp.finance.payment.index.k9d7a3f87')"
        align="center"
        prop="no"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.k703c9eb0')"
        align="center"
        prop="supplierName"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.k334ddb69')"
        align="center"
        prop="paymentTime"
        :formatter="dateFormatter2"
        width="120px"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.k787ad1de')"
        align="center"
        prop="creatorName"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.kb385b17a')"
        align="center"
        prop="financeUserName"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.k48eb67df')"
        align="center"
        prop="accountName"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.kfa52376e')"
        align="center"
        prop="totalPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.kaab4abdb')"
        align="center"
        prop="discountPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('auto.views.erp.finance.payment.index.kecd8db17')"
        align="center"
        prop="paymentPrice"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('common.status')"
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
            v-hasPermi="['erp:finance-payment:list']"
          >
            {{ t('action.detail') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['erp:finance-payment:saveOrUpdate']"
            :disabled="scope.row.status === 20"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="primary"
            @click="handleUpdateStatus(scope.row.id, 20)"
            v-hasPermi="['erp:finance-payment:update-status']"
            v-if="scope.row.status === 10"
          >
            {{ t('auto.views.erp.finance.payment.index.k5ce60cb7') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleUpdateStatus(scope.row.id, 10)"
            v-hasPermi="['erp:finance-payment:update-status']"
            v-else
          >
            {{ t('auto.views.erp.finance.payment.index.k5e6e19f7') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete([scope.row.id])"
            v-hasPermi="['erp:finance-payment:delete']"
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
  <FinancePaymentForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import { FinancePaymentApi, FinancePaymentVO } from '@/api/erp/finance/payment'
import FinancePaymentForm from './FinancePaymentForm.vue'
import { UserVO } from '@/api/system/user'
import * as UserApi from '@/api/system/user'
import { erpPriceTableColumnFormatter } from '@/utils'
import { SupplierApi, SupplierVO } from '@/api/erp/purchase/supplier'
import { AccountApi, AccountVO } from '@/api/erp/finance/account'

/** ERP 付款单列表 */
const { t } = useI18n()
defineOptions({ name: 'ErpPurchaseOrder' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<FinancePaymentVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  no: undefined,
  paymentTime: [],
  supplierId: undefined,
  creator: undefined,
  financeUserId: undefined,
  accountId: undefined,
  status: undefined,
  remark: undefined,
  bizNo: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const supplierList = ref<SupplierVO[]>([]) // 供应商列表
const userList = ref<UserVO[]>([]) // 用户列表
const accountList = ref<AccountVO[]>([]) // 账户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await FinancePaymentApi.getFinancePaymentPage(queryParams)
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
const openForm = (type: string, id?: string) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (ids: string[]) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await FinancePaymentApi.deleteFinancePayment(ids)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
    selectionList.value = selectionList.value.filter((item) => !ids.includes(item.id))
  } catch {}
}

/** 审批/反审批操作 */
const handleUpdateStatus = async (id: string, status: number) => {
  try {
    // 审批的二次确认
    await message.confirm(
      t('extra.kf3ad9dd3', {
        p0:
          status === 20
            ? t('auto.views.erp.finance.payment.index.k5ce60cb7')
            : t('auto.views.erp.finance.payment.index.k5e6e19f7')
      })
    )
    // 发起审批
    await FinancePaymentApi.updateFinancePaymentStatus(id, status)
    message.success(
      t('extra.k199cf463', {
        p0:
          status === 20
            ? t('auto.views.erp.finance.payment.index.k5ce60cb7')
            : t('auto.views.erp.finance.payment.index.k5e6e19f7')
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
    const data = await FinancePaymentApi.exportFinancePayment(queryParams)
    download.excel(data, t('auto.views.erp.finance.payment.index.k37c42501'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 选中操作 */
const selectionList = ref<FinancePaymentVO[]>([])
const handleSelectionChange = (rows: FinancePaymentVO[]) => {
  selectionList.value = rows
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载供应商、用户、账户
  supplierList.value = await SupplierApi.getSupplierSimpleList()
  userList.value = await UserApi.getSimpleUserList()
  accountList.value = await AccountApi.getAccountSimpleList()
})
// TODO 芋艿：可优化功能：列表界面，支持导入
// TODO 芋艿：可优化功能：详情界面，支持打印
</script>
