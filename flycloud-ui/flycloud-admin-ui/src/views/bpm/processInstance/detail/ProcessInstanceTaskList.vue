<template>
  <el-table
    :data="recordRows"
    :span-method="spanMethod"
    border
    header-cell-class-name="table-header-gray"
  >
    <el-table-column
      :label="t('auto.views.bpm.processInstance.detail.ProcessInstanceTaskList.kdce0dc69')"
      prop="name"
      min-width="120"
      align="center"
    />
    <el-table-column
      :label="t('auto.views.bpm.processInstance.detail.ProcessInstanceTaskList.k9b446de3')"
      min-width="100"
      align="center"
    >
      <template #default="scope">
        {{ scope.row.assigneeUser?.name || scope.row.ownerUser?.name || '-' }}
      </template>
    </el-table-column>
    <el-table-column
      :formatter="dateFormatter"
      align="center"
      :label="t('common.startTimeText')"
      prop="createTime"
      min-width="140"
    />
    <el-table-column
      :formatter="dateFormatter"
      align="center"
      :label="t('common.endTimeText')"
      prop="endTime"
      min-width="140"
    />
    <el-table-column
      align="center"
      :label="t('auto.views.bpm.task.done.index.k93623725')"
      prop="status"
      min-width="150"
    >
      <template #default="scope">
        <span
          v-if="scope.row.recordType === 'comment'"
          class="inline-flex items-center whitespace-nowrap rounded px-8px py-2px text-13px"
          :class="getCommentActionClass(scope.row.commentType)"
        >
          {{ scope.row.action }}
        </span>
        <dict-tag v-else :type="DICT_TYPE.BPM_TASK_STATUS" :value="scope.row.status" />
      </template>
    </el-table-column>
    <el-table-column align="center" :label="t('extra.kd2a31c2e')" prop="reason" min-width="200">
      <template #default="scope">
        {{ scope.row.reason }}
        <el-button
          class="ml-10px"
          size="small"
          v-if="scope.row.recordType === 'task' && scope.row.formId > 0"
          @click="handleFormDetail(scope.row)"
        >
          <Icon icon="ep:document" /> {{ t('extra.k5f73e281') }}
        </el-button>
      </template>
    </el-table-column>
    <el-table-column
      align="center"
      :label="t('extra.k39f1374d')"
      prop="durationInMillis"
      min-width="100"
    >
      <template #default="scope">
        {{ scope.row.durationInMillis == null ? '-' : formatPast2(scope.row.durationInMillis) }}
      </template>
    </el-table-column>
  </el-table>

  <!-- 弹窗：表单 -->
  <Dialog :title="t('extra.k216a0b25')" v-model="taskFormVisible" width="600">
    <form-create
      ref="fApi"
      v-model="taskForm.value"
      :option="taskForm.option"
      :rule="taskForm.rule"
    />
  </Dialog>
</template>
<script lang="ts" setup>
// @ts-nocheck
import { dateFormatter, formatPast2 } from '@/utils/formatTime'
import { propTypes } from '@/utils/propTypes'
import { DICT_TYPE } from '@/utils/dict'
import type { ApiAttrs } from '@form-create/element-ui/types/config'
import { setConfAndFields2 } from '@/utils/formCreate'
import * as TaskApi from '@/api/bpm/task'
const { t } = useI18n()
defineOptions({ name: 'BpmProcessInstanceTaskList' })

const props = defineProps({
  loading: propTypes.bool.def(false), // 是否加载中
  id: propTypes.string // 流程实例的编号
})
const tasks = ref([]) // 流程任务的数组
const recordRows = computed(() => {
  const rows: any[] = []
  tasks.value.forEach((task: any) => {
    // 审批通过、拒绝、取消、退回已经由任务行展示，避免评论和任务重复。
    const operationComments = (task.comments || []).filter((comment: any) =>
      ['5', '6', '7', '8', '9'].includes(comment.type)
    )
    const groupRows = operationComments.map((comment: any) => ({
      ...task,
      id: `comment-${comment.id}`,
      recordType: 'comment',
      assigneeUser: comment.user,
      ownerUser: undefined,
      createTime: comment.createTime,
      endTime: undefined,
      status: undefined,
      reason: comment.message,
      durationInMillis: undefined,
      commentType: comment.type,
      action: getCommentAction(comment)
    }))
    groupRows.push({
      ...task,
      recordType: task.taskDefinitionKey === 'StartUserNode' ? 'start' : 'task'
    })
    groupRows.forEach((row: any, index: number) => {
      row.nodeRowSpan = index === 0 ? groupRows.length : 0
      rows.push(row)
    })
  })
  return rows
})

/** 将同一个工作项的操作记录和当前状态合并到一个审批节点下 */
const spanMethod = ({ row, columnIndex }: any) => {
  if (columnIndex !== 0) {
    return [1, 1]
  }
  return row.nodeRowSpan > 0 ? [row.nodeRowSpan, 1] : [0, 0]
}

const getCommentTarget = (message: string, pattern: RegExp) => message?.match(pattern)?.[1]

/** 生成类似“转交 → 张三”的审批动作，不展示用户编号 */
const getCommentAction = (comment: any) => {
  if (comment.type === '7') {
    const target = getCommentTarget(comment.message, /将任务转派给\[([^\]]+)]/)
    return target ? `转交 → ${target}` : '转交'
  }
  if (comment.type === '5') {
    const target = getCommentTarget(comment.message, /将任务委派给\[([^\]]+)]/)
    return target ? `委派 → ${target}` : '委派'
  }
  if (comment.type === '6') {
    const target = getCommentTarget(comment.message, /任务重新回到\[([^\]]+)]手中/)
    return target ? `委派完成 → ${target}` : '委派完成'
  }
  return comment.typeName || '流程操作'
}

const getCommentActionClass = (commentType: string) => {
  if (commentType === '7') {
    return 'bg-orange-50 text-orange-500'
  }
  if (commentType === '5' || commentType === '6') {
    return 'bg-blue-50 text-blue-500'
  }
  return 'bg-gray-100 text-gray-600'
}

/** 查看表单 */
const fApi = ref<ApiAttrs>() // form-create 的 API 操作类
const taskForm = ref({
  rule: [],
  option: {},
  value: {}
}) // 流程任务的表单详情
const taskFormVisible = ref(false)
const handleFormDetail = async (row: any) => {
  // 设置表单
  setConfAndFields2(taskForm, row.formConf, row.formFields, row.formVariables)
  // 弹窗打开
  taskFormVisible.value = true
  // 隐藏提交、重置按钮，设置禁用只读
  await nextTick()
  fApi.value.fapi.btn.show(false)
  fApi.value?.fapi?.resetBtn.show(false)
  fApi.value?.fapi?.disabled(true)
}

/** 只有 loading 完成时，才去加载流程列表 */
watch(
  () => props.loading,
  async (value) => {
    if (value) {
      tasks.value = await TaskApi.getTaskListByProcessInstanceId(props.id)
    }
  }
)
</script>
