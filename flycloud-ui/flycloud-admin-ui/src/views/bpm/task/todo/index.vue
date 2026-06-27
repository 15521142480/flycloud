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
      <el-form-item :label="t('auto.views.bpm.task.todo.index.k2e304698')" prop="name">
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.bpm.task.todo.index.k5169b9ec')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          :end-placeholder="t('auto.views.bpm.task.todo.index.kf4b9b2b5')"
          :start-placeholder="t('auto.views.bpm.task.todo.index.k1f291968')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k9d955c5e') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.kfadbed1c') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.todo.index.k09d316b9')"
        prop="processInstance.name"
        width="180"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.todo.index.k89d282d2')"
        prop="processInstance.startUser.name"
        width="100"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.bpm.task.todo.index.k44042ce0')"
        prop="createTime"
        width="180"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.todo.index.ke94d4252')"
        prop="name"
        width="180"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('auto.views.bpm.task.todo.index.k425b5d5c')"
        prop="createTime"
        width="180"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.todo.index.ka11dfb55')"
        prop="id"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.task.todo.index.k017af56c')"
        prop="id"
        :show-overflow-tooltip="true"
      />
      <el-table-column align="center" :label="t('common.operation')" fixed="right" width="80">
        <template #default="scope">
          <el-button
            v-hasPermi="['bpm:audit:todo:doit']"
            link
            type="primary"
            @click="handleAudit(scope.row)"
            >{{ t('auto.views.bpm.task.todo.index.k5159733b') }}</el-button
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
import * as TaskApi from '@/api/bpm/task'
const { t } = useI18n()
defineOptions({ name: 'BpmTodoTask' })

const { push } = useRouter() // 路由

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询任务列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await TaskApi.getTaskTodoPage(queryParams)
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

/** 处理审批按钮 */
const handleAudit = (row: any) => {
  push({
    name: 'BpmProcessInstanceDetail',
    query: {
      id: row.processInstance.id,
      taskId: row.id
    }
  })
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
