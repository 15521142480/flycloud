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
        <el-button
          @click="openForm('update', product.id)"
          v-hasPermi="['iot:product:update']"
          v-if="product.status === 0"
        >
          {{ t('extra.k929d7448') }}
        </el-button>
        <el-button
          type="primary"
          @click="confirmPublish(product.id)"
          v-hasPermi="['iot:product:update']"
          v-if="product.status === 0"
        >
          {{ t('extra.kb464b4af') }}
        </el-button>
        <el-button
          type="danger"
          @click="confirmUnpublish(product.id)"
          v-hasPermi="['iot:product:update']"
          v-if="product.status === 1"
        >
          {{ t('extra.k23fca5ce') }}
        </el-button>
      </div>
    </div>
  </div>
  <ContentWrap class="mt-10px">
    <el-descriptions :column="5" direction="horizontal">
      <el-descriptions-item label="ProductKey">
        {{ product.productKey }}
        <el-button @click="copyToClipboard(product.productKey)">{{ t('common.copy') }}</el-button>
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="5" direction="horizontal">
      <el-descriptions-item
        :label="t('auto.views.iot.product.detail.ProductDetailsHeader.k7beb4b6b')"
      >
        {{ product.deviceCount }}
        <el-button @click="goToManagement(product.id)">{{
          t('auto.views.iot.product.detail.ProductDetailsHeader.kd96d992a')
        }}</el-button>
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
  <!-- 表单弹窗：添加/修改 -->
  <ProductForm ref="formRef" @success="emit('refresh')" />
</template>
<script setup lang="ts">
import ProductForm from '@/views/iot/product/ProductForm.vue'
import { ProductApi, ProductVO } from '@/api/iot/product'
const { t } = useI18n()
const message = useMessage()

const { product } = defineProps<{ product: ProductVO }>() // 定义 Props

/** 处理复制 */
const copyToClipboard = (text: string) => {
  navigator.clipboard.writeText(text).then(() => {
    message.success(t('auto.views.iot.product.detail.ProductDetailsHeader.kc1ef062e'))
  })
}

/** 路由跳转到设备管理 */
const { push } = useRouter()
const goToManagement = (productId: string) => {
  push({ name: 'IoTDevice', query: { productId } })
}

/** 操作修改 */
const emit = defineEmits(['refresh']) // 定义 Emits
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}
const confirmPublish = async (id: number) => {
  try {
    await ProductApi.updateProductStatus(id, 1)
    message.success(t('auto.views.iot.product.detail.ProductDetailsHeader.kec002336'))
    formRef.value.close() // 关闭弹框
    emit('refresh')
  } catch (error) {
    message.error(t('auto.views.iot.product.detail.ProductDetailsHeader.k7e7f5d44'))
  }
}
const confirmUnpublish = async (id: number) => {
  try {
    await ProductApi.updateProductStatus(id, 0)
    message.success(t('auto.views.iot.product.detail.ProductDetailsHeader.kfd4c4060'))
    formRef.value.close() // 关闭弹框
    emit('refresh')
  } catch (error) {
    message.error(t('auto.views.iot.product.detail.ProductDetailsHeader.k30a18583'))
  }
}
</script>
