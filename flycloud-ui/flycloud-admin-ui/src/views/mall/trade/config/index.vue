<template>
  <doc-alert
    :title="t('auto.views.mall.trade.config.index.k1daebace')"
    url="https://doc.iocoder.cn/mall/trade-order/"
  />
  <doc-alert
    :title="t('auto.views.mall.trade.config.index.k4c3890b5')"
    url="https://doc.iocoder.cn/mall/trade-cart/"
  />

  <ContentWrap>
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="120px"
    >
      <el-form-item v-show="false" label="hideId">
        <el-input v-model="formData.id" />
      </el-form-item>
      <el-tabs>
        <!-- 售后 -->
        <el-tab-pane :label="t('auto.views.mall.trade.config.index.k7f7a2fa0')">
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.kd63db516')"
            prop="afterSaleRefundReasons"
          >
            <el-select
              v-model="formData.afterSaleRefundReasons"
              allow-create
              filterable
              multiple
              :placeholder="t('auto.views.mall.trade.config.index.kf91fc99a')"
            >
              <el-option
                v-for="reason in formData.afterSaleRefundReasons"
                :key="reason"
                :label="reason"
                :value="reason"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.kb048ba88')"
            prop="afterSaleReturnReasons"
          >
            <el-select
              v-model="formData.afterSaleReturnReasons"
              allow-create
              filterable
              multiple
              :placeholder="t('auto.views.mall.trade.config.index.k7f6b94f3')"
            >
              <el-option
                v-for="reason in formData.afterSaleReturnReasons"
                :key="reason"
                :label="reason"
                :value="reason"
              />
            </el-select>
          </el-form-item>
        </el-tab-pane>
        <!-- 配送 -->
        <el-tab-pane :label="t('auto.views.mall.trade.config.index.kb6e8d4b7')">
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k10a53642')"
            prop="deliveryExpressFreeEnabled"
          >
            <el-switch v-model="formData.deliveryExpressFreeEnabled" style="user-select: none" />
            <el-text class="w-full" size="small" type="info">
              {{ t('auto.views.mall.trade.config.index.kf4c4214e') }}</el-text
            >
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.kc0f0699b')"
            prop="deliveryExpressFreePrice"
          >
            <el-input-number
              v-model="formData.deliveryExpressFreePrice"
              :min="0"
              :precision="2"
              class="!w-xs"
              :placeholder="t('auto.views.mall.trade.config.index.k16f01a75')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k628ee12c') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k361c3a7a')"
            prop="deliveryPickUpEnabled"
          >
            <el-switch v-model="formData.deliveryPickUpEnabled" style="user-select: none" />
          </el-form-item>
        </el-tab-pane>
        <!-- 分销 -->
        <el-tab-pane :label="t('auto.views.mall.trade.config.index.kc93f9efd')">
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k4ab21008')"
            prop="brokerageEnabled"
          >
            <el-switch v-model="formData.brokerageEnabled" style="user-select: none" />
            <el-text class="w-full" size="small" type="info">
              {{ t('auto.views.mall.trade.config.index.kc05de5d8') }}</el-text
            >
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k35e59462')"
            prop="brokerageEnabledCondition"
          >
            <el-radio-group v-model="formData.brokerageEnabledCondition">
              <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.BROKERAGE_ENABLED_CONDITION)"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k4c49b718') }}
            </el-text>
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k8242bc52') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.kcd24cdb2')"
            prop="brokerageBindMode"
          >
            <el-radio-group v-model="formData.brokerageBindMode">
              <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.BROKERAGE_BIND_MODE)"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.kd6f7153a') }}
            </el-text>
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k9bb16b5b') }}
            </el-text>
          </el-form-item>
          <el-form-item :label="t('auto.views.mall.trade.config.index.k3708a549')">
            <UploadImgs v-model="formData.brokeragePosterUrls" height="125px" width="75px" />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.kc8511a2f') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k73438f30')"
            prop="brokerageFirstPercent"
          >
            <el-input-number
              v-model="formData.brokerageFirstPercent"
              :max="100"
              :min="0"
              class="!w-xs"
              :placeholder="t('auto.views.mall.trade.config.index.k8aa0bc43')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.kb7fd1457') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k74fd730b')"
            prop="brokerageSecondPercent"
          >
            <el-input-number
              v-model="formData.brokerageSecondPercent"
              :max="100"
              :min="0"
              class="!w-xs"
              :placeholder="t('auto.views.mall.trade.config.index.kf89c6d59')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k38915184') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k1eaeece0')"
            prop="brokerageFrozenDays"
          >
            <el-input-number
              v-model="formData.brokerageFrozenDays"
              :min="0"
              class="!w-xs"
              :placeholder="t('auto.views.mall.trade.config.index.ka8d16059')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.kd22ac5d2') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k46e6e2b6')"
            prop="brokerageWithdrawMinPrice"
          >
            <el-input-number
              v-model="formData.brokerageWithdrawMinPrice"
              :min="0"
              :precision="2"
              class="!w-xs"
              :placeholder="t('auto.views.mall.trade.config.index.kaf708749')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.k567cc409') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k6408b941')"
            prop="brokerageWithdrawFeePercent"
          >
            <el-input-number
              v-model="formData.brokerageWithdrawFeePercent"
              :max="100"
              :min="0"
              class="!w-xs"
              :placeholder="t('auto.views.mall.trade.config.index.k982aa2e7')"
            />
            <el-text class="w-full" size="small" type="info">
              {{ t('extra.kd69db6bf') }}
            </el-text>
          </el-form-item>
          <el-form-item
            :label="t('auto.views.mall.trade.config.index.k9f9ca520')"
            prop="brokerageWithdrawTypes"
          >
            <el-checkbox-group v-model="formData.brokerageWithdrawTypes">
              <el-checkbox
                v-for="dict in getIntDictOptions(DICT_TYPE.BROKERAGE_WITHDRAW_TYPE)"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-checkbox>
            </el-checkbox-group>
            <el-text class="w-full" size="small" type="info">
              {{ t('auto.views.mall.trade.config.index.k1aa406f0') }}</el-text
            >
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
      <!-- 保存 -->
      <el-form-item>
        <el-button :loading="formLoading" type="primary" @click="submitForm">
          {{ t('common.save') }}</el-button
        >
      </el-form-item>
    </el-form>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as ConfigApi from '@/api/mall/trade/config'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { cloneDeep } from 'lodash-es'
