<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-tabs>
      <el-tab-pane :label="t('auto.views.crm.customer.limitConfig.index.ka459c52b')">
        <CustomerLimitConfigList :confType="LimitConfType.CUSTOMER_QUANTITY_LIMIT" />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.crm.customer.limitConfig.index.kd60b44ad')">
        <CustomerLimitConfigList :confType="LimitConfType.CUSTOMER_LOCK_LIMIT" />
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>
</template>
<script setup lang="ts">
import CustomerLimitConfigList from './CustomerLimitConfigList.vue'
import { LimitConfType } from '@/api/crm/customer/limitConfig'
const { t } = useI18n()
defineOptions({ name: 'CrmCustomerLimitConfig' })
</script>
