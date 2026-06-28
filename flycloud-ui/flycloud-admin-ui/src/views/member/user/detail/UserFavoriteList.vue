<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list">
      <el-table-column
        key="id"
        align="center"
        :label="t('auto.views.member.user.detail.UserFavoriteList.k8d0ed357')"
        width="180"
        prop="id"
      />
      <el-table-column
        :label="t('auto.views.member.user.detail.UserFavoriteList.k1b089e84')"
        min-width="80"
      >
        <template #default="{ row }">
          <el-image :src="row.picUrl" class="h-30px w-30px" @click="imagePreview(row.picUrl)" />
        </template>
      </el-table-column>
      <el-table-column
        :show-overflow-tooltip="true"
        :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.k47b74133')"
        min-width="300"
        prop="name"
      />
      <el-table-column align="center" :label="t('extra.kfb079de8')" min-width="90" prop="price">
        <template #default="{ row }">
          {{ floatToFixed2(row.price) }}{{ t('extra.k2d94e2e7') }}</template
        >
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('extra.k44e7ebb4')"
        min-width="90"
        prop="salesCount"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('extra.k692f444e')"
        prop="createTime"
        width="180"
      />
      <el-table-column align="center" :label="t('common.status')" min-width="80">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.PRODUCT_SPU_STATUS" :value="scope.row.status" />
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
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as FavoriteApi from '@/api/mall/product/favorite'
import { floatToFixed2 } from '@/utils'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: null,
  createTime: [],
  userId: NaN
})
const queryFormRef = ref() // 搜索的表单

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await FavoriteApi.getFavoritePage(queryParams)
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

const { userId } = defineProps({
  userId: {
    type: Number,
    required: true
  }
})

/** 初始化 **/
onMounted(() => {
  queryParams.userId = userId
  getList()
})
</script>
