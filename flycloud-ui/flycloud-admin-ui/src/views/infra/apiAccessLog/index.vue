<template>
  <doc-alert
    :title="t('auto.views.infra.apiAccessLog.index.k096733f9')"
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
      <el-form-item :label="t('auto.views.infra.apiAccessLog.index.kec750ef6')" prop="userId">
        <el-input
          v-model="queryParams.userId"
          :placeholder="t('auto.views.infra.apiAccessLog.index.kb719fb8a')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.apiAccessLog.index.k31ab92d1')" prop="userType">
        <el-select
          v-model="queryParams.userType"
          :placeholder="t('auto.views.infra.apiAccessLog.index.k8d91841e')"
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
        :label="t('auto.views.infra.apiAccessLog.index.k6c19fe01')"
        prop="applicationName"
      >
        <el-input
          v-model="queryParams.applicationName"
          :placeholder="t('auto.views.infra.apiAccessLog.index.k445d8859')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.apiAccessLog.index.ke8b5eda0')" prop="beginTime">
        <el-date-picker
          v-model="queryParams.beginTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.infra.apiAccessLog.index.k1f291968')"
          :end-placeholder="t('auto.views.infra.apiAccessLog.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.apiAccessLog.index.kf74f1dd2')" prop="duration">
        <el-input
          v-model="queryParams.duration"
          :placeholder="t('auto.views.infra.apiAccessLog.index.k3592b723')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.apiAccessLog.index.k52f2a6cf')" prop="resultCode">
        <el-input
          v-model="queryParams.resultCode"
          :placeholder="t('auto.views.infra.apiAccessLog.index.k3c85d6ac')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
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
          v-hasPermi="['infra:api-access-log:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k43c7c60a') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.index.k8cac83c8')"
        align="center"
        prop="id"
        width="100"
        fix="right"
      />
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.index.kec750ef6')"
        align="center"
        prop="userId"
      />
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.index.k31ab92d1')"
        align="center"
        prop="userType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="scope.row.userType" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k6c19fe01')"
        align="center"
        prop="applicationName"
        width="150"
      />
      <el-table-column
        :label="t('extra.kec49329b')"
        align="center"
        prop="requestMethod"
        width="80"
      />
      <el-table-column :label="t('extra.k251b56d4')" align="center" prop="requestUrl" width="500" />
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.ke8b5eda0')"
        align="center"
        prop="beginTime"
        width="180"
      >
        <template #default="scope">
          <span>{{ formatDate(scope.row.beginTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.index.kf74f1dd2')"
        align="center"
        prop="duration"
        width="180"
      >
        <template #default="scope"> {{ scope.row.duration }} ms </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k99d2f412')"
        align="center"
        prop="status"
      >
        <template #default="scope">
          {{ scope.row.resultCode === 0 ? '成功' : '失败(' + scope.row.resultMsg + ')' }}
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k301c995f')"
        align="center"
        prop="operateModule"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k6116e6e0')"
        align="center"
        prop="operateName"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.system.loginlog.LoginLogDetail.k19e41f1b')"
        align="center"
        prop="operateType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_OPERATE_TYPE" :value="scope.row.operateType" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.operation')" align="center" fixed="right" width="60">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row)"
            v-hasPermi="['infra:api-access-log:list']"
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

  <!-- 表单弹窗：详情 -->
  <ApiAccessLogDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import download from '@/utils/download'
import { formatDate } from '@/utils/formatTime'
import * as ApiAccessLogApi from '@/api/infra/apiAccessLog'
import ApiAccessLogDetail from './ApiAccessLogDetail.vue'
const { t } = useI18n()
defineOptions({ name: 'InfraApiAccessLog' })

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
  duration: null,
  resultCode: null,
  beginTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ApiAccessLogApi.getApiAccessLogPage(queryParams)
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
const openDetail = (data: ApiAccessLogApi.ApiAccessLogVO) => {
  detailRef.value.open(data)
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ApiAccessLogApi.exportApiAccessLog(queryParams)
    download.excel(data, t('auto.views.infra.apiAccessLog.index.kd74b5cad'))
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
