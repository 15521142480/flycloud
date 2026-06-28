<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.business.detail.BusinessDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('extra.kb6495a21')">{{
            business.name
          }}</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
            >{{ business.customerName }}</el-descriptions-item
          >
          <el-descriptions-item
            :label="t('auto.views.crm.business.detail.BusinessDetailsHeader.k9ee5e0c1')"
          >
            {{ erpPriceInputFormatter(business.totalPrice) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k491e6c11')">
            {{ formatDate(business.dealTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')">
            {{ formatDate(business.contactNextTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k9d0edbd1')">
            {{ business.statusTypeName }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.business.BusinessUpdateStatusForm.kc0a20f9a')"
            >{{ business.statusName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('common.remark')">{{
            business.remark
          }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('extra.kb7ea5e50') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k974d383f')">{{
            business.ownerUserName
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('extra.kffa0750f')">
            {{ formatDate(business.contactLastTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="">&nbsp;</el-descriptions-item>
          <el-descriptions-item label="">&nbsp;</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
            >{{ business.creatorName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(business.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(business.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as BusinessApi from '@/api/crm/business'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
const { business } = defineProps<{
  business: BusinessApi.BusinessVO
}>()

// 展示的折叠面板
const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
