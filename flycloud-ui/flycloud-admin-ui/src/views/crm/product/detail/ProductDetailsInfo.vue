<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{
            t('auto.views.crm.product.detail.ProductDetailsInfo.kb122f813')
          }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item
            :label="t('auto.views.crm.business.components.BusinessProductForm.kabc0ac79')"
            >{{ product.name }}</el-descriptions-item
          >
          <el-descriptions-item :label="t('auto.views.crm.product.ProductForm.k440ef986')">{{
            product.no
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.product.ProductForm.kb70c2d28')">
            {{ erpPriceInputFormatter(product.price) }} {{ t('extra.k2d94e2e7') }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.product.ProductForm.k03917006')">{{
            product.description
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.crm.product.ProductForm.k93b6f015')">{{
            product.categoryName
          }}</el-descriptions-item>
          <el-descriptions-item :label="t('extra.k41bf5483')">
            <dict-tag :type="DICT_TYPE.CRM_PRODUCT_STATUS" :value="product.status" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('auto.views.erp.product.product.ProductForm.k8ec8c8c7')">
            <dict-tag :type="DICT_TYPE.CRM_PRODUCT_UNIT" :value="product.unit" />
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import { DICT_TYPE } from '@/utils/dict'
import * as ProductApi from '@/api/crm/product'
import { erpPriceInputFormatter } from '@/utils'
const { t } = useI18n()
const { product } = defineProps<{
  product: ProductApi.ProductVO
}>()

// 展示的折叠面板
const activeNames = ref(['basicInfo'])
</script>
