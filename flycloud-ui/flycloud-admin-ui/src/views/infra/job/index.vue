<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="100px"
    >
      <el-form-item :label="t('auto.views.infra.job.index.k2e304698')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.infra.job.index.k5169b9ec')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.index.kb7d4128d')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.infra.job.index.kf8aa9ccb')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_JOB_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.job.index.kec311980')" prop="handlerName">
        <el-input
          v-model="queryParams.handlerName"
          :placeholder="t('auto.views.infra.job.index.kec878692')"
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
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['infra:job:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k1e136e95') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['infra:job:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.kb3081d0c') }}
        </el-button>
        <el-button type="info" plain @click="handleJobLog()" v-hasPermi="['infra:job:query']">
          <Icon icon="ep:zoom-in" class="mr-5px" /> {{ t('extra.k9a8378ea') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.infra.job.index.k017af56c')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.infra.job.index.k2e304698')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.infra.job.index.kb7d4128d')"
        align="center"
        prop="status"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_JOB_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.infra.job.JobDetail.kec311980')"
        align="center"
        prop="handlerName"
      />
      <el-table-column
        :label="t('auto.views.infra.job.JobDetail.kddc1dd16')"
        align="center"
        prop="handlerParam"
      />
      <el-table-column
        :label="t('auto.views.infra.job.JobForm.kf6b43ffc')"
        align="center"
        prop="cronExpression"
      />
      <el-table-column :label="t('common.operation')" align="center" width="200">
        <template #default="scope">
          <el-button
            type="primary"
            link
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['infra:job:update']"
          >
            {{ t('extra.k4c512392') }}
          </el-button>
          <el-button
            type="primary"
            link
            @click="handleChangeStatus(scope.row)"
            v-hasPermi="['infra:job:update']"
          >
            {{ scope.row.status === InfraJobStatusEnum.STOP ? '开启' : '暂停' }}
          </el-button>
          <el-button
            type="danger"
            link
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['infra:job:delete']"
          >
            {{ t('common.delete') }}
          </el-button>
          <el-dropdown
            @command="(command) => handleCommand(command, scope.row)"
            v-hasPermi="['infra:job:trigger', 'infra:job:query']"
          >
            <el-button type="primary" link
              ><Icon icon="ep:d-arrow-right" /> {{ t('action.more') }}</el-button
            >
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="handleRun" v-if="checkPermi(['infra:job:trigger'])">
                  {{ t('extra.k1bc974f4') }}
                </el-dropdown-item>
                <el-dropdown-item command="openDetail" v-if="checkPermi(['infra:job:query'])">
                  {{ t('auto.views.infra.job.JobDetail.kcb1587a2') }}
                </el-dropdown-item>
                <el-dropdown-item command="handleJobLog" v-if="checkPermi(['infra:job:query'])">
                  {{ t('auto.router.modules.remaining.ka3756839') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
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

  <!-- 表单弹窗：添加/修改 -->
  <JobForm ref="formRef" @success="getList" />
  <!-- 表单弹窗：查看 -->
  <JobDetail ref="detailRef" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { checkPermi } from '@/utils/permission'
import JobForm from './JobForm.vue'
import JobDetail from './JobDetail.vue'
import download from '@/utils/download'
import * as JobApi from '@/api/infra/job'
import { InfraJobStatusEnum } from '@/utils/constants'
const { t } = useI18n()
defineOptions({ name: 'InfraJob' })
const message = useMessage() // 消息弹窗
const { push } = useRouter() // 路由

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  status: undefined,
  handlerName: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await JobApi.getJobPage(queryParams)
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

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await JobApi.exportJob(queryParams)
    download.excel(data, t('auto.views.infra.job.index.k9ecd6f11'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 修改状态操作 */
const handleChangeStatus = async (row: JobApi.JobVO) => {
  try {
    // 修改状态的二次确认
    const text =
      row.status === InfraJobStatusEnum.STOP
        ? t('auto.views.infra.job.index.k256783b7')
        : t('common.close')
    await message.confirm(
      t('auto.views.infra.job.index.k2be38185') +
        text +
        t('auto.views.infra.job.index.k1203af22') +
        row.id +
        t('auto.views.infra.job.index.kc87f4c07'),
      t('common.reminder')
    )
    const status =
      row.status === InfraJobStatusEnum.STOP ? InfraJobStatusEnum.NORMAL : InfraJobStatusEnum.STOP
    await JobApi.updateJobStatus(row.id, status)
    message.success(text + t('common.success'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await JobApi.deleteJob(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** '更多'操作按钮 */
const handleCommand = (command, row) => {
  switch (command) {
    case 'handleRun':
      handleRun(row)
      break
    case 'openDetail':
      openDetail(row.id)
      break
    case 'handleJobLog':
      handleJobLog(row?.id)
      break
    default:
      break
  }
}

/** 执行一次 */
const handleRun = async (row: JobApi.JobVO) => {
  try {
    // 二次确认
    await message.confirm(
      t('auto.views.infra.job.index.k8e35da48') + row.name + '?',
      t('common.reminder')
    )
    // 提交执行
    await JobApi.runJob(row.id)
    message.success(t('auto.views.infra.job.index.k6c189aad'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 查看操作 */
const detailRef = ref()
const openDetail = (id: number) => {
  detailRef.value.open(id)
}

/** 跳转执行日志 */
const handleJobLog = (id?: number) => {
  if (id && id > 0) {
    push('/job/job-log/' + id)
  } else {
    push('/job/job-log')
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
