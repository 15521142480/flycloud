<template>
  <ContentWrap>
    <el-collapse v-model="activeNames" class="">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.clue.detail.ClueDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.ClueFollowList.k733eabce')"
          >
            {{ clue.name }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.kb805cdaa')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_SOURCE" :value="clue.source" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k9c01ad09')">{{
            clue.mobile
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('system.dept.phone')">{{
            clue.telephone
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('system.user.email')">{{
            clue.email
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k67d2d797')">
            {{ clue.areaName }} {{ clue.detailAddress }}
          </el-descriptions-item>
          <el-descriptions-item label="QQ">{{ clue.qq }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.utils.constants.k68406df3')">{{
            clue.wechat
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k7b39ef2d')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_INDUSTRY" :value="clue.industryId" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.kbb7208b8')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="clue.level" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')">
            {{ formatDate(clue.contactNextTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.remark')">{{ clue.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('extra.kb7ea5e50') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k974d383f')">{{
            clue.ownerUserName
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('extra.k1afb06c4')">
            {{ clue.contactLastContent }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.kffa0750f')">
            {{ formatDate(clue.contactLastTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="">&nbsp;</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
            >{{ clue.creatorName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(clue.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(clue.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ClueApi from '@/api/crm/clue'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'CrmClueDetailsInfo' })
const { clue } = defineProps<{
  clue: ClueApi.ClueVO // 线索明细
}>()

const activeNames = ref(['basicInfo', 'systemInfo']) // 展示的折叠面板
</script>
<style lang="scss" scoped></style>
