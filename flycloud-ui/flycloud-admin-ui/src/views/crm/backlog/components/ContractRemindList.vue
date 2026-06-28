<!-- 即将到期的合同 -->
<template>
  <ContentWrap>
    <div class="pb-5 text-xl">
      {{ t('auto.views.crm.backlog.components.ContractRemindList.k9c295b69') }}
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
        :label="t('auto.views.crm.backlog.components.ContractRemindList.kc411b3ba')"
        prop="expiryType"
      >
        <el-select
          v-model="queryParams.expiryType"
          class="!w-240px"
          :placeholder="t('common.status')"
          @change="handleQuery"
        >
          <el-option
            v-for="(option, index) in CONTRACT_EXPIRY_TYPE"
            :label="option.label"
            :value="option.value"
            :key="index"
          />
        </el-select>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.backlog.components.ContractRemindList.k17b34173')"
        prop="no"
        width="180"
      />
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.backlog.components.ContractRemindList.kb8fbf277')"
        prop="name"
        width="160"
      >
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
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
        :label="t('auto.views.crm.business.BusinessForm.k84b59248')"
        prop="businessName"
        width="130"
      >
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openBusinessDetail(scope.row.businessId)"
          >
            {{ scope.row.businessName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k4687c195')"
        prop="totalPrice"
        width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k01f1aece')"
        prop="orderDate"
        width="120"
        :formatter="dateFormatter2"
      />
      <el-table-column
        align="center"
        :label="t('extra.k159bc8c4')"
        prop="startTime"
        width="120"
        :formatter="dateFormatter2"
      />
      <el-table-column
        align="center"
        :label="t('extra.k65a66e31')"
        prop="endTime"
        width="120"
        :formatter="dateFormatter2"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.contract.ContractForm.kbebe405e')"
        prop="contactName"
        width="130"
      >
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openContactDetail(scope.row.signContactId)"
          >
            {{ scope.row.signContactName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.crm.contract.ContractForm.kcc2b34c1')"
        prop="signUserName"
        width="130"
      />
      <el-table-column align="center" :label="t('common.remark')" prop="remark" width="200" />
      <el-table-column
        align="center"
        :label="t('extra.k6f614f6c')"
        prop="totalReceivablePrice"
        width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        :label="t('extra.k0b91adcf')"
        prop="totalReceivablePrice"
        width="140"
        :formatter="erpPriceTableColumnFormatter"
      >
        <template #default="scope">
          {{ erpPriceInputFormatter(scope.row.totalPrice - scope.row.totalReceivablePrice) }}
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('extra.kffa0750f')"
        prop="contactLastTime"
        width="180px"
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
        :label="t('auto.views.crm.backlog.components.ContractAuditList.keb5aba97')"
        prop="auditStatus"
        width="120"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column fixed="right" :label="t('common.operation')" width="90">
        <template #default="scope">
          <el-button
            link
            v-hasPermi="['crm:contract:update']"
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
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>
</template>

<script setup lang="ts" name="EndContract">
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import * as ContractApi from '@/api/crm/contract'
import { fenToYuanFormat } from '@/utils/formatter'
import { DICT_TYPE } from '@/utils/dict'
import { CONTRACT_EXPIRY_TYPE } from './common'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
const { t } = useI18n()
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  sceneType: '1', // 自己负责的
  expiryType: 1
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ContractApi.getContractPage(queryParams)
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
const handleProcessDetail = (row: ContractApi.ContractVO) => {
  push({ name: 'BpmProcessInstanceDetail', query: { id: row.processInstanceId } })
}

/** 打开合同详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmContractDetail', params: { id } })
}

/** 打开客户详情 */
const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

/** 打开联系人详情 */
const openContactDetail = (id: number) => {
  push({ name: 'CrmContactDetail', params: { id } })
}

/** 打开商机详情 */
const openBusinessDetail = (id: number) => {
  push({ name: 'CrmBusinessDetail', params: { id } })
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
