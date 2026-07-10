<template>
  <ProductDetailsHeader :loading="loading" :product="product" @refresh="() => getProductData(id)" />
  <el-col>
    <el-tabs v-model="activeTab">
      <el-tab-pane :label="t('auto.views.iot.product.detail.index.k90095856')" name="info">
        <ProductDetailsInfo v-if="activeTab === 'info'" :product="product" />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.iot.product.detail.index.k25c4668a')" name="topic">
        <ProductTopic v-if="activeTab === 'topic'" :product="product" />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.iot.product.detail.index.kcf6091a6')" name="function">
        <ThinkModelFunction v-if="activeTab === 'function'" :product="product" />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.iot.product.detail.index.k27c0f91e')" name="message" />
      <el-tab-pane
        :label="t('auto.views.iot.product.detail.index.k0431306e')"
        name="subscription"
      />
    </el-tabs>
  </el-col>
</template>
<script lang="ts" setup>
import { ProductApi, ProductVO } from '@/api/iot/product'
import { DeviceApi } from '@/api/iot/device'
import ProductDetailsHeader from '@/views/iot/product/detail/ProductDetailsHeader.vue'
import ProductDetailsInfo from '@/views/iot/product/detail/ProductDetailsInfo.vue'
import ProductTopic from '@/views/iot/product/detail/ProductTopic.vue'
import ThinkModelFunction from '@/views/iot/product/detail/ThinkModelFunction.vue'
import { useTagsViewStore } from '@/store/modules/tagsView'
import { useRouter } from 'vue-router'
const { t } = useI18n()
defineOptions({ name: 'IoTProductDetail' })

const { delView } = useTagsViewStore() // 视图操作
const { currentRoute } = useRouter()

const route = useRoute()
const message = useMessage()
const id = String(route.params.id) // 编号
const loading = ref(true) // 加载中
const product = ref<ProductVO>({} as ProductVO) // 详情
const activeTab = ref('info') // 默认激活的标签页

/** 获取详情 */
const getProductData = async (id: string) => {
  loading.value = true
  try {
    product.value = await ProductApi.getProduct(id)
    console.log('Product data:', product.value)
  } finally {
    loading.value = false
  }
}

// 查询设备数量
const getDeviceCount = async (productId: string) => {
  try {
    const count = await DeviceApi.getDeviceCount(productId)
    console.log('Device count response:', count)
    return count
  } catch (error) {
    console.error('Error fetching device count:', error)
    return 0
  }
}

/** 初始化 */
onMounted(async () => {
  if (!id) {
    message.warning(t('auto.views.iot.product.detail.index.k07573642'))
    delView(unref(currentRoute))
    return
  }
  await getProductData(id)
  // 查询设备数量
  if (product.value.id) {
    product.value.deviceCount = await getDeviceCount(product.value.id)
    console.log('Device count:', product.value.deviceCount)
  } else {
    console.error('Product ID is undefined')
  }
})
</script>
