<template>
  <ContentWrap>
    <el-collapse v-model="activeNames" class="">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.customer.detail.CustomerDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
          >
            {{ customer.name }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.kb805cdaa')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_SOURCE" :value="customer.source" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k9c01ad09')">{{
            customer.mobile
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('system.dept.phone')">{{
            customer.telephone
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('system.user.email')">{{
            customer.email
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k67d2d797')">
            {{ customer.areaName }} {{ customer.detailAddress }}
          </el-descriptions-item>
          <el-descriptions-item label="QQ">{{ customer.qq }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.utils.constants.k68406df3')">{{
            customer.wechat
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k7b39ef2d')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_INDUSTRY" :value="customer.industryId" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.kbb7208b8')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="customer.level" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')">
            {{ formatDate(customer.contactNextTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.remark')">{{
            customer.remark
          }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('extra.kb7ea5e50') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k974d383f')">{{
            customer.ownerUserName
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('extra.k1afb06c4')">
            {{ customer.contactLastContent }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.kffa0750f')">
            {{ formatDate(customer.contactLastTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="">&nbsp;</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
            >{{ customer.creatorName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(customer.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(customer.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as CustomerApi from '@/api/crm/customer'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'CrmCustomerDetailsInfo' })
const { customer } = defineProps<{
  customer: CustomerApi.CustomerVO // 客户明细
}>()

const activeNames = ref(['basicInfo', 'systemInfo']) // 展示的折叠面板
</script>
<style lang="scss" scoped></style>
