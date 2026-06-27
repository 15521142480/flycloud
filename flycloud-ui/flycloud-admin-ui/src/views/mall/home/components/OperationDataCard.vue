<template>
  <el-card shadow="never">
    <template #header>
      <CardTitle :title="t('auto.views.mall.home.components.OperationDataCard.k45b9d122')" />
    </template>
    <div class="flex flex-row flex-wrap items-center gap-8 p-4">
      <div
        v-for="item in data"
        :key="item.name"
        class="h-20 w-20% flex flex-col cursor-pointer items-center justify-center gap-2"
        @click="handleClick(item.routerName)"
      >
        <CountTo
          :decimals="item.decimals"
          :end-val="item.value"
          :prefix="item.prefix"
          class="text-3xl"
        />
        <span class="text-center">{{ item.name }}</span>
      </div>
    </div>
  </el-card>
</template>
<script lang="ts" setup>
import * as ProductSpuApi from '@/api/mall/product/spu'
import * as TradeStatisticsApi from '@/api/mall/statistics/trade'
import * as PayStatisticsApi from '@/api/mall/statistics/pay'
import { CardTitle } from '@/components/Card'

/** 运营数据卡片 */
const { t } = useI18n()
defineOptions({ name: 'OperationDataCard' })

const router = useRouter() // 路由

/** 数据 */
const data = reactive({
  orderUndelivered: {
    name: t('auto.views.mall.home.components.OperationDataCard.kc1d9820f'),
    value: 9,
    routerName: 'TradeOrder'
  },
  orderAfterSaleApply: {
    name: t('auto.views.mall.home.components.OperationDataCard.k8c6a48f8'),
    value: 4,
    routerName: 'TradeAfterSale'
  },
  orderWaitePickUp: {
    name: t('auto.views.mall.home.components.OperationDataCard.k65b5072d'),
    value: 0,
    routerName: 'TradeOrder'
  },
  productAlertStock: {
    name: t('auto.views.mall.home.components.OperationDataCard.ke8ee7bca'),
    value: 0,
    routerName: 'ProductSpu'
  },
  productForSale: {
    name: t('auto.views.mall.home.components.OperationDataCard.kc6a49d3a'),
    value: 0,
    routerName: 'ProductSpu'
  },
  productInWarehouse: {
    name: t('auto.views.mall.home.components.OperationDataCard.k70b85c3d'),
    value: 0,
    routerName: 'ProductSpu'
  },
  withdrawAuditing: {
    name: t('auto.views.mall.home.components.OperationDataCard.kd85c40da'),
    value: 0,
    routerName: 'TradeBrokerageWithdraw'
  },
  rechargePrice: {
    name: t('auto.views.mall.home.components.OperationDataCard.k46c2558a'),
    value: 0.0,
    prefix: '￥',
    decimals: 2,
    routerName: 'PayWalletRecharge'
  }
})

/** 查询订单数据 */
const getOrderData = async () => {
  const orderCount = await TradeStatisticsApi.getOrderCount()
  if (orderCount.undelivered != null) {
    data.orderUndelivered.value = orderCount.undelivered
  }
  if (orderCount.afterSaleApply != null) {
    data.orderAfterSaleApply.value = orderCount.afterSaleApply
  }
  if (orderCount.pickUp != null) {
    data.orderWaitePickUp.value = orderCount.pickUp
  }
  if (orderCount.auditingWithdraw != null) {
    data.withdrawAuditing.value = orderCount.auditingWithdraw
  }
}

/** 查询商品数据 */
const getProductData = async () => {
  // TODO: ：这个接口的返回值，是不是用命名字段更好些？
  const productCount = await ProductSpuApi.getTabsCount()
  data.productForSale.value = productCount['0']
  data.productInWarehouse.value = productCount['1']
  data.productAlertStock.value = productCount['3']
}

/** 查询钱包充值数据 */
const getWalletRechargeData = async () => {
  const paySummary = await PayStatisticsApi.getWalletRechargePrice()
  data.rechargePrice.value = paySummary.rechargePrice
}

/**
 * 跳转到对应页面
 *
 * @param routerName 路由页面组件的名称
 */
const handleClick = (routerName: string) => {
  router.push({ name: routerName })
}

/** 激活时 */
onActivated(() => {
  getOrderData()
  getProductData()
  getWalletRechargeData()
})

/** 初始化 **/
onMounted(() => {
  getOrderData()
  getProductData()
  getWalletRechargeData()
})
</script>