const { t } = useI18n()
defineOptions({ name: 'TradeConfig' })

const message = useMessage() // 消息弹窗

const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formRef = ref()
const formData = ref({
  id: null,
  afterSaleRefundReasons: [],
  afterSaleReturnReasons: [],
  deliveryExpressFreeEnabled: false,
  deliveryExpressFreePrice: 0,
  deliveryPickUpEnabled: false,
  brokerageEnabled: false,
  brokerageEnabledCondition: undefined,
  brokerageBindMode: undefined,
  brokeragePosterUrls: [],
  brokerageFirstPercent: 0,
  brokerageSecondPercent: 0,
  brokerageWithdrawMinPrice: 0,
  brokerageWithdrawFeePercent: 0,
  brokerageFrozenDays: 0,
  brokerageWithdrawTypes: []
})
const formRules = reactive({
  deliveryExpressFreePrice: [
    { required: true, message: t('auto.views.mall.trade.config.index.k145a0676'), trigger: 'blur' }
  ],
  brokerageEnabledCondition: [
    { required: true, message: t('auto.views.mall.trade.config.index.kd42e9fbd'), trigger: 'blur' }
  ],
  brokerageBindMode: [
    { required: true, message: t('auto.views.mall.trade.config.index.ke5a11825'), trigger: 'blur' }
  ],
  brokerageFirstPercent: [
    { required: true, message: t('auto.views.mall.trade.config.index.k36aad944'), trigger: 'blur' }
  ],
  brokerageSecondPercent: [
    { required: true, message: t('auto.views.mall.trade.config.index.k8e08c961'), trigger: 'blur' }
  ],
  brokerageWithdrawMinPrice: [
    { required: true, message: t('auto.views.mall.trade.config.index.kfa62d106'), trigger: 'blur' }
  ],
  brokerageWithdrawFeePercent: [
    { required: true, message: t('auto.views.mall.trade.config.index.k8dee238f'), trigger: 'blur' }
  ],
  brokerageFrozenDays: [
    { required: true, message: t('auto.views.mall.trade.config.index.k797ef89d'), trigger: 'blur' }
  ],
  brokerageWithdrawTypes: [
    {
      required: true,
      message: t('auto.views.mall.trade.config.index.k33075cf9'),
      trigger: 'change'
    }
  ]
})

const submitForm = async () => {
  if (formLoading.value) return
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = cloneDeep(unref(formData.value)) as unknown as ConfigApi.ConfigVO
    // 金额放大
    data.deliveryExpressFreePrice = data.deliveryExpressFreePrice * 100
    data.brokerageWithdrawMinPrice = data.brokerageWithdrawMinPrice * 100
    await ConfigApi.saveTradeConfig(data)
    message.success(t('auto.views.mall.trade.config.index.k7e68eb62'))
  } finally {
    formLoading.value = false
  }
}

/** 查询交易中心配置 */
const getConfig = async () => {
  formLoading.value = true
  try {
    const data = await ConfigApi.getTradeConfig()
    if (data != null) {
      formData.value = data
      // 金额缩小
      formData.value.deliveryExpressFreePrice = data.deliveryExpressFreePrice / 100
      formData.value.brokerageWithdrawMinPrice = data.brokerageWithdrawMinPrice / 100
    }
  } finally {
    formLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getConfig()
})
</script>
