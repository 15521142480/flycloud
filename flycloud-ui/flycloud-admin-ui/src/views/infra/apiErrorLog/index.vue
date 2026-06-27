<template>
  <doc-alert
    :title="t('auto.views.infra.apiErrorLog.index.k096733f9')"
    url="https://doc.iocoder.cn/system-log/"
  />

  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.infra.apiErrorLog.index.kec750ef6')" prop="userId">
        <el-input
          v-model="queryParams.userId"
          :placeholder="t('auto.views.infra.apiErrorLog.index.kb719fb8a')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.apiErrorLog.index.k31ab92d1')" prop="userType">
        <el-select
          v-model="queryParams.userType"
          :placeholder="t('auto.views.infra.apiErrorLog.index.k8d91841e')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.USER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.infra.apiErrorLog.index.k6c19fe01')"
        prop="applicationName"
      >
        <el-input
          v-model="queryParams.applicationName"
          :placeholder="t('auto.views.infra.apiErrorLog.index.k445d8859')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.apiErrorLog.index.kdf79ec03')" prop="exceptionTime">
        <el-date-picker
          v-model="queryParams.exceptionTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.infra.apiErrorLog.index.k1f291968')"
          :end-placeholder="t('auto.views.infra.apiErrorLog.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.apiErrorLog.index.k8542beb9')" prop="processStatus">
        <el-select
          v-model="queryParams.processStatus"
          :placeholder="t('auto.views.infra.apiErrorLog.index.k8cda1014')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_API_ERROR_LOG_PROCESS_STATUS)"
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
          v-hasPermi="['infra:api-error-log:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k33e922a5') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.infra.apiErrorLog.index.k8cac83c8')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.infra.apiErrorLog.index.kec750ef6')"
        align="center"
        prop="userId"
      />
      <el-table-column
        :label="t('auto.views.infra.apiErrorLog.index.k31ab92d1')"
        align="center"
        prop="userType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column label="应用名" align="center" prop="applicationName" width="200" />
      <el-table-column label="请求方法" align="center" prop="requestMethod" width="80" />
      <el-table-column label="请求地址" align="center" prop="requestUrl" width="180" />
      <el-table-column
        label="异常发生时间"
        align="center"
        prop="exceptionTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="异常名" align="center" prop="exceptionName" width="180" />
      <el-table-column label="处理状态" align="center" prop="processStatus">
        <template #default="scope">
          <dict-tag
            :type="DICT_TYPE.INFRA_API_ERROR_LOG_PROCESS_STATUS"
            :value="scope.row.processStatus"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row)"
            v-hasPermi="['infra:api-error-log:query']"
          >
            详细
          </el-button>
          <el-button
            link
            type="primary"
            v-if="scope.row.processStatus === InfraApiErrorLogProcessStatusEnum.INIT"
            @click="handleProcess(scope.row.id, InfraApiErrorLogProcessStatusEnum.DONE)"
            v-hasPermi="['infra:api-error-log:update-status']"
          >
            已处理
          </el-button>
          <el-button
            link
            type="primary"
            v-if="scope.row.processStatus === InfraApiErrorLogProcessStatusEnum.INIT"
            @click="handleProcess(scope.row.id, InfraApiErrorLogProcessStatusEnum.IGNORE)"
            v-hasPermi="['infra:api-error-log:update-status']"
          >
            已忽略
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

  <!-- 表单弹窗：详情 -->
  <ApiErrorLogDetail ref="detailRef" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ApiErrorLogApi from '@/api/infra/apiErrorLog'
import ApiErrorLogDetail from './ApiErrorLogDetail.vue'
import { InfraApiErrorLogProcessStatusEnum } from '@/utils/constants'
const { t } = useI18n()
defineOptions({ name: 'InfraApiErrorLog' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: null,
  userType: null,
  applicationName: null,
  requestUrl: null,
  processStatus: null,
  exceptionTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ApiErrorLogApi.getApiErrorLogPage(queryParams)
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

/** 详情操作 */
const detailRef = ref()
const openDetail = (data: ApiErrorLogApi.ApiErrorLogVO) => {
  detailRef.value.open(data)
}

/** 处理已处理 / 已忽略的操作 **/
const handleProcess = async (id: number, processStatus: number) => {
  try {
    // 操作的二次确认
    const type =
      processStatus === InfraApiErrorLogProcessStatusEnum.DONE
        ? t('auto.views.infra.apiErrorLog.index.k219bde30')
        : t('auto.views.infra.apiErrorLog.index.kbd172c8d')
    await message.confirm(t('auto.views.infra.apiErrorLog.index.kdf80698f') + type + '?')
    // 执行操作
    await ApiErrorLogApi.updateApiErrorLogPage(id, processStatus)
    await message.success(type)
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
    const data = await ApiErrorLogApi.exportApiErrorLog(queryParams)
    download.excel(data, t('auto.views.infra.apiErrorLog.index.k7a9c0e67'))
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
