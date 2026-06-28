<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.receivable.plan.detail.ReceivablePlanDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item
            :label="t('auto.views.crm.receivable.plan.components.ReceivablePlanList.k389372a5')"
            >{{ receivablePlan.period }}</el-descriptions-item
          >
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
          >
            {{ receivablePlan.customerName }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.ContractAuditList.k17b34173')"
          >
            {{ receivablePlan.contractNo }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k2eaffb78')"
          >
            {{ erpPriceInputFormatter(receivablePlan.price) }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.kf5888bd3')"
          >
            {{ formatDate(receivablePlan.returnTime, 'YYYY-MM-DD') }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.kaef4c61d')">
            <dict-tag
              :type="DICT_TYPE.CRM_RECEIVABLE_RETURN_TYPE"
              :value="receivablePlan.returnType"
            />
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k4eb4f0ab')"
          >
            {{ receivablePlan.remindDays }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.remark')">{{
            receivablePlan.remark
          }}</el-descriptions-item>
          <el-descriptions-item
            :label="
              t('auto.views.crm.receivable.plan.detail.ReceivablePlanDetailsHeader.k2b3b7506')
            "
          >
            <el-text v-if="receivablePlan.receivable">
              {{ erpPriceInputFormatter(receivablePlan.receivable.price) }}
            </el-text>
            <el-text v-else>{{ erpPriceInputFormatter(0) }}</el-text>
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.k9875650f')">
            <el-text v-if="receivablePlan.receivable">
              {{ erpPriceInputFormatter(receivablePlan.price - receivablePlan.receivable.price) }}
            </el-text>
            <el-text v-else>{{ erpPriceInputFormatter(receivablePlan.price) }}</el-text>
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.k7ebebecf')">
            {{ formatDate(receivablePlan.receivable?.returnTime, 'YYYY-MM-DD') }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('extra.kb7ea5e50') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k974d383f')">
            {{ receivablePlan.ownerUserName }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
          >
            {{ receivablePlan.creatorName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(receivablePlan.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(receivablePlan.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
const { receivablePlan } = defineProps<{
  receivablePlan: ReceivablePlanApi.ReceivablePlanVO
}>()

// 展示的折叠面板
const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
