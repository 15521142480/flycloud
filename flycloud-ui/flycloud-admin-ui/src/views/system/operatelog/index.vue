<template>
  <doc-alert
    :title="t('auto.views.system.operatelog.index.k096733f9')"
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
      <el-form-item :label="t('auto.views.system.operatelog.index.k06858dfb')" prop="userId">
        <el-select
          v-model="queryParams.userId"
          clearable
          filterable
          :placeholder="t('auto.views.system.operatelog.index.kdc7915c4')"
          class="!w-240px"
        >
          <el-option v-for="user in userList" :key="user.id" :label="user.name" :value="user.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.operatelog.index.k301c995f')" prop="type">
        <el-input
          v-model="queryParams.type"
          :placeholder="t('auto.views.system.operatelog.index.k36de5bc7')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.operatelog.index.k6116e6e0')" prop="subType">
        <el-input
          v-model="queryParams.subType"
          :placeholder="t('auto.views.system.operatelog.index.k7fc0171e')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.operatelog.index.k33089c48')" prop="action">
        <el-input
          v-model="queryParams.action"
          :placeholder="t('auto.views.system.operatelog.index.k7fc0171e')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.operatelog.index.kc7fc0c4d')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.system.operatelog.index.k1f291968')"
          :end-placeholder="t('auto.views.system.operatelog.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.operatelog.index.k9c5f5763')" prop="bizId">
        <el-input
          v-model="queryParams.bizId"
          :placeholder="t('auto.views.system.operatelog.index.k6a439c83')"
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
          v-hasPermi="['infra:operate-log:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.kd1f35d89') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.operatelog.index.k8cac83c8')"
        align="center"
        prop="id"
        width="100"
      />
      <el-table-column
        :label="t('auto.views.system.operatelog.index.k06858dfb')"
        align="center"
        prop="userName"
        width="120"
      />
      <el-table-column
        :label="t('auto.views.system.operatelog.index.k301c995f')"
        align="center"
        prop="type"
        width="120"
      />
      <el-table-column
        :label="t('auto.views.system.operatelog.index.k6116e6e0')"
        align="center"
        prop="subType"
        width="160"
      />
      <el-table-column
        :label="t('auto.views.system.operatelog.index.k33089c48')"
        align="center"
        prop="action"
      />
      <el-table-column
        :label="t('auto.views.system.operatelog.index.kc7fc0c4d')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('auto.views.system.operatelog.index.k9c5f5763')"
        align="center"
        prop="bizId"
        width="120"
      />
      <el-table-column
        :label="t('auto.views.system.operatelog.index.ke95a2168')"
        align="center"
        prop="userIp"
        width="120"
      />
      <el-table-column :label="t('common.operation')" align="center" fixed="right" width="60">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row)"
            v-hasPermi="['infra:operate-log:query']"
          >
            {{ t('extra.kea731f93') }}
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

  <!-- 表单弹窗：详情 -->
  <OperateLogDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as OperateLogApi from '@/api/system/operatelog'
import OperateLogDetail from './OperateLogDetail.vue'
import * as UserApi from '@/api/system/user'
const { t } = useI18n()
const userList = ref<UserApi.UserVO[]>([]) // 用户列表

defineOptions({ name: 'SystemOperateLog' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: undefined,
  type: undefined,
  subType: undefined,
  action: undefined,
  createTime: [],
  bizId: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await OperateLogApi.getOperateLogPage(queryParams)
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
const openDetail = (data: OperateLogApi.OperateLogVO) => {
  detailRef.value.open(data)
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await OperateLogApi.exportOperateLog(queryParams)
    download.excel(data, t('auto.views.system.operatelog.index.k2f3447ea'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 获得用户列表
  userList.value = await UserApi.getSimpleUserList()
})
</script>
