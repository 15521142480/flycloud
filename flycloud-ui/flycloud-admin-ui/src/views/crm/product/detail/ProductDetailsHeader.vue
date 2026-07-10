<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ product.name }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <!-- 右上：按钮 -->
        <el-button @click="openForm('update', product.id)" v-hasPermi="['crm:product:saveOrUpdate']">
          {{ t('extra.k25a56180') }}
        </el-button>
      </div>
    </div>
  </div>
  <ContentWrap class="mt-10px">
    <el-descriptions :column="5" direction="vertical">
      <el-descriptions-item
        :label="t('auto.views.crm.product.detail.ProductDetailsHeader.kf169e62c')"
        >{{ product.categoryName }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.product.detail.ProductDetailsHeader.k298ff4b9')"
      >
        <dict-tag :type="DICT_TYPE.CRM_PRODUCT_UNIT" :value="product.unit" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.crm.product.detail.ProductDetailsHeader.k1e182bb2')"
        >{{
          t('extra.k5b2c9958', { p0: erpPriceInputFormatter(product.price) })
        }}</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.crm.product.detail.ProductDetailsHeader.k440ef986')"
        >{{ product.no }}</el-descriptions-item
      >
    </el-descriptions>
  </ContentWrap>
  <!-- 表单弹窗：添加/修改 -->
  <ProductForm ref="formRef" @success="emit('refresh')" />
</template>
<script setup lang="ts">
import ProductForm from '@/views/crm/product/ProductForm.vue'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceInputFormatter } from '@/utils'
import * as ProductApi from '@/api/crm/product'
const { t } = useI18n()
const formRef = ref()
const openForm = (type: string, id?: string) => {
  formRef.value.open(type, id)
}
const { product } = defineProps<{ product: ProductApi.ProductVO }>()
</script>
