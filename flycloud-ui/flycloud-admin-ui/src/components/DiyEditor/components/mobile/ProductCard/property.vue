<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form label-width="80px" :model="formData">
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.ProductCard.property.k44544d21')"
        class="property-group"
        shadow="never"
      >
        <SpuShowcase v-model="formData.spuIds" />
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.ProductCard.property.ka9d65e39')"
        class="property-group"
        shadow="never"
      >
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.kb0961e17')"
          prop="type"
        >
          <el-radio-group v-model="formData.layoutType">
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.ProductCard.property.kcfd4ceae')
              "
              placement="bottom"
            >
              <el-radio-button value="oneColBigImg">
                <Icon icon="fluent:text-column-one-24-filled" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.ProductCard.property.k13c7ca06')
              "
              placement="bottom"
            >
              <el-radio-button value="oneColSmallImg">
                <Icon icon="fluent:text-column-two-left-24-filled" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.ProductCard.property.k677628f2')
              "
              placement="bottom"
            >
              <el-radio-button value="twoCol">
                <Icon icon="fluent:text-column-two-24-filled" />
              </el-radio-button>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.k47b74133')"
          prop="fields.name.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.name.color" />
            <el-checkbox v-model="formData.fields.name.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.ke50db553')"
          prop="fields.introduction.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.introduction.color" />
            <el-checkbox v-model="formData.fields.introduction.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.k41f54c30')"
          prop="fields.price.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.price.color" />
            <el-checkbox v-model="formData.fields.price.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.kfaeb45bc')"
          prop="fields.marketPrice.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.marketPrice.color" />
            <el-checkbox v-model="formData.fields.marketPrice.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.k99911c5b')"
          prop="fields.salesCount.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.salesCount.color" />
            <el-checkbox v-model="formData.fields.salesCount.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.k9d0fd20c')"
          prop="fields.stock.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.stock.color" />
            <el-checkbox v-model="formData.fields.stock.show" />
          </div>
        </el-form-item>
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.ProductCard.property.kc7919508')"
        class="property-group"
        shadow="never"
      >
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.kc7919508')"
          prop="badge.show"
        >
          <el-switch v-model="formData.badge.show" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.ProductCard.property.kc7919508')"
          prop="badge.imgUrl"
          v-if="formData.badge.show"
        >
          <UploadImg v-model="formData.badge.imgUrl" directory="diy" height="44px" width="72px">
            <template #tip>
              {{ t('auto.components.DiyEditor.components.mobile.ProductCard.property.k5bba7969') }}
            </template>
          </UploadImg>
        </el-form-item>
      </el-card>
      <el-card :header="t('form.button')" class="property-group" shadow="never">
        <el-form-item :label="t('extra.kc82de942')" prop="btnBuy.type">
          <el-radio-group v-model="formData.btnBuy.type">
            <el-radio-button value="text">{{ t('extra.k99e832f9') }}</el-radio-button>
            <el-radio-button value="img">{{ t('extra.k3b9e2563') }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <template v-if="formData.btnBuy.type === 'text'">
          <el-form-item :label="t('extra.k1102b12a')" prop="btnBuy.text">
            <el-input v-model="formData.btnBuy.text" />
          </el-form-item>
          <el-form-item :label="t('extra.k961f2280')" prop="btnBuy.bgBeginColor">
            <ColorInput v-model="formData.btnBuy.bgBeginColor" />
          </el-form-item>
          <el-form-item :label="t('extra.k55328e6a')" prop="btnBuy.bgEndColor">
            <ColorInput v-model="formData.btnBuy.bgEndColor" />
          </el-form-item>
        </template>
        <template v-else>
          <el-form-item :label="t('extra.k3b9e2563')" prop="btnBuy.imgUrl">
            <UploadImg v-model="formData.btnBuy.imgUrl" directory="diy" height="56px" width="56px">
              <template #tip> {{ t('extra.k61b3bf07') }} </template>
            </UploadImg>
          </el-form-item>
        </template>
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
import { ProductCardProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
import SpuShowcase from '@/views/mall/product/spu/components/SpuShowcase.vue'
const { t } = useI18n()
defineOptions({ name: 'ProductCardProperty' })

const props = defineProps<{ modelValue: ProductCardProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
