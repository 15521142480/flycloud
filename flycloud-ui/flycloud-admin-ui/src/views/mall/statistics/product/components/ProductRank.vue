<template>
  <el-card shadow="never">
    <template #header>
      <!-- 标题 -->
      <div class="flex flex-row items-center justify-between">
        <CardTitle
          :title="t('auto.views.mall.statistics.product.components.ProductRank.k38754fec')"
        />
        <!-- 查询条件 -->
        <ShortcutDateRangePicker ref="shortcutDateRangePicker" @change="handleDateRangeChange" />
      </div>
    </template>
    <!-- 排行列表 -->
    <el-table v-loading="loading" :data="list" @sort-change="handleSortChange">
      <el-table-column :label="t('extra.k858526d6')" prop="spuId" min-width="70" />
      <el-table-column
        :label="t('auto.views.mall.promotion.point.activity.index.k188d1e2d')"
        align="center"
        prop="picUrl"
        width="80"
      >
        <template #default="{ row }">
          <el-image
            :src="row.picUrl"
            :preview-src-list="[row.picUrl]"
            class="h-30px w-30px"
            preview-teleported
          />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.k47b74133')"
        prop="name"
        min-width="200"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('extra.k88bc4d7a')"
        prop="browseCount"
        min-width="90"
        sortable="custom"
      />
      <el-table-column
        :label="t('extra.kced9fc4c')"
        prop="browseUserCount"
        min-width="90"
        sortable="custom"
      />
      <el-table-column
        :label="t('extra.k9836ba49')"
        prop="cartCount"
        min-width="105"
        sortable="custom"
      />
      <el-table-column
        :label="t('extra.kef4c56f2')"
        prop="orderCount"
        min-width="105"
        sortable="custom"
      />
      <el-table-column
        :label="t('extra.kbe4e90a9')"
        prop="orderPayCount"
        min-width="105"
        sortable="custom"
      />
      <el-table-column
        :label="t('auto.views.mall.statistics.product.components.ProductSummary.kb579703e')"
        prop="orderPayPrice"
        min-width="105"
        sortable="custom"
      />
      <el-table-column
        :label="t('extra.k35337eb4')"
        prop="favoriteCount"
        min-width="90"
        sortable="custom"
      />
      <el-table-column
        :label="t('extra.kd02d2b84')"
        prop="browseConvertPercent"
        min-width="180"
        sortable="custom"
        :formatter="formatConvertRate"
      />
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getSpuList"
    />
  </el-card>
</template>
<script lang="ts" setup>
import { ProductStatisticsApi, ProductStatisticsVO } from '@/api/mall/statistics/product'
import { CardTitle } from '@/components/Card'
import { buildSortingField } from '@/utils'

/** 商品排行 */
const { t } = useI18n()
defineOptions({ name: 'ProductRank' })

// 格式化：访客-支付转化率
const formatConvertRate = (row: ProductStatisticsVO) => {
  return `${row.browseConvertPercent}%`
}

const handleSortChange = (params: any) => {
  queryParams.sortingFields = [buildSortingField(params)]
  getSpuList()
}

const handleDateRangeChange = (times: any[]) => {
  queryParams.times = times as []
  getSpuList()
}

const shortcutDateRangePicker = ref()
// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  times: [],
  sortingFields: {}
})
const loading = ref(false) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref<ProductStatisticsVO[]>([]) // 列表的数据

/** 查询商品列表 */
const getSpuList = async () => {
  loading.value = true
  try {
    const data = await ProductStatisticsApi.getProductStatisticsRankPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  await getSpuList()
})
</script>
<style lang="scss" scoped></style>
