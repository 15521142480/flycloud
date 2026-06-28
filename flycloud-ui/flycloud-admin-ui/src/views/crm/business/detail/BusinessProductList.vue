<template>
  <ContentWrap>
    <el-table :data="business.products" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column
        align="center"
        :label="t('auto.views.crm.business.detail.BusinessProductList.kabc0ac79')"
        fixed="left"
        prop="productName"
        min-width="160"
      >
        <template #default="scope">
          {{ scope.row.productName }}
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.kf8df7e6f')"
        align="center"
        prop="productNo"
        min-width="120"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.crm.product.ProductForm.k298ff4b9')"
        prop="productUnit"
        min-width="160"
      >
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.CRM_PRODUCT_UNIT" :value="row.productUnit" />
        </template>
      </el-table-column>
      <el-table-column
        :label="t('extra.k3f6a71d7')"
        align="center"
        prop="productPrice"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('extra.k5e3f9f3e')"
        align="center"
        prop="businessPrice"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        :label="t('auto.views.erp.stock.check.index.kb9ae8931')"
        prop="count"
        min-width="100px"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :label="t('extra.kc53c7f26')"
        align="center"
        prop="totalPrice"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
    </el-table>
    <el-row class="mt-10px" justify="end">
      <el-col :span="3">
        {{ t('extra.k80d4c19d') }}{{ erpPriceInputFormatter(business.discountPercent) }}%
      </el-col>
      <el-col :span="4">
        {{ t('extra.k311b7e20') }}{{ erpPriceInputFormatter(business.totalProductPrice) }}
        {{ t('extra.k2d94e2e7') }}
      </el-col>
    </el-row>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as BusinessApi from '@/api/crm/business'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import { DICT_TYPE } from '@/utils/dict'
const { t } = useI18n()
const { business } = defineProps<{
  business: BusinessApi.BusinessVO
}>()
</script>
