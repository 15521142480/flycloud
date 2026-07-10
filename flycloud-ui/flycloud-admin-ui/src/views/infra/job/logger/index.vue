<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="120px"
    >
      <el-form-item :label="t('auto.views.infra.job.logger.index.kec311980')" prop="handlerName">
        <el-input
          v-model="queryParams.handlerName"
          :placeholder="t('auto.views.infra.job.logger.index.kec878692')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.logger.index.k9052df91')" prop="beginTime">
        <el-date-picker
          v-model="queryParams.beginTime"
          type="date"
          value-format="YYYY-MM-DD HH:mm:ss"
          :placeholder="t('auto.views.infra.job.logger.index.k43112ae9')"
          clearable
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.logger.index.k2d4848eb')" prop="endTime">
        <el-date-picker
          v-model="queryParams.endTime"
          type="date"
          value-format="YYYY-MM-DD HH:mm:ss"
          :placeholder="t('auto.views.infra.job.logger.index.k656d9dce')"
          clearable
          :default-time="new Date('1 23:59:59')"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.logger.index.kb7d4128d')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.infra.job.logger.index.kf8aa9ccb')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_JOB_LOG_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"
          ><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button
        >
        <el-button @click="resetQuery"
          ><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button
        >
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['infra:job:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k85e7bca0') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.infra.job.logger.index.k8cac83c8')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.infra.job.logger.index.k017af56c')"
        align="center"
        prop="jobId"
      />
      <el-table-column
        :label="t('auto.views.infra.job.logger.index.kec311980')"
        align="center"
        prop="handlerName"
      />
      <el-table-column
        :label="t('auto.views.infra.job.logger.index.kddc1dd16')"
        align="center"
        prop="handlerParam"
      />
      <el-table-column
        :label="t('auto.views.infra.job.logger.index.k038b052f')"
        align="center"
        prop="executeIndex"
      />
      <el-table-column
        :label="t('auto.views.infra.job.logger.index.k68f17a6c')"
        align="center"
        width="170s"
      >
        <template #default="scope">
          <span>{{ formatDate(scope.row.beginTime) + ' ~ ' + formatDate(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.index.kf74f1dd2')"
        align="center"
        prop="duration"
      >
        <template #default="scope">
          <span>{{ scope.row.duration + ' 毫秒' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.infra.job.index.kb7d4128d')"
        align="center"
        prop="status"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_JOB_LOG_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            type="primary"
            link
            @click="openDetail(scope.row.id)"
            v-hasPermi="['infra:job:list']"
          >
            {{ t('extra.k1f0a3a1c') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：查看 -->
  <JobLogDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import download from '@/utils/download'
import JobLogDetail from './JobLogDetail.vue'
import * as JobLogApi from '@/api/infra/jobLog'
const { t } = useI18n()
defineOptions({ name: 'InfraJobLog' })

const message = useMessage() // 消息弹窗
const { query } = useRoute() // 查询参数

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  jobId: query.id,
  handlerName: undefined,
  beginTime: undefined,
  endTime: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await JobLogApi.getJobLogPage(queryParams)
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

/** 查看操作 */
const detailRef = ref()
const openDetail = (rowId?: string) => {
  detailRef.value.open(rowId)
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await JobLogApi.exportJobLog(queryParams)
    download.excel(data, t('auto.views.infra.job.logger.index.k15c526c1'))
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
