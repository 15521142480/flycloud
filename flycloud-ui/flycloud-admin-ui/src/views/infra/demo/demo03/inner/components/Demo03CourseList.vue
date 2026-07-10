<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        :label="t('auto.views.infra.demo.demo03.inner.components.k9f42dac6')"
        align="center"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.infra.demo.demo03.inner.components.k364bd1bf')"
        align="center"
        prop="name"
      />
      <el-table-column
        :label="t('auto.views.infra.demo.demo03.inner.components.kda335e20')"
        align="center"
        prop="score"
      />
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
    </el-table>
  </ContentWrap>
</template>
<script setup lang="ts">
// @ts-nocheck
import { dateFormatter } from '@/utils/formatTime'
import * as Demo03StudentApi from '@/api/infra/demo/demo03/inner'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const props = defineProps<{
  studentId: undefined // 学生编号（主表的关联字段）
}>()
const loading = ref(false) // 列表的加载中
const list = ref([]) // 列表的数据

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    list.value = await Demo03StudentApi.getDemo03CourseListByStudentId(props.studentId)
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
