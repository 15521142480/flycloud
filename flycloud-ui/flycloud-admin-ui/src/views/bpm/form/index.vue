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
      <el-form-item :label="t('auto.views.bpm.form.index.ke3f3073e')" prop="name">
        <el-input
          v-model="queryParams.name"
          class="!w-240px"
          clearable
          :placeholder="t('auto.views.bpm.form.index.k80f67d59')"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon class="mr-5px" icon="ep:search" />
          {{ t('extra.ka8ec6730') }}
        </el-button>
        <el-button @click="resetQuery">
          <Icon class="mr-5px" icon="ep:refresh" />
          {{ t('extra.kb339012c') }}
        </el-button>
        <el-button
          v-hasPermi="['bpm:manage:form:saveOrUpdate']"
          plain
          type="primary"
          @click="openForm"
        >
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('extra.k9ad548c3') }}
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" height="calc(100vh - 310px)">
      <el-table-column align="center" :label="t('auto.views.bpm.form.index.k9f42dac6')" prop="id" />
      <el-table-column
        align="center"
        :label="t('auto.views.bpm.form.index.ke3f3073e')"
        prop="name"
      />
      <el-table-column align="center" :label="t('common.status')" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注" prop="remark" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
      />
      <el-table-column align="center" label="操作">
        <template #default="scope">
          <el-button
            v-hasPermi="['bpm:manage:form:saveOrUpdate']"
            link
            type="primary"
            @click="openForm(scope.row.id)"
          >
            编辑
          </el-button>
          <el-button link @click="openDetail(scope.row.id)"> 详情 </el-button>
          <el-button
            v-hasPermi="['bpm:manage:form:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            删除
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

  <!-- 表单详情的弹窗 -->
  <Dialog v-model="detailVisible" title="表单详情" width="800">
    <form-create :option="detailData.option" :rule="detailData.rule" />
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as FormApi from '@/api/bpm/form'
import { setConfAndFields2 } from '@/utils/formCreate'
const { t } = useI18n()
defineOptions({ name: 'BpmForm' })

const message = useMessage() // 消息弹窗
const { currentRoute, push } = useRouter() // 路由

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await FormApi.getFormPage(queryParams)
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
const openForm = (id?: number) => {
  const toRouter: { name: string; query?: { id: number } } = {
    name: 'BpmFormEditor'
  }
  console.log(typeof id)
  // 表单新建的时候id传的是event需要排除
  if (typeof id === 'number' || typeof id === 'string') {
    toRouter.query = {
      id
    }
  }
  push(toRouter)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await FormApi.deleteForm(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 详情操作 */
const detailVisible = ref(false)
const detailData = ref({
  rule: [],
  option: {}
})
const openDetail = async (rowId: number) => {
  // 设置表单
  const data = await FormApi.getForm(rowId)
  setConfAndFields2(detailData, data.conf, data.fields)
  // 弹窗打开
  detailVisible.value = true
}
/**表单保存返回后重新加载列表 */
watch(
  () => currentRoute.value,
  () => {
    getList()
  },
  {
    immediate: true
  }
)
/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
