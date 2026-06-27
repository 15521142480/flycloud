<template>
  <div v-loading="loading">
    <div class="flex items-start justify-between">
      <div>
        <!-- 左上：客户基本信息 -->
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ customer.name }}</span>
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
        :label="t('auto.views.crm.customer.detail.CustomerDetailsHeader.kbb7208b8')"
      >
        <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="customer.level" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.customer.detail.CustomerDetailsHeader.kf3fd0dd7')"
        >{{
          customer.dealStatus ? t('extra.k3a49d55b') : t('extra.k4b792639')
        }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.customer.detail.CustomerDetailsHeader.k974d383f')"
        >{{ customer.ownerUserName }}</el-descriptions-item
      >
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(customer.createTime) }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import * as CustomerApi from '@/api/crm/customer'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'CrmCustomerDetailsHeader' })
defineProps<{
  customer: CustomerApi.CustomerVO // 客户信息
  loading: boolean // 加载中
}>()
</script>
