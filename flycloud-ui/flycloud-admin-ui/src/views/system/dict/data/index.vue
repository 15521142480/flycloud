<template>
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('auto.views.system.dict.data.index.k32bc8312')" prop="dictType">
        <el-select v-model="queryParams.dictType" class="!w-240px">
          <el-option
            v-for="item in dictTypeList"
            :key="item.type"
            :label="item.name"
            :value="item.type"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.dict.data.index.k46be6f46')" prop="label">
        <el-input
          v-model="queryParams.label"
          :placeholder="t('auto.views.system.dict.data.index.kea755769')"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="queryParams.status"
          :placeholder="t('auto.views.system.dict.data.index.kf024b444')"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
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
          v-hasPermi="['system:dict:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.ke646be4d') }}
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['system:dict:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> {{ t('extra.ke7248b81') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" height="calc(100vh - 355px)">
      <el-table-column
        :label="t('auto.views.system.dict.data.index.k4444a417')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.system.dict.data.index.k46be6f46')"
        align="center"
        prop="label"
      />
      <el-table-column
        :label="t('auto.views.system.dict.data.index.k1d72882e')"
        align="center"
        prop="value"
      />
      <el-table-column
        :label="t('auto.views.system.dict.data.index.kc55da3d7')"
        align="center"
        prop="sort"
      />
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="颜色类型" align="center" prop="colorType" />
      <el-table-column label="CSS Class" align="center" prop="cssClass" />
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
        :formatter="dateFormatter"
      />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['system:dict:update']"
          >
            修改
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['system:dict:delete']"
          >
            删除
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
  <DictDataForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as DictDataApi from '@/api/system/dict/dict.data'
import * as DictTypeApi from '@/api/system/dict/dict.type'
import DictDataForm from './DictDataForm.vue'
const { t } = useI18n()
defineOptions({ name: 'SystemDictData' })

const message = useMessage() // 消息弹窗
const route = useRoute() // 路由

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  label: '',
  status: undefined,
  dictType: route.params.dictType
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const dictTypeList = ref<DictTypeApi.DictTypeVO[]>() // 字典类型的列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await DictDataApi.getDictDataPage(queryParams)
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
  formRef.value.open(type, id, queryParams.dictType)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await DictDataApi.deleteDictData(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await DictDataApi.exportDictData(queryParams)
    download.excel(data, t('auto.views.system.dict.data.index.k9c4311c1'))
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 查询字典（精简)列表
  dictTypeList.value = await DictTypeApi.getSimpleDictTypeList()
})
</script>
