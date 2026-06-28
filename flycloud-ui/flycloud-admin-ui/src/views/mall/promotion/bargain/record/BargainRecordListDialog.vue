<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.mall.promotion.bargain.record.BargainRecordListDialog.k9d43d7d1')"
  >
    <!-- 列表 -->
    <ContentWrap>
      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
        <el-table-column
          :label="t('auto.views.mall.promotion.bargain.record.BargainRecordListDialog.kec750ef6')"
          prop="userId"
          min-width="80px"
        />
        <el-table-column
          :label="t('auto.views.mall.promotion.bargain.record.BargainRecordListDialog.k2b78f76f')"
          prop="avatar"
          min-width="80px"
        >
          <template #default="scope">
            <el-avatar :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column :label="t('profile.user.name')" prop="name" min-width="100px" />
        <el-table-column
          :label="t('extra.kcb97a6a0')"
          prop="reducePrice"
          min-width="100px"
          :formatter="fenToYuanFormat"
        />
        <el-table-column
          :label="t('extra.kfefba32a')"
          align="center"
          prop="createTime"
          :formatter="dateFormatter"
          width="180px"
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
  </Dialog>
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as BargainHelpApi from '@/api/mall/promotion/bargain/bargainHelp'
import { fenToYuanFormat } from '@/utils/formatter'
/** 助力列表 */
const { t } = useI18n()
defineOptions({ name: 'BargainRecordListDialog' })

const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  recordId: undefined
})
const queryFormRef = ref() // 搜索的表单

/** 打开弹窗 */
const dialogVisible = ref(false) // 弹窗的是否展示
const open = async (recordId: any) => {
  dialogVisible.value = true
  queryParams.recordId = recordId
  resetQuery()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await BargainHelpApi.getBargainHelpPage(queryParams)
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
  queryFormRef.value?.resetFields()
  handleQuery()
}
</script>
