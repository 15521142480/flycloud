<template>
  <ProductItem
    v-for="item in list"
    :spu-id="item.spuId"
    :key="item.id"
    :picUrl="item.picUrl"
    :price="item.price"
    :skuText="item.introduction"
    :title="item.spuName"
    :titleWidth="400"
    class="mb-10px"
    priceColor="#FF3000"
  />
</template>

<script lang="ts" setup>
import { getBrowseHistoryPage } from '@/api/mall/product/history'
import ProductItem from '@/views/mall/promotion/kefu/components/message/ProductItem.vue'
import { KeFuConversationRespVO } from '@/api/mall/promotion/kefu/conversation'
import { concat } from 'lodash-es'

defineOptions({ name: 'ProductBrowsingHistory' })

const list = ref<any>([]) // 列表
const total = ref(0) // 总数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: 0,
  userDeleted: false
})
const skipGetMessageList = computed(() => {
  // 已加载到最后一页的话则不触发新的消息获取
  return total.value > 0 && Math.ceil(total.value / queryParams.pageSize) === queryParams.pageNum
}) // 跳过消息获取

/** 获得浏览记录 */
const getHistoryList = async (val: KeFuConversationRespVO) => {
  queryParams.userId = val.userId
  const res = await getBrowseHistoryPage(queryParams)
  total.value = res.total
  list.value = res.list
}

/** 加载下一页数据 */
const loadMore = async () => {
  if (skipGetMessageList.value) {
    return
  }
  queryParams.pageNum += 1
  const res = await getBrowseHistoryPage(queryParams)
  total.value = res.total
  concat(list.value, res.list)
}
defineExpose({ getHistoryList, loadMore })
</script>

<style lang="scss" scoped></style>
