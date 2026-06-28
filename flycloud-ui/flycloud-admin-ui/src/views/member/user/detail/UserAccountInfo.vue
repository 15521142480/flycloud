<template>
  <el-descriptions :column="2">
    <el-descriptions-item>
      <template #label>
        <descriptions-item-label
          icon="svg-icon:member_level"
          :label="t('auto.views.member.user.detail.UserAccountInfo.k5c42c048')"
        />
      </template>
      {{ user.levelName || '无' }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <descriptions-item-label icon="ep:suitcase" :label="t('extra.k4a23832a')" />
      </template>
      {{ user.experience || 0 }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <descriptions-item-label icon="ep:coin" :label="t('extra.k701a03c0')" />
      </template>
      {{ user.point || 0 }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <descriptions-item-label icon="ep:coin" :label="t('extra.ke06ab0e3')" />
      </template>
      {{ user.totalPoint || 0 }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <descriptions-item-label icon="svg-icon:member_balance" :label="t('extra.kae190ef1')" />
      </template>
      {{ fenToYuan(wallet.balance || 0) }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <descriptions-item-label
          icon="svg-icon:member_expenditure_balance"
          :label="t('auto.views.mall.statistics.trade.index.k41fc3a1c')"
        />
      </template>
      {{ fenToYuan(wallet.totalExpense || 0) }}
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <descriptions-item-label
          icon="svg-icon:member_recharge_balance"
          :label="t('auto.views.mall.statistics.trade.index.kbb535002')"
        />
      </template>
      {{ fenToYuan(wallet.totalRecharge || 0) }}
    </el-descriptions-item>
  </el-descriptions>
</template>
<script lang="ts" setup>
import { DescriptionsItemLabel } from '@/components/Descriptions'
import * as UserApi from '@/api/member/user'
import * as WalletApi from '@/api/pay/wallet/balance'
import { fenToYuan } from '@/utils'
const { t } = useI18n()
defineProps<{ user: UserApi.UserVO; wallet: WalletApi.WalletVO }>() // 用户信息
</script>
<style lang="scss" scoped>
.cell-item {
  display: inline;
}

.cell-item::after {
  content: ':';
}
</style>
