<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{
              t('extra.k85505c9c', { p0: receivablePlan.period })
            }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <!-- 右上：按钮 -->
        <slot></slot>
      </div>
    </div>
  </div>
  <ContentWrap class="mt-10px">
    <el-descriptions :column="5" direction="vertical">
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.plan.detail.ReceivablePlanDetailsHeader.ke941d410')"
      >
        {{ receivablePlan.customerName }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.plan.detail.ReceivablePlanDetailsHeader.k17b34173')"
        >{{ receivablePlan.contractNo }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.plan.detail.ReceivablePlanDetailsHeader.k2eaffb78')"
      >
        {{ erpPriceInputFormatter(receivablePlan.price) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.plan.detail.ReceivablePlanDetailsHeader.kf5888bd3')"
      >
        {{ formatDate(receivablePlan.returnTime) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.plan.detail.ReceivablePlanDetailsHeader.k2b3b7506')"
      >
        <el-text v-if="receivablePlan.receivable">
          {{ erpPriceInputFormatter(receivablePlan.receivable.price) }}
        </el-text>
        <el-text v-else>{{ erpPriceInputFormatter(0) }}</el-text>
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
const { receivablePlan } = defineProps<{ receivablePlan: ReceivablePlanApi.ReceivablePlanVO }>()
</script>
