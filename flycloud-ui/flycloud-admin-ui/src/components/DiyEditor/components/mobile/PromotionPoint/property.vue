<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form :model="formData" label-width="80px">
      <el-card
        class="property-group"
        :header="t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k929ca2dd')"
        shadow="never"
      >
        <PointShowcase v-model="formData.activityIds" />
      </el-card>
      <el-card
        class="property-group"
        :header="t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.ka9d65e39')"
        shadow="never"
      >
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.kb0961e17')
          "
          prop="type"
        >
          <el-radio-group v-model="formData.layoutType">
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.kcfd4ceae')
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
                t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k13c7ca06')
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
                t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k677628f2')
              "
              placement="bottom"
            >
              <el-radio-button value="twoCol">
                <Icon icon="fluent:text-column-two-24-filled" />
              </el-radio-button>
            </el-tooltip>
            <!--<el-tooltip class="item" :content="t('extra.k2d69d155')" placement="bottom">
              <el-radio-button value="threeCol">
                <Icon icon="fluent:text-column-three-24-filled" />
              </el-radio-button>
            </el-tooltip>-->
          </el-radio-group>
        </el-form-item>
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k47b74133')
          "
          prop="fields.name.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.name.color" />
            <el-checkbox v-model="formData.fields.name.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.ke50db553')
          "
          prop="fields.introduction.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.introduction.color" />
            <el-checkbox v-model="formData.fields.introduction.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k41f54c30')
          "
          prop="fields.price.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.price.color" />
            <el-checkbox v-model="formData.fields.price.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.kfaeb45bc')
          "
          prop="fields.marketPrice.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.marketPrice.color" />
            <el-checkbox v-model="formData.fields.marketPrice.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k99911c5b')
          "
          prop="fields.salesCount.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.salesCount.color" />
            <el-checkbox v-model="formData.fields.salesCount.show" />
          </div>
        </el-form-item>
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k9d0fd20c')
          "
          prop="fields.stock.show"
        >
          <div class="flex gap-8px">
            <ColorInput v-model="formData.fields.stock.color" />
            <el-checkbox v-model="formData.fields.stock.show" />
          </div>
        </el-form-item>
      </el-card>
      <el-card
        class="property-group"
        :header="t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.kc7919508')"
        shadow="never"
      >
        <el-form-item
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.kc7919508')
          "
          prop="badge.show"
        >
          <el-switch v-model="formData.badge.show" />
        </el-form-item>
        <el-form-item
          v-if="formData.badge.show"
          :label="
            t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.kc7919508')
          "
          prop="badge.imgUrl"
        >
          <UploadImg v-model="formData.badge.imgUrl" height="44px" width="72px">
            <template #tip>
              {{
                t('auto.components.DiyEditor.components.mobile.PromotionPoint.property.k5bba7969')
              }}</template
            >
          </UploadImg>
        </el-form-item>
      </el-card>
      <el-card class="property-group" :header="t('extra.k3cdbab81')" shadow="never">
        <el-form-item :label="t('extra.kd0c33412')" prop="btnBuy.type">
          <el-radio-group v-model="formData.btnBuy.type">
            <el-radio-button value="text">{{ t('extra.k99e832f9') }}</el-radio-button>
            <el-radio-button value="img">{{ t('extra.k3b9e2563') }}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <template v-if="formData.btnBuy.type === 'text'">
          <el-form-item :label="t('extra.k1102b12a')" prop="btnBuy.text">
            <el-input v-model="formData.btnBuy.text" />
          </el-form-item>
          <el-form-item :label="t('extra.ka28fd4ad')" prop="btnBuy.bgBeginColor">
            <ColorInput v-model="formData.btnBuy.bgBeginColor" />
          </el-form-item>
          <el-form-item :label="t('extra.k55328e6a')" prop="btnBuy.bgEndColor">
            <ColorInput v-model="formData.btnBuy.bgEndColor" />
          </el-form-item>
        </template>
        <template v-else>
          <el-form-item :label="t('extra.k3b9e2563')" prop="btnBuy.imgUrl">
            <UploadImg v-model="formData.btnBuy.imgUrl" height="56px" width="56px">
              <template #tip> {{ t('extra.k61b3bf07') }}</template>
            </UploadImg>
          </el-form-item>
        </template>
      </el-card>
      <el-card
        class="property-group"
        :header="t('auto.components.DiyEditor.components.mobile.ProductCard.property.ka9d65e39')"
        shadow="never"
      >
        <el-form-item :label="t('extra.kdb0f024d')" prop="borderRadiusTop">
          <el-slider
            v-model="formData.borderRadiusTop"
            :max="100"
            :min="0"
            :show-input-controls="false"
            input-size="small"
            show-input
          />
        </el-form-item>
        <el-form-item :label="t('extra.k17430953')" prop="borderRadiusBottom">
          <el-slider
            v-model="formData.borderRadiusBottom"
            :max="100"
            :min="0"
            :show-input-controls="false"
            input-size="small"
            show-input
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
            :show-input-controls="false"
            input-size="small"
            show-input
          />
        </el-form-item>
      </el-card>
    </el-form>
  </ComponentContainerProperty>
</template>

<script lang="ts" setup>
import { PromotionPointProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
import PointShowcase from '@/views/mall/promotion/point/components/PointShowcase.vue'

// 秒杀属性面板
const { t } = useI18n()
defineOptions({ name: 'PromotionPointProperty' })

const props = defineProps<{ modelValue: PromotionPointProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style lang="scss" scoped></style>
