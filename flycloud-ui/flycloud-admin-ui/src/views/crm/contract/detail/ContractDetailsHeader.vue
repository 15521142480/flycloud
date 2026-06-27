<!-- 合同详情头部组件-->
<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ contract.name }}</span>
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
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.ke941d410')"
      >
        {{ contract.customerName }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k4687c195')"
      >
        {{ erpPriceInputFormatter(contract.totalPrice) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k01f1aece')"
      >
        {{ formatDate(contract.orderDate) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.kfe6b09b9')"
      >
        {{ erpPriceInputFormatter(contract.totalReceivablePrice) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k974d383f')"
      >
        {{ contract.ownerUserName }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ContractApi from '@/api/crm/contract'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'ContractDetailsHeader' })
defineProps<{ contract: ContractApi.ContractVO }>()
</script>
