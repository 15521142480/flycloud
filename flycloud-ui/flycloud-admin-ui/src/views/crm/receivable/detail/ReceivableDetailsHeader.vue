<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ receivable.no }}</span>
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
        :label="t('auto.views.crm.receivable.detail.ReceivableDetailsHeader.ke941d410')"
      >
        {{ receivable.customerName }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.detail.ReceivableDetailsHeader.k75e856a1')"
      >
        {{ erpPriceInputFormatter(receivable.contract?.totalPrice) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.detail.ReceivableDetailsHeader.kd0132503')"
      >
        {{ formatDate(receivable.returnTime) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.detail.ReceivableDetailsHeader.kc60d7ff5')"
      >
        {{ erpPriceInputFormatter(receivable.price) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.receivable.detail.ReceivableDetailsHeader.k974d383f')"
      >
        {{ receivable.ownerUserName }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ReceivableApi from '@/api/crm/receivable'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
const { receivable } = defineProps<{ receivable: ReceivableApi.ReceivableVO }>()
</script>
