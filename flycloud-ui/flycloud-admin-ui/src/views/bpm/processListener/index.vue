<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="85px"
    >
      <el-form-item :label="t('auto.views.bpm.processListener.index.k364bd1bf')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.bpm.processListener.index.k010c1585')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.processListener.index.ke4e46c72')" prop="type">
        <el-select
          v-model="queryParams.type"
          :placeholder="t('auto.views.bpm.processListener.index.k97f47b78')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getStrDictOptions(DICT_TYPE.BPM_PROCESS_LISTENER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['bpm:manage:listener:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k6250ed32') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="list"
      :stripe="true"
      :show-overflow-tooltip="true"
      height="calc(100vh - 310px)"
    >
      <el-table-column
        :label="t('auto.views.bpm.processListener.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.bpm.processListener.index.k364bd1bf')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.bpm.processListener.index.ke4e46c72')"
        align="center"
        prop="type"
        width="110px"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BPM_PROCESS_LISTENER_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.status')" align="center" prop="status" width="100px">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.k550e3280')"
        align="center"
        prop="event"
        width="100px"
      />
      <el-table-column
        :label="t('auto.views.bpm.processListener.ProcessListenerForm.k5d6dd202')"
        align="center"
        prop="valueType"
        width="110px"
      >
        <template #default="scope">
          <dict-tag
            :type="DICT_TYPE.BPM_PROCESS_LISTENER_VALUE_TYPE"
            :value="scope.row.valueType"
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="
          t('auto.components.bpmnProcessDesigner.package.designer.plugins.translate.k321b9558')
        "
        align="center"
        prop="value"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['bpm:manage:listener:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['bpm:manage:listener:delete']"
          >
            {{ t('common.delete') }}
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

  <!-- 表单弹窗：添加/修改 -->
  <ProcessListenerForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getStrDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { ProcessListenerApi, ProcessListenerVO } from '@/api/bpm/processListener'
import ProcessListenerForm from './ProcessListenerForm.vue'

/** BPM 流程 列表 */
const { t } = useI18n()
defineOptions({ name: 'BpmProcessListener' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const list = ref<ProcessListenerVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  type: undefined,
  event: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProcessListenerApi.getProcessListenerPage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ProcessListenerApi.deleteProcessListener(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
