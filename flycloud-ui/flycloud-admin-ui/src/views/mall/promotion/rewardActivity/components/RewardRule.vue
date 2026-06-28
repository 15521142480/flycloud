<template>
  <!-- 满减送活动规则组件 -->
  <el-row>
    <template v-if="formData.rules">
      <el-col v-for="(rule, index) in formData.rules" :key="index" :span="24">
        <span class="font-bold">{{ t('extra.k186d544e', { p0: index + 1 }) }}</span>
        <el-button v-if="index !== 0" link type="danger" @click="deleteRule(index)">
          {{ t('extra.k78ac4ef8') }}
        </el-button>
        <el-form ref="formRef" :model="rule">
          <el-form-item
            :label="t('auto.views.mall.promotion.rewardActivity.components.RewardRule.k98332b58')"
            label-width="100px"
            prop="limit"
          >
            {{ t('extra.k53a8ae0b') }}
            <el-input-number
              v-if="PromotionConditionTypeEnum.PRICE.type === formData.conditionType"
              v-model="rule.limit"
              :min="0"
              :precision="2"
              :step="0.1"
              class="w-150px! p-x-20px!"
              placeholder=""
              type="number"
              controls-position="right"
            />
            <el-input
              v-else
              v-model="rule.limit"
              :min="0"
              class="w-150px! p-x-20px!"
              placeholder=""
              type="number"
            />{{
              PromotionConditionTypeEnum.PRICE.type === formData.conditionType
                ? t('extra.k335ea324')
                : t('extra.kbef06937')
            }}</el-form-item
          >
          <el-form-item
            :label="t('auto.views.mall.promotion.rewardActivity.components.RewardRule.ke62d831a')"
            label-width="100px"
          >
            <el-col :span="24">
              {{ t('extra.k2129883a') }}
              <el-form-item>
                {{ t('extra.kd85832e8') }}
                <el-input-number
                  v-model="rule.discountPrice"
                  :min="0"
                  :precision="2"
                  :step="0.1"
                  class="w-150px! p-x-20px!"
                  controls-position="right"
                />
                {{ t('extra.k335ea324') }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <span>{{
                t('auto.views.mall.promotion.rewardActivity.components.RewardRule.k980a83a8')
              }}</span>
              <el-switch
                v-model="rule.freeDelivery"
                :active-text="t('extra.k91ede183')"
                :inactive-text="t('extra.k13d9d69d')"
                inline-prompt
              />
            </el-col>
            <el-col :span="24">
              <span>{{
                t('auto.views.mall.promotion.rewardActivity.components.RewardRule.k9600a245')
              }}</span>
              <el-form-item>
                {{ t('extra.kf929dd4c') }}
                <el-input
                  v-model="rule.point"
                  class="w-150px! p-x-20px!"
                  placeholder=""
                  type="number"
                />
                {{ t('extra.k4b7324dd') }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <span>{{
                t('auto.views.mall.promotion.rewardActivity.components.RewardRule.kd9d096f2')
              }}</span>
              <RewardRuleCouponSelect ref="rewardRuleCouponSelectRef" v-model="rule!" />
            </el-col>
          </el-form-item>
        </el-form>
      </el-col>
    </template>
    <el-col :span="24" class="mt-10px">
      <el-button type="primary" @click="addRule">{{ t('extra.k83f158b3') }}</el-button>
    </el-col>
    <el-col :span="24">
      <el-tag type="warning"> {{ t('extra.kbdf57543') }}</el-tag>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
import RewardRuleCouponSelect from './RewardRuleCouponSelect.vue'
import { RewardActivityVO } from '@/api/mall/promotion/reward/rewardActivity'
import { PromotionConditionTypeEnum } from '@/utils/constants'
import { useVModel } from '@vueuse/core'
import { isEmpty } from '@/utils/is'
const { t } = useI18n()
defineOptions({ name: 'RewardRule' })

const props = defineProps<{
  modelValue: RewardActivityVO
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', v: any): void
  (e: 'deleteRule', v: number): void
}>()

const formData = useVModel(props, 'modelValue', emits) // 活动数据
const rewardRuleCouponSelectRef = ref<InstanceType<typeof RewardRuleCouponSelect>[]>() // 活动规则优惠券 Ref

/** 删除优惠规则 */
const deleteRule = (ruleIndex: number) => {
  formData.value.rules.splice(ruleIndex, 1)
}

/** 添加优惠规则 */
const addRule = () => {
  if (isEmpty(formData.value.rules)) {
    formData.value.rules = []
  }
  formData.value.rules.push({
    limit: 0,
    discountPrice: 0,
    freeDelivery: false,
    point: 0
  })
}

/** 设置规则优惠券-提交时 */
const setRuleCoupon = () => {
  if (isEmpty(rewardRuleCouponSelectRef.value)) {
    return
  }

  rewardRuleCouponSelectRef.value?.forEach((item) => item.setGiveCouponList())
}

defineExpose({ setRuleCoupon })
</script>
