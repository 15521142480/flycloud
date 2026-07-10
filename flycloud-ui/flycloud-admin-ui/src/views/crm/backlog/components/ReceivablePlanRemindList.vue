<!-- 待回款提醒 -->
<template>
  <ContentWrap>
    <div class="pb-5 text-xl">{{
      t('auto.views.crm.backlog.components.ReceivablePlanRemindList.kd40192ef')
    }}</div>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item
        :label="t('auto.views.crm.backlog.components.ReceivablePlanRemindList.keb5aba97')"
        prop="remindType"
      >
        <el-select
          v-model="queryParams.remindType"
          class="!w-240px"
          :placeholder="t('common.status')"
          @change="handleQuery"
        >
          <el-option
            v-for="(option, index) in RECEIVABLE_REMIND_TYPE"
            :label="option.label"
            :value="option.value"
            :key="index"
          />
        </el-select>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.backlog.components.ReceivablePlanRemindList.ke941d410')"
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
            v-hasPermi="['crm:receivable:saveOrUpdate']"
            link
            type="success"
            @click="openReceivableForm(scope.row)"
            :disabled="scope.row.receivableId"
          >
            {{ t('extra.ka683c87a') }}
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
  <ReceivableForm ref="receivableFormRef" @success="getList" />
</template>

<script setup lang="ts">
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import { RECEIVABLE_REMIND_TYPE } from './common'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import ReceivableForm from '@/views/crm/receivable/ReceivableForm.vue'
const { t } = useI18n()
defineOptions({ name: 'ReceivablePlanRemindList' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  remindType: 1
})
const queryFormRef = ref() // 搜索的表单

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

/** 创建回款操作 */
const receivableFormRef = ref()
const openReceivableForm = (row: ReceivablePlanApi.ReceivablePlanVO) => {
  receivableFormRef.value.open('create', undefined, row)
}

/** 打开详情 */
const { push } = useRouter()
const openDetail = (id: string) => {
  push({ name: 'CrmReceivablePlanDetail', params: { id } })
}

/** 打开客户详情 */
const openCustomerDetail = (id: string) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

/** 激活时 */
onActivated(async () => {
  await getList()
})

/** 初始化 **/
onMounted(async () => {
  await getList()
})
</script>
