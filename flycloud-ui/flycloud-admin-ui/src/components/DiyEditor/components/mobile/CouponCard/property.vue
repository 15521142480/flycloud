<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form label-width="80px" :model="formData">
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k74da87d3')"
        class="property-group"
        shadow="never"
      >
        <div
          v-for="(coupon, index) in couponList"
          :key="index"
          class="flex items-center justify-between"
        >
          <el-text size="large" truncated>{{ coupon.name }}</el-text>
          <el-text type="info" truncated>
            <span v-if="coupon.usePrice > 0">{{
              t('extra.k050d3bc8', { p0: floatToFixed2(coupon.usePrice) })
            }}</span>
            <span v-if="coupon.discountType === PromotionDiscountTypeEnum.PRICE.type">{{
              t('extra.k43b9bc4b', { p0: floatToFixed2(coupon.discountPrice) })
            }}</span>
            <span v-else>{{ t('extra.k22b52a7e', { p0: coupon.discountPercent }) }}</span>
          </el-text>
        </div>
        <el-form-item label-width="0">
          <el-button @click="handleAddCoupon" type="primary" plain class="m-t-8px w-full">
            <Icon icon="ep:plus" class="mr-5px" /> {{ t('extra.kdb3b33e6') }}
          </el-button>
        </el-form-item>
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k08106990')"
        class="property-group"
        shadow="never"
      >
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k067f80c3')"
          prop="type"
        >
          <el-radio-group v-model="formData.columns">
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.CouponCard.property.k595d3796')
              "
              placement="bottom"
            >
              <el-radio-button :value="1">
                <Icon icon="fluent:text-column-one-24-filled" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.CouponCard.property.k44b6f47f')
              "
              placement="bottom"
            >
              <el-radio-button :value="2">
                <Icon icon="fluent:text-column-two-24-filled" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.CouponCard.property.k75cb02d5')
              "
              placement="bottom"
            >
              <el-radio-button :value="3">
                <Icon icon="fluent:text-column-three-24-filled" />
              </el-radio-button>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k8c841866')"
          prop="bgImg"
        >
          <UploadImg v-model="formData.bgImg" directory="diy" height="80px" width="100%" class="min-w-160px" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k07f568da')"
          prop="textColor"
        >
          <ColorInput v-model="formData.textColor" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k97ab8681')"
          prop="button.bgColor"
        >
          <ColorInput v-model="formData.button.bgColor" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k78104a8d')"
          prop="button.color"
        >
          <ColorInput v-model="formData.button.color" />
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
  <!-- 优惠券选择 -->
  <CouponSelect ref="couponSelectDialog" v-model:multiple-selection="couponList" />
</template>

<script setup lang="ts">
import { CouponCardProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
import * as CouponTemplateApi from '@/api/mall/promotion/coupon/couponTemplate'
import { floatToFixed2 } from '@/utils'
import { PromotionDiscountTypeEnum } from '@/utils/constants'
import CouponSelect from '@/views/mall/promotion/coupon/components/CouponSelect.vue'
const { t } = useI18n()
defineOptions({ name: 'CouponCardProperty' })

const props = defineProps<{ modelValue: CouponCardProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)

// 优惠券列表
const couponList = ref<CouponTemplateApi.CouponTemplateVO[]>([])
const couponSelectDialog = ref()
// 添加优惠券
const handleAddCoupon = () => {
  couponSelectDialog.value.open()
}
watch(
  () => couponList.value,
  () => {
    formData.value.couponIds = couponList.value.map((coupon) => coupon.id)
  }
)
</script>

<style scoped lang="scss"></style>
