<!-- 分配给我的客户 -->
<!-- WHERE followUpStatus = ? -->
<template>
  <ContentWrap>
    <div class="pb-5 text-xl">{{
      t('auto.views.crm.backlog.components.CustomerFollowList.kbd907d44')
    }}</div>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item :label="t('common.status')" prop="followUpStatus">
        <el-select
          v-model="queryParams.followUpStatus"
          class="!w-240px"
          :placeholder="t('common.status')"
          @change="handleQuery"
        >
          <el-option
            v-for="(option, index) in FOLLOWUP_STATUS"
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
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
        fixed="left"
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
        :label="t('auto.views.crm.clue.ClueForm.kb805cdaa')"
        prop="source"
        width="100"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_SOURCE" :value="scope.row.source" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.clue.ClueForm.k9c01ad09')"
        align="center"
        prop="mobile"
        width="120"
      />
      <el-table-column
        :label="t('system.dept.phone')"
        align="center"
        prop="telephone"
        width="130"
      />
      <el-table-column :label="t('system.user.email')" align="center" prop="email" width="180" />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.kbb7208b8')"
        prop="level"
        width="135"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="scope.row.level" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.k7b39ef2d')"
        prop="industryId"
        width="100"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_INDUSTRY" :value="scope.row.industryId" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')"
        prop="contactNextTime"
        width="180px"
      />
      <el-table-column align="center" :label="t('common.remark')" prop="remark" width="200" />
      <el-table-column align="center" :label="t('extra.kf72c2ad9')" prop="lockStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.lockStatus" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.crm.customer.detail.CustomerDetailsHeader.kf3fd0dd7')"
        prop="dealStatus"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.dealStatus" />
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
        :label="t('extra.k1afb06c4')"
        prop="contactLastContent"
        width="200"
      />
      <el-table-column
        :label="t('auto.views.crm.clue.ClueForm.k67d2d797')"
        align="center"
        prop="detailAddress"
        width="180"
      />
      <el-table-column align="center" :label="t('extra.k0b51579b')" prop="poolDay" width="140">
        <template #default="scope"> {{ scope.row.poolDay }} {{ t('extra.k0ed895b2') }}</template>
      </el-table-column>
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

<script setup lang="ts">
import * as CustomerApi from '@/api/crm/customer'
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { FOLLOWUP_STATUS } from './common'
const { t } = useI18n()
defineOptions({ name: 'CrmCustomerFollowList' })

const { push } = useRouter()

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  sceneType: 1,
  followUpStatus: false
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CustomerApi.getCustomerPage(queryParams.value)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

/** 打开客户详情 */
const openDetail = (id: string) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
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
