<template>
  <doc-alert
    :title="t('auto.views.infra.fileConfig.index.ka534f64e')"
    url="https://doc.iocoder.cn/file/"
  />

  <!-- 搜索 -->
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.infra.fileConfig.index.kf0020751')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="t('auto.views.infra.fileConfig.index.k0b5e6a5c')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.infra.fileConfig.index.k620f816b')" prop="storage">
        <el-select
          v-model="queryParams.storage"
          :placeholder="t('auto.views.infra.fileConfig.index.k92c2f199')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.INFRA_FILE_STORAGE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('common.createTime')" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          :start-placeholder="t('auto.views.infra.fileConfig.index.k1f291968')"
          :end-placeholder="t('auto.views.infra.fileConfig.index.kf4b9b2b5')"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
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
          v-hasPermi="['infra:file-config:saveOrUpdate']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.k1c7d3e6e') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        :label="t('auto.views.infra.fileConfig.index.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.infra.fileConfig.index.kf0020751')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.infra.fileConfig.index.k620f816b')"
        align="center"
        prop="storage"
      >
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_FILE_STORAGE" :value="scope.row.storage" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.remark')" align="center" prop="remark" />
      <el-table-column :label="t('extra.k5fa0fcd9')" align="center" prop="primary">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.master" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column :label="t('common.operation')" align="center" width="240px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['infra:file-config:saveOrUpdate']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            link
            type="primary"
            :disabled="scope.row.master"
            @click="handleMaster(scope.row.id)"
            v-hasPermi="['infra:file-config:saveOrUpdate']"
          >
            {{ t('extra.k5fa0fcd9') }}
          </el-button>
          <el-button link type="primary" @click="handleTest(scope.row.id)">
            {{ t('action.test') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['infra:file-config:delete']"
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
  <FileConfigForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import * as FileConfigApi from '@/api/infra/fileConfig'
import FileConfigForm from './FileConfigForm.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'InfraFileConfig' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: undefined,
  storage: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await FileConfigApi.getFileConfigPage(queryParams)
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
    await FileConfigApi.deleteFileConfig(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 主配置按钮操作 */
const handleMaster = async (id) => {
  try {
    await message.confirm(
      t('auto.views.infra.fileConfig.index.keec2f1d8') +
        id +
        t('auto.views.infra.fileConfig.index.kd23be7ae')
    )
    await FileConfigApi.updateFileConfigMaster(id)
    message.success(t('common.updateSuccess'))
    await getList()
  } catch {}
}

/** 测试按钮操作 */
const handleTest = async (id) => {
  try {
    const response = await FileConfigApi.testFileConfig(id)
    await message.confirm(
      t('auto.views.infra.fileConfig.index.kab1997c0'),
      t('auto.views.infra.fileConfig.index.k64b28e69')
    )
    window.open(response, '_blank')
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
