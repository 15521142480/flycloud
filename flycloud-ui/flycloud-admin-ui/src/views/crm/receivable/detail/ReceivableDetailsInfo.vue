<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.receivable.detail.ReceivableDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.ReceivableAuditList.k89c860f8')"
            >{{ receivable.no }}</el-descriptions-item
          >
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
          >
            {{ receivable.customerName }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.ContractAuditList.k17b34173')"
          >
            {{ receivable.contract?.no }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.receivable.ReceivableForm.kd0132503')">
            {{ formatDate(receivable.returnTime, 'YYYY-MM-DD') }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.receivable.ReceivableForm.kc60d7ff5')">
            {{ erpPriceInputFormatter(receivable.price) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.receivable.ReceivableForm.k6c0d9ca0')">
            <dict-tag :type="DICT_TYPE.CRM_RECEIVABLE_RETURN_TYPE" :value="receivable.returnType" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.remark')">{{
            receivable.remark
          }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('extra.kb7ea5e50') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k974d383f')">
            {{ receivable.ownerUserName }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
          >
            {{ receivable.creatorName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(receivable.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(receivable.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as ReceivableApi from '@/api/crm/receivable'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
const { receivable } = defineProps<{
  receivable: ReceivableApi.ReceivableVO
}>()

// 展示的折叠面板
const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
