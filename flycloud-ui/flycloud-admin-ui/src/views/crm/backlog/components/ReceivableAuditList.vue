<!-- 待审核回款 -->
<template>
  <ContentWrap>
    <div class="pb-5 text-xl">
      {{ t('auto.views.crm.backlog.components.ReceivableAuditList.kda707675') }}
    </div>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item
        :label="t('auto.views.crm.backlog.components.ReceivableAuditList.keb5aba97')"
        prop="auditStatus"
      >
        <el-select
          v-model="queryParams.auditStatus"
          class="!w-240px"
          :placeholder="t('common.status')"
          @change="handleQuery"
        >
          <el-option
            v-for="(option, index) in AUDIT_STATUS"
            :label="option.label"
            :value="option.value"
            :key="index"
          />
        </el-select>
      </el-form-item>
    </el-form>
  </ContentWrap>
  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.backlog.components.ReceivableAuditList.k89c860f8')"
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
            @click="handleProcessDetail(scope.row)"
          >
            {{ t('extra.k68aad05a') }}
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
</template>

<script setup lang="ts">
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import * as ReceivableApi from '@/api/crm/receivable'
import { AUDIT_STATUS } from './common'
import { erpPriceTableColumnFormatter } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'CrmReceivableAuditList' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  auditStatus: 10
})
const queryFormRef = ref() // 搜索的表单

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

/** 查看审批 */
const handleProcessDetail = (row: ReceivableApi.ReceivableVO) => {
  push({ name: 'BpmProcessInstanceDetail', query: { id: row.processInstanceId } })
}

/** 打开回款详情 */
const { push } = useRouter()
const openDetail = (id: string) => {
  push({ name: 'CrmReceivableDetail', params: { id } })
}

/** 打开客户详情 */
const openCustomerDetail = (id: string) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

/** 打开合同详情 */
const openContractDetail = (id: string) => {
  push({ name: 'CrmContractDetail', params: { id } })
}

/** 激活时 */
onActivated(async () => {
  await getList()
})

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
