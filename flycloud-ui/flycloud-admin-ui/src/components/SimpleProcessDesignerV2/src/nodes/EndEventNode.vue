<template>
  <div class="end-node-wrapper">
    <div
      class="end-node-box cursor-pointer"
      :class="`${useTaskStatusClass(currentNode?.activityStatus)}`"
      @click="nodeClick"
    >
      <span
        class="node-fixed-name"
        :title="t('auto.components.SimpleProcessDesignerV2.src.nodes.EndEventNode.k76b98808')"
        >{{ t('auto.components.SimpleProcessDesignerV2.src.nodes.EndEventNode.k76b98808') }}</span
      >
    </div>
  </div>
  <el-dialog
    :title="t('auto.components.SimpleProcessDesignerV2.src.nodes.EndEventNode.kd7fd2db8')"
    v-model="dialogVisible"
    width="1000px"
    append-to-body
  >
    <el-row>
      <el-table
        :data="processInstanceInfos"
        size="small"
        border
        header-cell-class-name="table-header-gray"
      >
        <el-table-column
          :label="t('common.index')"
          header-align="center"
          align="center"
          type="index"
          width="50"
        />
        <el-table-column
          :label="t('auto.components.SimpleProcessDesignerV2.src.nodes.EndEventNode.k89d282d2')"
          prop="assigneeUser.name"
          min-width="100"
          align="center"
        />
        <el-table-column
          :label="t('auto.components.SimpleProcessDesignerV2.src.nodes.EndEventNode.k91061a56')"
          min-width="100"
          align="center"
        >
          <template #default="scope">
            {{ scope.row.assigneeUser?.deptName || scope.row.ownerUser?.deptName }}
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
          min-width="90"
        >
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS" :value="scope.row.status" />
          </template>
        </el-table-column>

        <el-table-column
          align="center"
          :label="t('extra.k39f1374d')"
          prop="durationInMillis"
          width="100"
        >
          <template #default="scope">
            {{ formatPast2(scope.row.durationInMillis) }}
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </el-dialog>
</template>
<script setup lang="ts">
// @ts-nocheck
import { SimpleFlowNode } from '../consts'
import { useWatchNode, useTaskStatusClass } from '../node'
import { dateFormatter, formatPast2 } from '@/utils/formatTime'
import { DICT_TYPE } from '@/utils/dict'
const { t } = useI18n()
defineOptions({
  name: 'EndEventNode'
})
const props = defineProps({
  flowNode: {
    type: Object as () => SimpleFlowNode,
    default: () => null
  }
})
// 监控节点变化
const currentNode = useWatchNode(props)
// 是否只读
const readonly = inject<Boolean>('readonly')
const processInstance = inject<Ref<any>>('processInstance')
// 审批信息的弹窗显示，用于只读模式
const dialogVisible = ref(false) // 弹窗可见性
const processInstanceInfos = ref<any[]>([]) // 流程的审批信息

const nodeClick = () => {
  if (readonly) {
    if (processInstance && processInstance.value) {
      processInstanceInfos.value = [
        {
          assigneeUser: processInstance.value.startUser,
          createTime: processInstance.value.startTime,
          endTime: processInstance.value.endTime,
          status: processInstance.value.status,
          durationInMillis: processInstance.value.durationInMillis
        }
      ]
      dialogVisible.value = true
    }
  }
}
</script>
<style lang="scss" scoped></style>
