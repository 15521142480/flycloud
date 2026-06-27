<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ business.name }}</span>
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
        :label="t('auto.views.crm.business.detail.BusinessDetailsHeader.ke941d410')"
        >{{ business.customerName }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.business.detail.BusinessDetailsHeader.k9ee5e0c1')"
      >
        {{ erpPriceInputFormatter(business.totalPrice) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.business.detail.BusinessDetailsHeader.k949cacee')"
        >{{ business.statusTypeName }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.business.detail.BusinessDetailsHeader.k974d383f')"
        >{{ business.ownerUserName }}</el-descriptions-item
      >
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(business.createTime) }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as BusinessApi from '@/api/crm/business'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
const { business } = defineProps<{ business: BusinessApi.BusinessVO }>()
</script>
