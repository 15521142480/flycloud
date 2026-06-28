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
      <el-form-item :label="t('auto.views.crm.receivable.plan.index.ke941d410')" prop="customerId">
        <el-select
          v-model="queryParams.customerId"
          class="!w-240px"
          :placeholder="t('auto.views.crm.receivable.plan.index.k6bdb05d6')"
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
      <el-form-item :label="t('auto.views.crm.receivable.plan.index.k17b34173')" prop="contractNo">
        <el-input
          v-model="queryParams.contractNo"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.crm.receivable.plan.index.kae0dd683')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.kcdd45a29') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k007ed55c') }}
        </el-button>
        <el-button
          v-hasPermi="['crm:receivable-plan:create']"
          plain
          type="primary"
          @click="openForm('create')"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.k9f4c1afe') }}
        </el-button>
        <el-button
          v-hasPermi="['crm:receivable-plan:export']"
          :loading="exportLoading"
          plain
          type="success"
          @click="handleExport"
        >
          <Icon class="mr-5px" icon="ep:download" />
          {{ t('extra.k660206a7') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('auto.views.crm.receivable.plan.index.k0bbf4db1')" name="1" />
      <el-tab-pane :label="t('auto.views.crm.receivable.plan.index.k72715f2e')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.receivable.plan.index.ke941d410')"
        prop="customerName"
        width="150"
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
        width="200px"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.receivable.plan.components.ReceivablePlanList.k389372a5')"
        prop="period"
      >
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.period }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('extra.keb8da7ed')"
        prop="price"
        width="160"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.kf5888bd3')"
        prop="returnTime"
        width="180px"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k4eb4f0ab')"
        prop="remindDays"
        width="150"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.receivable.plan.components.ReceivablePlanList.k1ce64935')"
        prop="remindTime"
        width="180px"
        :formatter="dateFormatter2"
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
      <el-table-column align="center" :label="t('common.remark')" prop="remark" />
      <el-table-column
        :label="t('auto.views.crm.business.BusinessForm.k974d383f')"
        prop="ownerUserName"
        width="120"
      />
      <el-table-column
        align="center"
        :label="t('extra.kf49c0ad3')"
        prop="receivable.price"
        width="160"
      >
        <template #default="scope">
          <el-text v-if="scope.row.receivable">
            {{ erpPriceInputFormatter(scope.row.receivable.price) }}
          </el-text>
          <el-text v-else>{{ erpPriceInputFormatter(0) }}</el-text>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('extra.k7ebebecf')"
        prop="receivable.returnTime"
        width="180px"
        :formatter="dateFormatter2"
      />
      <el-table-column
        align="center"
        :label="t('extra.kf49c0ad3')"
        prop="receivable.price"
        width="160"
      >
        <template #default="scope">
          <el-text v-if="scope.row.receivable">
            {{ erpPriceInputFormatter(scope.row.price - scope.row.receivable.price) }}
          </el-text>
          <el-text v-else>{{ erpPriceInputFormatter(scope.row.price) }}</el-text>
        </template>
      </el-table-column>
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
        width="100px"
      />
      <el-table-column align="center" fixed="right" :label="t('common.operation')" width="180px">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:receivable:create']"
            link
            type="success"
            @click="openReceivableForm(scope.row)"
            :disabled="scope.row.receivableId"
          >
            {{ t('extra.ka683c87a') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:receivable-plan:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:receivable-plan:delete']"
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
  <ReceivablePlanForm ref="formRef" @success="getList" />
  <ReceivableForm ref="receivableFormRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import ReceivablePlanForm from './ReceivablePlanForm.vue'
import * as CustomerApi from '@/api/crm/customer'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import { TabsPaneContext } from 'element-plus'
import ReceivableForm from '@/views/crm/receivable/ReceivableForm.vue'
const { t } = useI18n()
defineOptions({ name: 'ReceivablePlan' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  sceneType: '1', // 默认和 activeName 相等
  customerId: undefined,
  contractNo: undefined
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
    const data = await ReceivablePlanApi.getReceivablePlanPage(queryParams)
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

/** 创建回款操作 */
const receivableFormRef = ref()
const openReceivableForm = (row: ReceivablePlanApi.ReceivablePlanVO) => {
  receivableFormRef.value.open('create', undefined, row)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ReceivablePlanApi.deleteReceivablePlan(id)
    message.success(t('common.delSuccess'))
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
    const data = await ReceivablePlanApi.exportReceivablePlan(queryParams)
    download.excel(data, t('auto.views.crm.receivable.plan.index.ka2a0dff5'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 打开详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmReceivablePlanDetail', params: { id } })
}

/** 打开客户详情 */
const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 获得客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
})
</script>
