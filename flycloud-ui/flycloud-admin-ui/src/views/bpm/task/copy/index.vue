<!-- 工作流 - 抄送我的流程 -->
<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form ref="queryFormRef" :inline="true" class="-mb-15px" label-width="68px">
      <el-form-item :label="t('auto.views.bpm.task.copy.index.k323a8305')" prop="name">
        <el-input
          v-model="queryParams.processInstanceName"
          @keyup.enter="handleQuery"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.bpm.task.copy.index.ke9399a13')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.task.copy.index.ke3b33874')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          :end-placeholder="t('auto.views.bpm.task.copy.index.kf4b9b2b5')"
          :start-placeholder="t('auto.views.bpm.task.copy.index.k1f291968')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k6597160d') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.ke25599a6') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.copy.index.kf766a7e3')"
        prop="processInstanceName"
        min-width="180"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.copy.index.k340010f7')"
        prop="startUser.name"
        min-width="100"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.bpm.task.copy.index.k21ac3727')"
        prop="processInstanceStartTime"
        width="180"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.copy.index.k7afea2ce')"
        prop="activityName"
        min-width="180"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.copy.index.kdb9f866c')"
        min-width="100"
      >
        <template #default="scope"> {{ scope.row.createUser?.name || '系统' }} </template>
      </el-table-column>
      <el-table-column align="center" :label="t('extra.k51cbb39c')" prop="reason" width="150" />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.copy.index.ke3b33874')"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column align="center" :label="t('common.operation')" fixed="right" width="80">
        <template #default="scope">
          <el-button
            v-hasPermi="['bpm:audit:copy:detail']"
            link
            type="primary"
            @click="handleAudit(scope.row)"
            >{{ t('action.detail') }}</el-button
          >
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
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
const { t } = useI18n()
defineOptions({ name: 'BpmProcessInstanceCopy' })

const { push } = useRouter() // 路由

const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  processInstanceId: '',
  processInstanceName: '',
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询任务列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProcessInstanceApi.getProcessInstanceCopyPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 处理审批按钮 */
const handleAudit = (row: any) => {
  const query = {
    id: row.processInstanceId,
    activityId: undefined
  }
  if (row.activityId) {
    query.activityId = row.activityId
  }
  push({
    name: 'BpmProcessInstanceDetail',
    query: query
  })
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

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
