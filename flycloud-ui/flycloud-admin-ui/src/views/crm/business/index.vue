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
      <el-form-item :label="t('auto.views.crm.business.index.k84b59248')" prop="name">
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.crm.business.index.k258a412f')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k0b032ab0') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k93babe95') }}
        </el-button>
        <el-button v-hasPermi="['crm:business:create']" type="primary" @click="openForm('create')">
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.kfdc95d4f') }}
        </el-button>
        <el-button
          v-hasPermi="['crm:business:export']"
          :loading="exportLoading"
          plain
          type="success"
          @click="handleExport"
        >
          <Icon class="mr-5px" icon="ep:download" />
          {{ t('extra.k760d6856') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('auto.views.crm.business.index.k0bbf4db1')" name="1" />
      <el-tab-pane :label="t('auto.views.crm.business.index.k1640a0ff')" name="2" />
      <el-tab-pane :label="t('auto.views.crm.business.index.k72715f2e')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        fixed="left"
        :label="t('auto.views.crm.business.index.k84b59248')"
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
        fixed="left"
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
        :formatter="erpPriceTableColumnFormatter"
        align="center"
        :label="t('auto.views.crm.business.detail.BusinessDetailsHeader.k9ee5e0c1')"
        prop="totalPrice"
        width="140"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.crm.business.BusinessForm.k491e6c11')"
        prop="dealTime"
        width="180px"
      />
      <el-table-column align="center" :label="t('common.remark')" prop="remark" width="200" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')"
        prop="contactNextTime"
        width="180px"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.business.BusinessForm.k974d383f')"
        prop="ownerUserName"
        width="100px"
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
        :label="t('extra.kffa0750f')"
        prop="contactLastTime"
        width="180px"
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
        width="100px"
      />
      <el-table-column
        align="center"
        fixed="right"
        :label="t('auto.views.crm.business.BusinessForm.k9d0edbd1')"
        prop="statusTypeName"
        width="140"
      />
      <el-table-column
        align="center"
        fixed="right"
        :label="t('auto.views.crm.business.BusinessUpdateStatusForm.kc0a20f9a')"
        prop="statusName"
        width="120"
      />
      <el-table-column align="center" fixed="right" :label="t('common.operation')" width="130px">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:business:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:business:delete']"
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
  <BusinessForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as BusinessApi from '@/api/crm/business'
import BusinessForm from './BusinessForm.vue'
import { erpPriceTableColumnFormatter } from '@/utils'
import { TabsPaneContext } from 'element-plus'
const { t } = useI18n()
defineOptions({ name: 'CrmBusiness' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  sceneType: '1', // 默认和 activeName 相等
  name: null
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const activeName = ref('1') // 列表 tab

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await BusinessApi.getBusinessPage(queryParams)
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

/** tab 切换 */
const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.sceneType = tab.paneName
  handleQuery()
}

/** 打开客户详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmBusinessDetail', params: { id } })
}

/** 打开客户详情 */
const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
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
    await BusinessApi.deleteBusiness(id)
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
    const data = await BusinessApi.exportBusiness(queryParams)
    download.excel(data, t('auto.views.crm.business.index.k38858e81'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
