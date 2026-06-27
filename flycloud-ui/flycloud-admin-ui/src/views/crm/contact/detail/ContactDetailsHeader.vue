<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ contact.name }}</span>
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
        :label="t('auto.views.crm.contact.detail.ContactDetailsHeader.ke941d410')"
        >{{ contact.customerName }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.contact.detail.ContactDetailsHeader.k4ffe06c7')"
        >{{ contact.post }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.contact.detail.ContactDetailsHeader.k9c01ad09')"
        >{{ contact.mobile }}</el-descriptions-item
      >
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(contact.createTime) }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ContactApi from '@/api/crm/contact'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
const { contact } = defineProps<{ contact: ContactApi.ContactVO }>()
</script>
