<template>
  <doc-alert
    :title="t('auto.views.infra.codegen.index.k5b195894')"
    url="https://doc.iocoder.cn/new-feature/"
  />
  <doc-alert
    :title="t('auto.views.infra.codegen.index.kbb58ac10')"
    url="https://doc.iocoder.cn/new-feature/tree/"
  />
  <doc-alert
    :title="t('auto.views.infra.codegen.index.k523a5d65')"
    url="https://doc.iocoder.cn/new-feature/master-sub/"
  />
  <doc-alert
    :title="t('auto.views.infra.codegen.index.kad6e2cd5')"
    url="https://doc.iocoder.cn/unit-test/"
  />

  <!-- 搜索 -->
  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :inline="true"
      :model="queryParams"
      class="-mb-15px"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.infra.codegen.index.k4b2d958f')" prop="tableName">
        <el-input
          v-model="queryParams.tableName"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.infra.codegen.index.k0abae2f6')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.codegen.index.kdef4ee41')" prop="tableComment">
        <el-input
          v-model="queryParams.tableComment"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.infra.codegen.index.kf161574b')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-240px"
          :end-placeholder="t('auto.views.infra.codegen.index.kf4b9b2b5')"
          :start-placeholder="t('auto.views.infra.codegen.index.k1f291968')"
          type="daterange"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.k349141c6') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.k2291d9b6') }}
        </el-button>
        <el-button v-hasPermi="['infra:codegen:create']" type="primary" @click="openImportTable()">
          <Icon class="mr-5px" icon="ep:zoom-in" />
          {{ t('extra.kc62b1222') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column align="center" :label="t('auto.views.infra.codegen.index.ka3ccf702')">
        <template #default="scope">
          {{
            dataSourceConfigList.find((config) => config.id === scope.row.dataSourceConfigId)?.name
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('auto.views.infra.codegen.ImportTable.k4b2d958f')"
        prop="tableName"
        width="200"
      />
      <el-table-column
        :show-overflow-tooltip="true"
        align="center"
        :label="t('auto.views.infra.codegen.ImportTable.kdef4ee41')"
        prop="tableComment"
        width="200"
      />
      <el-table-column align="center" :label="t('extra.k808e5669')" prop="className" width="200" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        width="180"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.updateTime')"
        prop="createTime"
        width="180"
      />
      <el-table-column align="center" fixed="right" :label="t('common.operation')" width="300px">
        <template #default="scope">
          <el-button
            v-hasPermi="['infra:codegen:preview']"
            link
            type="primary"
            @click="handlePreview(scope.row)"
          >
            {{ t('action.preview') }}
          </el-button>
          <el-button
            v-hasPermi="['infra:codegen:update']"
            link
            type="primary"
            @click="handleUpdate(scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['infra:codegen:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
          <el-button
            v-hasPermi="['infra:codegen:update']"
            link
            type="primary"
            @click="handleSyncDB(scope.row)"
          >
            {{ t('action.sync') }}
          </el-button>
          <el-button
            v-hasPermi="['infra:codegen:download']"
            link
            type="primary"
            @click="handleGenTable(scope.row)"
          >
            {{ t('extra.kf09f59b6') }}
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

  <!-- 弹窗：导入表 -->
  <ImportTable ref="importRef" @success="getList" />
  <!-- 弹窗：预览代码 -->
  <PreviewCode ref="previewRef" />
</template>
<script lang="ts" setup>
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as CodegenApi from '@/api/infra/codegen'
import * as DataSourceConfigApi from '@/api/infra/dataSourceConfig'
import ImportTable from './ImportTable.vue'
import PreviewCode from './PreviewCode.vue'
const { t } = useI18n()
defineOptions({ name: 'InfraCodegen' })

const message = useMessage() // 消息弹窗
const { push } = useRouter() // 路由跳转

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  tableName: undefined,
  tableComment: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const dataSourceConfigList = ref<DataSourceConfigApi.DataSourceConfigVO[]>([]) // 数据源列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CodegenApi.getCodegenTablePage(queryParams)
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

/** 导入操作 */
const importRef = ref()
const openImportTable = () => {
  importRef.value.open()
}

/** 编辑操作 */
const handleUpdate = (id: number) => {
  push('/codegen/edit/' + id)
}

/** 预览操作 */
const previewRef = ref()
const handlePreview = (row: CodegenApi.CodegenTableVO) => {
  previewRef.value.open(row.id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await CodegenApi.deleteCodegenTable(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 同步操作  */
const handleSyncDB = async (row: CodegenApi.CodegenTableVO) => {
  // 基于 DB 同步
  const tableName = row.tableName
  try {
    await message.confirm(
      t('auto.views.infra.codegen.index.k9d671367') +
        tableName +
        t('auto.views.infra.codegen.index.k63436e11'),
      t('common.reminder')
    )
    await CodegenApi.syncCodegenFromDB(row.id)
    message.success(t('auto.views.infra.codegen.index.k857139f1'))
  } catch {}
}

/** 生成代码操作 */
const handleGenTable = async (row: CodegenApi.CodegenTableVO) => {
  const res = await CodegenApi.downloadCodegen(row.id)
  download.zip(res, 'codegen-' + row.className + '.zip')
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载数据源列表
  dataSourceConfigList.value = await DataSourceConfigApi.getDataSourceConfigList()
})
</script>
