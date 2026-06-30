<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.crm.receivable.index.k89c860f8')" prop="no">
        <el-input
          v-model="queryParams.no"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.crm.receivable.index.kd4d051d6')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.crm.receivable.index.ke941d410')" prop="customerId">
        <el-select
          v-model="queryParams.customerId"
          class="!w-240px"
          :placeholder="t('auto.views.crm.receivable.index.k6bdb05d6')"
          @keyup.enter="handleQuery"
        >
          <el-option
            v-for="item in customerList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.kc217462d') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k252a41f2') }}
        </el-button>
        <el-button
          v-hasPermi="['crm:receivable:saveOrUpdate']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.k9cdea0f4') }}
        </el-button>
        <el-button
          v-hasPermi="['crm:receivable:export']"
          :loading="exportLoading"
          plain
          type="success"
          @click="handleExport"
        >
          <Icon class="mr-5px" icon="ep:download" />
          {{ t('extra.k1b7477e6') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('auto.views.crm.receivable.index.k0bbf4db1')" name="1" />
      <el-tab-pane :label="t('auto.views.crm.receivable.index.k1640a0ff')" name="2" />
      <el-tab-pane :label="t('auto.views.crm.receivable.index.k72715f2e')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.receivable.index.k89c860f8')"
        prop="no"
        width="180"
      >
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
        prop="customerName"
        width="120"
      >
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openCustomerDetail(scope.row.customerId)"
          >
            {{ scope.row.customerName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.crm.backlog.components.ContractAuditList.k17b34173')"
        prop="contractNo"
        width="180"
      >
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openContractDetail(scope.row.contractId)"
          >
            {{ scope.row.contract.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        :label="t('auto.views.crm.receivable.ReceivableForm.kd0132503')"
        prop="returnTime"
        width="150px"
      />
      <el-table-column
        align="center"
        :label="t('extra.k5961f09c')"
        prop="price"
        width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.receivable.ReceivableForm.k6c0d9ca0')"
        prop="returnType"
        width="130px"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_RECEIVABLE_RETURN_TYPE" :value="scope.row.returnType" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('common.remark')" prop="remark" width="200" />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k4687c195')"
        prop="contract.totalPrice"
        width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.business.BusinessForm.k974d383f')"
        prop="ownerUserName"
        width="120"
      />
      <el-table-column
        align="center"
        :label="t('profile.user.dept')"
        prop="ownerUserDeptName"
        width="100px"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.updateTime')"
        prop="updateTime"
        width="180px"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        width="180px"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
        prop="creatorName"
        width="120"
      />
      <el-table-column
        align="center"
        fixed="right"
        :label="t('extra.k764a6609')"
        prop="auditStatus"
        width="120"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" :label="t('common.operation')" width="180px">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:receivable:saveOrUpdate']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-if="scope.row.auditStatus === 0"
            v-hasPermi="['crm:receivable:saveOrUpdate']"
            link
            type="primary"
            @click="handleSubmit(scope.row)"
          >
            {{ t('extra.k646db084') }}
          </el-button>
          <el-button
            v-else
            v-hasPermi="['crm:receivable:saveOrUpdate']"
            link
            type="primary"
            @click="handleProcessDetail(scope.row)"
          >
            {{ t('extra.k68aad05a') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:receivable:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
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

  <!-- 表单弹窗：添加/修改 -->
  <ReceivableForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ReceivableApi from '@/api/crm/receivable'
import ReceivableForm from './ReceivableForm.vue'
import * as CustomerApi from '@/api/crm/customer'
import { TabsPaneContext } from 'element-plus'
import { erpPriceTableColumnFormatter } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'Receivable' })

const message = useMessage() // 消息弹窗
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  sceneType: '1', // 默认和 activeName 相等
  no: undefined,
  customerId: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const activeName = ref('1') // 列表 tab
const customerList = ref<CustomerApi.CustomerVO[]>([]) // 客户列表

/** tab 切换 */
const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.sceneType = tab.paneName
  handleQuery()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ReceivableApi.getReceivablePage(queryParams)
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
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ReceivableApi.deleteReceivable(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 提交审核 **/
const handleSubmit = async (row: ReceivableApi.ReceivableVO) => {
  await message.confirm(t('extra.k6f991214', { p0: row.no }))
  await ReceivableApi.submitReceivable(row.id)
  message.success(t('auto.views.crm.receivable.index.k7165ee3f'))
  await getList()
}

/** 查看审批 */
const handleProcessDetail = (row: ReceivableApi.ReceivableVO) => {
  push({ name: 'BpmProcessInstanceDetail', query: { id: row.processInstanceId } })
}

/** 打开回款详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmReceivableDetail', params: { id } })
}

/** 打开客户详情 */
const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

/** 打开合同详情 */
const openContractDetail = (id: number) => {
  push({ name: 'CrmContractDetail', params: { id } })
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ReceivableApi.exportReceivable(queryParams)
    download.excel(data, t('auto.views.crm.receivable.index.k855d0cca'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 获得客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
})
</script>
