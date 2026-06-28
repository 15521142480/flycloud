<template>
  <ContentWrap>
    <div class="pb-5 text-xl">{{
      t('auto.views.crm.backlog.components.ClueFollowList.k330a38c5')
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
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.crm.backlog.components.ClueFollowList.k733eabce')"
        align="center"
        prop="name"
        fixed="left"
        width="160"
      >
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.crm.clue.detail.ClueDetailsHeader.k673239bf')"
        align="center"
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
        :label="t('auto.views.crm.clue.ClueForm.k67d2d797')"
        align="center"
        prop="detailAddress"
        width="180"
      />
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
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')"
        prop="contactNextTime"
        width="180px"
      />
      <el-table-column align="center" :label="t('common.remark')" prop="remark" width="200" />
      <el-table-column
        :label="t('extra.kffa0750f')"
        align="center"
        prop="contactLastTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        align="center"
        :label="t('extra.k1afb06c4')"
        prop="contactLastContent"
        width="200"
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
        width="100"
      />
      <el-table-column
        :label="t('common.updateTime')"
        align="center"
        prop="updateTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
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
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
</template>
<script setup lang="ts">
import * as ClueApi from '@/api/crm/clue'
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { FOLLOWUP_STATUS } from './common'
const { t } = useI18n()
defineOptions({ name: 'CrmClueFollowList' })

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  followUpStatus: false,
  transformStatus: false
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ClueApi.getCluePage(queryParams)
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

/** 打开线索详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmClueDetail', params: { id } })
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
