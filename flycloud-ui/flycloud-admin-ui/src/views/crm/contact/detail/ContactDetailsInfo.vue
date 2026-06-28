<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.contact.detail.ContactDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('system.user.realName')">{{
            contact.name
          }}</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
            >{{ contact.customerName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k9c01ad09')">{{
            contact.mobile
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('system.dept.phone')">{{
            contact.telephone
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('system.user.email')">{{
            contact.email
          }}</el-descriptions-item>
          <el-descriptions-item label="QQ">{{ contact.qq }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.utils.constants.k68406df3')">{{
            contact.wechat
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k67d2d797')">
            {{ contact.areaName }} {{ contact.detailAddress }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.contact.detail.ContactDetailsHeader.k4ffe06c7')"
            >{{ contact.post }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('auto.views.crm.contact.ContactForm.kcac16f50')">{{
            contact.parentName
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.contact.ContactForm.k20f9b81d')">
            <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="contact.master" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('profile.user.sex')">
            <dict-tag :type="DICT_TYPE.SYSTEM_USER_SEX" :value="contact.sex" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.clue.ClueForm.k8e1beb13')">
            {{ formatDate(contact.contactNextTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.remark')">{{
            contact.remark
          }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('extra.kb7ea5e50') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k974d383f')">{{
            contact.ownerUserName
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('extra.k1afb06c4')">
            {{ contact.contactLastContent }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.kffa0750f')">
            {{ formatDate(contact.contactLastTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="">&nbsp;</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
            >{{ contact.creatorName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(contact.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(contact.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as ContactApi from '@/api/crm/contact'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
const { contact } = defineProps<{
  contact: ContactApi.ContactVO
}>()

// 展示的折叠面板
const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
