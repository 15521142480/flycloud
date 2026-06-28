<!-- 合同详情组件 -->
<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="contractInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.contract.detail.ContractDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.ContractAuditList.k17b34173')"
            >{{ contract.no }}</el-descriptions-item
          >
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.ContractAuditList.kb8fbf277')"
            >{{ contract.name }}</el-descriptions-item
          >
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.CustomerFollowList.ke941d410')"
            >{{ contract.customerName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k84b59248')">{{
            contract.businessName
          }}</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k4687c195')"
          >
            {{ erpPriceInputFormatter(contract.totalPrice) }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.contract.detail.ContractDetailsHeader.k01f1aece')"
          >
            {{ formatDate(contract.orderDate) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.k159bc8c4')">
            {{ formatDate(contract.startTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('extra.k65a66e31')">
            {{ formatDate(contract.endTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.contract.ContractForm.kbebe405e')">
            {{ contract.signContactName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.contract.ContractForm.kcc2b34c1')">
            {{ contract.signUserName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.remark')">
            {{ contract.remark }}
          </el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.backlog.components.ContractAuditList.keb5aba97')"
          >
            <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="contract.auditStatus" />
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('extra.kb7ea5e50') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('auto.views.crm.business.BusinessForm.k974d383f')">{{
            contract.ownerUserName
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('extra.kffa0750f')">
            {{ formatDate(contract.contactLastTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="">&nbsp;</el-descriptions-item>
          <el-descriptions-item label="">&nbsp;</el-descriptions-item>
          <el-descriptions-item
            :label="t('auto.views.crm.statistics.rank.components.ContactCountRank.k787ad1de')"
            >{{ contract.creatorName }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(contract.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(contract.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ContractApi from '@/api/crm/contract'
import { formatDate } from '@/utils/formatTime'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
defineOptions({ name: 'ContractDetailsInfo' })
defineProps<{
  contract: ContractApi.ContractVO
}>()

// 展示的折叠面板
const activeNames = ref(['contractInfo', 'systemInfo'])
</script>
