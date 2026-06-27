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
      <el-form-item :label="t('auto.views.bpm.oa.leave.index.k1509ea5a')" prop="type">
        <el-select
          v-model="queryParams.type"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.bpm.oa.leave.index.k770d5b5d')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BPM_OA_LEAVE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.oa.leave.index.ke85ad6ea')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          :end-placeholder="t('auto.views.bpm.oa.leave.index.kf4b9b2b5')"
          :start-placeholder="t('auto.views.bpm.oa.leave.index.k1f291968')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.oa.leave.index.k3fbd0491')" prop="status">
        <el-select
          v-model="queryParams.status"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.bpm.oa.leave.index.k4d553a63')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.oa.leave.index.k1ff9c3d0')" prop="reason">
        <el-input
          v-model="queryParams.reason"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.bpm.oa.leave.index.k6faa6ebc')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k530e3c18') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k6fdfb376') }}
        </el-button>
        <el-button
          v-hasPermi="['bpm:oa:leave:create']"
          plain
          type="primary"
          @click="handleCreate()"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.k5a0cca71') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.oa.leave.index.k4d2ae683')"
        prop="id"
      />
      <el-table-column align="center" :label="t('common.status')" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="开始时间"
        prop="startTime"
        width="180"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="结束时间"
        prop="endTime"
        width="180"
      />
      <el-table-column align="center" label="请假类型" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BPM_OA_LEAVE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="原因" prop="reason" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="申请时间"
        prop="createTime"
        width="180"
      />
      <el-table-column align="center" label="操作" width="200">
        <template #default="scope">
          <el-button
            v-hasPermi="['bpm:oa:leave:detail']"
            link
            type="primary"
            @click="handleDetail(scope.row)"
          >
            详情
          </el-button>
          <el-button
            v-hasPermi="['bpm:oa:leave:progress']"
            link
            type="primary"
            @click="handleProcessDetail(scope.row)"
          >
            进度
          </el-button>
          <el-button
            v-if="scope.row.result === 1"
            link
            type="danger"
            @click="cancelLeave(scope.row)"
          >
            取消
          </el-button>
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
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as LeaveApi from '@/api/bpm/leave'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
const { t } = useI18n()
defineOptions({ name: 'BpmOALeave' })

const message = useMessage() // 消息弹窗
const router = useRouter() // 路由

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  type: undefined,
  status: undefined,
  reason: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await LeaveApi.getLeavePage(queryParams)
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

/** 添加操作 */
const handleCreate = () => {
  router.push({ name: 'OALeaveCreate' })
}

/** 详情操作 */
const handleDetail = (row: LeaveApi.LeaveVO) => {
  router.push({
    name: 'OALeaveDetail',
    query: {
      id: row.id
    }
  })
}

/** 取消请假操作 */
const cancelLeave = async (row) => {
  // 二次确认
  const { value } = await ElMessageBox.prompt(
    t('auto.views.bpm.oa.leave.index.kd878c25a'),
    t('auto.views.bpm.oa.leave.index.kef368da8'),
    {
      confirmButtonText: t('common.ok'),
      cancelButtonText: t('common.cancel'),
      inputPattern: /^[\s\S]*.*\S[\s\S]*$/, // 判断非空，且非空格
      inputErrorMessage: t('auto.views.bpm.oa.leave.index.k6a6cf9af')
    }
  )
  // 发起取消
  await ProcessInstanceApi.cancelProcessInstanceByStartUser(row.id, value)
  message.success(t('auto.views.bpm.oa.leave.index.k24a1c71e'))
  // 刷新列表
  await getList()
}

/** 审批进度 */
const handleProcessDetail = (row) => {
  router.push({
    name: 'BpmProcessInstanceDetail',
    query: {
      id: row.processInstanceId
    }
  })
}

// fix: 列表不刷新的问题。
watch(
  () => router.currentRoute.value,
  () => {
    getList()
  }
)

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
