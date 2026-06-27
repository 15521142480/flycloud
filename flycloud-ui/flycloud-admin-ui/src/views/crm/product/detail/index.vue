<template>
  <ProductDetailsHeader :loading="loading" :product="product" @refresh="getProductData(id)" />
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('auto.views.crm.product.detail.index.kbf455584')">
        <ProductDetailsInfo :product="product" />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.crm.product.detail.index.kf4bc877c')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
    </el-tabs>
  </el-col>
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import { OperateLogVO } from '@/api/system/operatelog'
import * as ProductApi from '@/api/crm/product'
import ProductDetailsHeader from '@/views/crm/product/detail/ProductDetailsHeader.vue'
import ProductDetailsInfo from '@/views/crm/product/detail/ProductDetailsInfo.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { getOperateLogPage } from '@/api/crm/operateLog'
const { t } = useI18n()
defineOptions({ name: 'CrmProductDetail' })

const route = useRoute()
const message = useMessage()
const id = Number(route.params.id) // 编号
const loading = ref(true) // 加载中
const product = ref<ProductApi.ProductVO>({} as ProductApi.ProductVO) // 详情

/** 获取详情 */
const getProductData = async (id: number) => {
  loading.value = true
  try {
    product.value = await ProductApi.getProduct(id)
    await getOperateLog(id)
  } finally {
    loading.value = false
  }
}

/** 获取操作日志 */
const logList = ref<OperateLogVO[]>([]) // 操作日志列表
const getOperateLog = async (productId: number) => {
  if (!productId) {
    return
  }
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_PRODUCT,
    bizId: productId
  })
  logList.value = data.list
}

/** 初始化 */
const { delView } = useTagsViewStore() // 视图操作
const { currentRoute } = useRouter() // 路由
onMounted(async () => {
  if (!id) {
    message.warning(t('auto.views.crm.product.detail.index.k07573642'))
    delView(unref(currentRoute))
    return
  }
  await getProductData(id)
})
</script>
