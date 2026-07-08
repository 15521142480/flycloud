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
      <el-form-item :label="t('auto.views.crm.contract.index.k17b34173')" prop="no">
        <el-input
          v-model="queryParams.no"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.crm.contract.index.kae0dd683')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.crm.contract.index.kb8fbf277')" prop="name">
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.crm.contract.index.kd336a47f')"
          @keyup.enter="handleQuery"
        />
        <el-form-item :label="t('auto.views.crm.contract.index.kf2068706')" prop="customerId">
          <el-select
            v-model="queryParams.customerId"
            class="!w-240px"
            clearable
            lable-key="name"
            :placeholder="t('auto.views.crm.contract.index.k6bdb05d6')"
            value-key="id"
            @keyup.enter="handleQuery"
          >
            <el-option
              v-for="item in customerList"
              :key="item.id"
              :label="item.name"
              :value="item.id!"
            />
          </el-select>
        </el-form-item>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k508c1a32') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.kc5217b66') }}
        </el-button>
        <el-button v-hasPermi="['crm:contract:saveOrUpdate']" type="primary" @click="openForm('create')">
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.ka93aca74') }}
        </el-button>
        <el-button
          v-hasPermi="['crm:contract:export']"
          :loading="exportLoading"
          plain
          type="success"
          @click="handleExport"
        >
          <Icon class="mr-5px" icon="ep:download" />
          {{ t('extra.k282cf4aa') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('auto.views.crm.contract.index.k0bbf4db1')" name="1" />
      <el-tab-pane :label="t('auto.views.crm.contract.index.k1640a0ff')" name="2" />
      <el-tab-pane :label="t('auto.views.crm.contract.index.k72715f2e')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.contract.index.k17b34173')"
        prop="no"
        width="180"
      />
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.contract.index.kb8fbf277')"
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
      <el-table-column fixed="right" :label="t('common.operation')" width="250">
        <template #default="scope">
          <el-button
            v-if="scope.row.auditStatus === 0"
            v-hasPermi="['crm:contract:saveOrUpdate']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-if="scope.row.auditStatus === 0"
            v-hasPermi="['crm:contract:saveOrUpdate']"
            link
            type="primary"
            @click="handleSubmit(scope.row)"
          >
            {{ t('extra.k646db084') }}
          </el-button>
          <el-button
            v-else
            link
            v-hasPermi="['crm:contract:saveOrUpdate']"
            type="primary"
            @click="handleProcessDetail(scope.row)"
          >
            {{ t('extra.k68aad05a') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:contract:list']"
            link
            type="primary"
            @click="openDetail(scope.row.id)"
          >
            {{ t('action.detail') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:contract:delete']"
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
  <ContractForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ContractApi from '@/api/crm/contract'
import ContractForm from './ContractForm.vue'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import * as CustomerApi from '@/api/crm/customer'
import { TabsPaneContext } from 'element-plus'
const { t } = useI18n()
defineOptions({ name: 'CrmContract' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  sceneType: '1', // 默认和 activeName 相等
  name: null,
  customerId: null,
  orderDate: [],
  no: null
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
    await ContractApi.deleteContract(id)
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
    const data = await ContractApi.exportContract(queryParams)
    download.excel(data, t('auto.views.crm.contract.index.kd0ef23ec'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 提交审核 **/
const handleSubmit = async (row: ContractApi.ContractVO) => {
  await message.confirm(t('extra.k54cbe02f', { p0: row.name }))
  await ContractApi.submitContract(row.id)
  message.success(t('auto.views.crm.contract.index.k7165ee3f'))
  await getList()
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

/** 初始化 **/
onMounted(async () => {
  await getList()
  customerList.value = await CustomerApi.getCustomerSimpleList()
})
</script>
