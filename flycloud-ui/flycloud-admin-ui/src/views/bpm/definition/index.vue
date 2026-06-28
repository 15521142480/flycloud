<template>
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.bpm.definition.index.k41fad255')"
        align="center"
        prop="id"
        width="400"
      />
      <el-table-column
        :label="t('auto.views.bpm.definition.index.k323a8305')"
        align="center"
        prop="name"
        width="200"
      >
        <template #default="scope">
          <el-button type="primary" link @click="handleBpmnDetail(scope.row)">
            <span>{{ scope.row.name }}</span>
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k92140340')"
        align="center"
        prop="categoryName"
        width="100"
      />
      <el-table-column :label="t('extra.k1a946e0f')" align="center" prop="formType" width="200">
        <template #default="scope">
          <el-button
            v-if="scope.row.formType === 10"
            type="primary"
            link
            @click="handleFormDetail(scope.row)"
          >
            <span>{{ scope.row.formName }}</span>
          </el-button>
          <el-button v-else type="primary" link @click="handleFormDetail(scope.row)">
            <span>{{ scope.row.formCustomCreatePath }}</span>
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.ke0542d83')"
        align="center"
        prop="processDefinition.version"
        width="80"
      >
        <template #default="scope">
          <el-tag v-if="scope.row">v{{ scope.row.version }}</el-tag>
          <el-tag type="warning" v-else>{{ t('extra.k647f2223') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('common.status')" align="center" prop="version" width="80">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.suspensionState === 1">{{
            t('extra.k83a991d7')
          }}</el-tag>
          <el-tag type="warning" v-if="scope.row.suspensionState === 2">{{
            t('extra.k65d1ff59')
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k66d588da')"
        align="center"
        prop="deploymentTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column
        :label="t('extra.k68622d8f')"
        align="center"
        prop="description"
        width="300"
        show-overflow-tooltip
      />
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 弹窗：表单详情 -->
  <Dialog :title="t('extra.k216a0b25')" v-model="formDetailVisible" width="800">
    <form-create :rule="formDetailPreview.rule" :option="formDetailPreview.option" />
  </Dialog>

  <!-- 弹窗：流程模型图的预览 -->
  <Dialog
    :title="t('auto.views.bpm.processInstance.create.ProcessDefinitionDetail.k3bffba42')"
    v-model="bpmnDetailVisible"
    width="800"
  >
    <MyProcessViewer style="height: 700px" key="designer" :xml="bpmnXml" />
  </Dialog>
</template>

<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import { MyProcessViewer } from '@/components/bpmnProcessDesigner/package'
import * as DefinitionApi from '@/api/bpm/definition'
import { setConfAndFields2 } from '@/utils/formCreate'
const { t } = useI18n()
defineOptions({ name: 'BpmProcessDefinition' })

const { push } = useRouter() // 路由
const { query } = useRoute() // 查询参数

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  key: query.key
})

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DefinitionApi.getProcessDefinitionPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 流程表单的详情按钮操作 */
const formDetailVisible = ref(false)
const formDetailPreview = ref({
  rule: [],
  option: {}
})
const handleFormDetail = async (row: any) => {
  if (row.formType == 10) {
    // 设置表单
    setConfAndFields2(formDetailPreview, row.formConf, row.formFields)
    // 弹窗打开
    formDetailVisible.value = true
  } else {
    await push({
      path: row.formCustomCreatePath
    })
  }
}

/** 流程图的详情按钮操作 */
const bpmnDetailVisible = ref(false)
const bpmnXml = ref('')
const handleBpmnDetail = async (row: any) => {
  // 设置可见
  bpmnXml.value = ''
  bpmnDetailVisible.value = true
  // 加载 BPMN XML
  bpmnXml.value = (await DefinitionApi.getProcessDefinition(row.id))?.bpmnXml
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
