<!-- 表达式选择 -->
<template>
  <Dialog
    :title="t('auto.components.bpmnProcessDesigner.package.penal.task.task_components.k0a6b5566')"
    v-model="dialogVisible"
    width="1024px"
  >
    <ContentWrap>
      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
        <el-table-column
          :label="
            t('auto.components.bpmnProcessDesigner.package.penal.task.task_components.k364bd1bf')
          "
          align="center"
          prop="name"
        />
        <el-table-column
          :label="
            t('auto.components.bpmnProcessDesigner.package.penal.task.task_components.k513c1c63')
          "
          align="center"
          prop="expression"
        />
        <el-table-column :label="t('common.operation')" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="select(scope.row)">
              {{
                t(
                  'auto.components.bpmnProcessDesigner.package.penal.task.task_components.k70b20820'
                )
              }}
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
  </Dialog>
</template>
<script setup lang="ts">
import { CommonStatusEnum } from '@/utils/constants'
import { ProcessExpressionApi, ProcessExpressionVO } from '@/api/bpm/processExpression'
/** BPM 流程 表单 */
const { t } = useI18n()
defineOptions({ name: 'ProcessExpressionDialog' })

const dialogVisible = ref(false) // 弹窗的是否展示
const loading = ref(true) // 列表的加载中
const list = ref<ProcessExpressionVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  type: '',
  status: CommonStatusEnum.ENABLE
})

/** 打开弹窗 */
const open = (type: string) => {
  queryParams.pageNum = 1
  queryParams.type = type
  getList()
  dialogVisible.value = true
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProcessExpressionApi.getProcessExpressionPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const select = async (row) => {
  dialogVisible.value = false
  // 发送操作成功的事件
  emit('select', row)
}
</script>
