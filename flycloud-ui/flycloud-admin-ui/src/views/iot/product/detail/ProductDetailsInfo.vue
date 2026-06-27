<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-descriptions
        :column="3"
        :title="t('auto.views.iot.product.detail.ProductDetailsInfo.k90095856')"
      >
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.kabc0ac79')"
          >{{ product.name }}</el-descriptions-item
        >
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.k47a991d1')"
        >
          <dict-tag :type="DICT_TYPE.IOT_PRODUCT_DEVICE_TYPE" :value="product.deviceType" />
        </el-descriptions-item>
        <el-descriptions-item :label="t('common.createTime')">
          {{ formatDate(product.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.k44f68d95')"
        >
          <dict-tag :type="DICT_TYPE.IOT_DATA_FORMAT" :value="product.dataFormat" />
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.kf151fa68')"
        >
          <dict-tag :type="DICT_TYPE.IOT_VALIDATE_TYPE" :value="product.validateType" />
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.k6f659980')"
        >
          <dict-tag :type="DICT_TYPE.IOT_PRODUCT_STATUS" :value="product.status" />
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.k17878d79')"
          v-if="product.deviceType === 0 || product.deviceType === 2"
        >
          <dict-tag :type="DICT_TYPE.IOT_NET_TYPE" :value="product.netType" />
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.k7eabdba0')"
          v-if="product.deviceType === 1"
        >
          <dict-tag :type="DICT_TYPE.IOT_PROTOCOL_TYPE" :value="product.protocolType" />
        </el-descriptions-item>
        <el-descriptions-item
          :label="t('auto.views.iot.product.detail.ProductDetailsInfo.k03917006')"
          >{{ product.description }}</el-descriptions-item
        >
      </el-descriptions>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import { DICT_TYPE } from '@/utils/dict'
import { ProductVO } from '@/api/iot/product'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
const { product } = defineProps<{ product: ProductVO }>()

// 展示的折叠面板
const activeNames = ref(['basicInfo'])
</script>
