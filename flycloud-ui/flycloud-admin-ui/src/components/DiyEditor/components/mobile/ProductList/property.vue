<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form label-width="80px" :model="formData">
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.ProductList.property.k44544d21')"
        class="property-group"
        shadow="never"
      >
        <SpuShowcase v-model="formData.spuIds" />
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.ProductList.property.ka9d65e39')"
        class="property-group"
        shadow="never"
      >
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductList.property.kb0961e17')"
          prop="type"
        >
          <el-radio-group v-model="formData.layoutType">
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.ProductList.property.k677628f2')
              "
              placement="bottom"
            >
              <el-radio-button value="twoCol">
                <Icon icon="fluent:text-column-two-24-filled" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.ProductList.property.k75cb02d5')
              "
              placement="bottom"
            >
              <el-radio-button value="threeCol">
                <Icon icon="fluent:text-column-three-24-filled" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.ProductList.property.ka15ad544')
              "
              placement="bottom"
            >
              <el-radio-button value="horizSwiper">
                <Icon icon="system-uicons:carousel" />
              </el-radio-button>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductList.property.k47b74133')"
          prop="fields.name.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.name.color" />
            <el-checkbox v-model="formData.fields.name.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductList.property.k41f54c30')"
          prop="fields.price.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.price.color" />
            <el-checkbox v-model="formData.fields.price.show" />
          </div>
        </el-form-item>
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.ProductList.property.kc7919508')"
        class="property-group"
        shadow="never"
      >
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductList.property.kc7919508')"
          prop="badge.show"
        >
          <el-switch v-model="formData.badge.show" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductList.property.kc7919508')"
          prop="badge.imgUrl"
          v-if="formData.badge.show"
        >
          <UploadImg v-model="formData.badge.imgUrl" height="44px" width="72px">
            <template #tip>
              {{ t('auto.components.DiyEditor.components.mobile.ProductList.property.k5bba7969') }}
            </template>
          </UploadImg>
        </el-form-item>
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.ProductCard.property.ka9d65e39')"
        class="property-group"
        shadow="never"
      >
        <el-form-item :label="t('extra.kdb0f024d')" prop="borderRadiusTop">
          <el-slider
            v-model="formData.borderRadiusTop"
            :max="100"
            :min="0"
            show-input
            input-size="small"
            :show-input-controls="false"
          />
        </el-form-item>
        <el-form-item :label="t('extra.k17430953')" prop="borderRadiusBottom">
          <el-slider
            v-model="formData.borderRadiusBottom"
            :max="100"
            :min="0"
            show-input
            input-size="small"
            :show-input-controls="false"
          />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k940c8865')"
          prop="space"
        >
          <el-slider
            v-model="formData.space"
            :max="100"
            :min="0"
            show-input
            input-size="small"
            :show-input-controls="false"
          />
        </el-form-item>
      </el-card>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { ProductListProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
import SpuShowcase from '@/views/mall/product/spu/components/SpuShowcase.vue'
const { t } = useI18n()
defineOptions({ name: 'ProductListProperty' })

const props = defineProps<{ modelValue: ProductListProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
