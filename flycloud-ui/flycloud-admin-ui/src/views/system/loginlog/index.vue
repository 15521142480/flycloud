<template>
  <doc-alert
    :title="t('auto.views.system.loginlog.index.k096733f9')"
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
      <el-form-item :label="t('auto.views.system.loginlog.index.ka311ed74')" prop="username">
        <el-input
          v-model="queryParams.username"
          :placeholder="t('auto.views.system.loginlog.index.k15762992')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.loginlog.index.k84dd1c57')" prop="userIp">
        <el-input
          v-model="queryParams.userIp"
          :placeholder="t('auto.views.system.loginlog.index.kf21e6957')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.loginlog.index.k3e890a22')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.system.loginlog.index.k1f291968')"
          :end-placeholder="t('auto.views.system.loginlog.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
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
          v-hasPermi="['infra:login-log:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.k63b2a94e') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.system.loginlog.index.k8cac83c8')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.system.loginlog.index.k19e41f1b')"
        align="center"
        prop="logType"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_LOGIN_TYPE" :value="scope.row.logType" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('profile.user.username')"
        align="center"
        prop="username"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.system.loginlog.LoginLogDetail.k84dd1c57')"
        align="center"
        prop="userIp"
        width="180"
      />
      <el-table-column
        :label="t('auto.views.system.loginlog.LoginLogDetail.k88d650dd')"
        align="center"
        prop="userAgent"
      />
      <el-table-column
        :label="t('auto.views.system.loginlog.LoginLogDetail.k5ba4da17')"
        align="center"
        prop="result"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_LOGIN_RESULT" :value="scope.row.result" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.system.loginlog.LoginLogDetail.k3e890a22')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openDetail(scope.row)"
            v-hasPermi="['infra:login-log:query']"
          >
            {{ t('action.detail') }}
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
  <LoginLogDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as LoginLogApi from '@/api/system/loginLog'
import LoginLogDetail from './LoginLogDetail.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemLoginLog' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  username: undefined,
  userIp: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await LoginLogApi.getLoginLogPage(queryParams)
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
const openDetail = (data: LoginLogApi.LoginLogVO) => {
  detailRef.value.open(data)
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await LoginLogApi.exportLoginLog(queryParams)
    download.excel(data, t('auto.views.system.loginlog.index.kae3381c6'))
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
