<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-button
      v-hasPermi="['infra:demo03-student:saveOrUpdate']"
      plain
      type="primary"
      @click="openForm('create')"
    >
      <Icon class="mr-5px" icon="ep:plus" />
      {{ t('extra.k6c89172a') }}
    </el-button>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column
        align="center"
        :label="t('auto.views.infra.demo.demo03.erp.components.k9f42dac6')"
        prop="id"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.infra.demo.demo03.erp.components.k364bd1bf')"
        prop="name"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.infra.demo.demo03.erp.components.k10b965b9')"
        prop="teacher"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        width="180px"
      />
      <el-table-column align="center" :label="t('common.operation')">
        <template #default="scope">
          <el-button
            v-hasPermi="['infra:demo03-student:saveOrUpdate']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('extra.k62be98a4') }}
          </el-button>
          <el-button
            v-hasPermi="['infra:demo03-student:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('extra.k1e2dc148') }}
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
  <!-- 表单弹窗：添加/修改 -->
  <Demo03GradeForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
// @ts-nocheck
import { dateFormatter } from '@/utils/formatTime'
import * as Demo03StudentApi from '@/api/infra/demo/demo03/erp'
import Demo03GradeForm from './Demo03GradeForm.vue'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const props = defineProps<{
  studentId?: string // 学生编号（主表的关联字段）
}>()
const loading = ref(false) // 列表的加载中
const list = ref([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  studentId: undefined as unknown
})

/** 监听主表的关联字段的变化，加载对应的子表数据 */
watch(
  () => props.studentId,
  (val: number) => {
    if (!val) {
      return
    }
    queryParams.studentId = val
    handleQuery()
  },
  { immediate: true, deep: true }
)

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await Demo03StudentApi.getDemo03GradePage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: string) => {
  if (!props.studentId) {
    message.error(t('auto.views.infra.demo.demo03.erp.components.k3606495e'))
    return
  }
  formRef.value.open(type, id, props.studentId)
}

/** 删除按钮操作 */
const handleDelete = async (id: string) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await Demo03StudentApi.deleteDemo03Grade(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}
</script>
