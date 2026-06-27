<template>
  <el-card shadow="never">
    <template #header>
      <CardTitle :title="t('auto.views.mall.home.components.ShortcutCard.k73f33421')" />
    </template>
    <div class="flex flex-row flex-wrap gap-8 p-4">
      <div
        v-for="menu in menuList"
        :key="menu.name"
        class="h-20 w-20% flex flex-col cursor-pointer items-center justify-center gap-2"
        @click="handleMenuClick(menu.routerName)"
      >
        <div
          :class="menu.bgColor"
          class="h-48px w-48px flex items-center justify-center rounded text-white"
        >
          <Icon :icon="menu.icon" class="text-7.5!" />
        </div>
        <span>{{ menu.name }}</span>
      </div>
    </div>
  </el-card>
</template>
<script lang="ts" setup>
/** 快捷入口卡片 */
import { CardTitle } from '@/components/Card'
const { t } = useI18n()
defineOptions({ name: 'ShortcutCard' })

const router = useRouter() // 路由

/** 菜单列表 */
const menuList = [
  {
    name: t('auto.views.mall.home.components.ShortcutCard.kbaf84751'),
    icon: 'ep:user-filled',
    bgColor: 'bg-red-400',
    routerName: 'MemberUser'
  },
  {
    name: t('auto.views.mall.home.components.ShortcutCard.k4c536ac8'),
    icon: 'fluent-mdl2:product',
    bgColor: 'bg-orange-400',
    routerName: 'ProductSpu'
  },
  {
    name: t('auto.views.mall.home.components.ShortcutCard.k26d8e2fa'),
    icon: 'ep:list',
    bgColor: 'bg-yellow-500',
    routerName: 'TradeOrder'
  },
  {
    name: t('auto.views.mall.home.components.ShortcutCard.kd7413960'),
    icon: 'ri:refund-2-line',
    bgColor: 'bg-green-600',
    routerName: 'TradeAfterSale'
  },
  {
    name: t('auto.views.mall.home.components.ShortcutCard.kdead2295'),
    icon: 'fa-solid:project-diagram',
    bgColor: 'bg-cyan-500',
    routerName: 'TradeBrokerageUser'
  },
  {
    name: t('auto.views.mall.home.components.ShortcutCard.k30f810e7'),
    icon: 'ep:ticket',
    bgColor: 'bg-blue-500',
    routerName: 'PromotionCoupon'
  },
  {
    name: t('auto.views.mall.home.components.ShortcutCard.kf68ed13e'),
    icon: 'fa:group',
    bgColor: 'bg-purple-500',
    routerName: 'PromotionBargainActivity'
  },
  {
    name: t('auto.views.mall.home.components.ShortcutCard.k5b301da3'),
    icon: 'vaadin:money-withdraw',
    bgColor: 'bg-rose-500',
    routerName: 'TradeBrokerageWithdraw'
  }
]

/**
 * 跳转到菜单对应页面
 *
 * @param routerName 路由页面组件的名称
 */
const handleMenuClick = (routerName: string) => {
  router.push({ name: routerName })
}
</script>
