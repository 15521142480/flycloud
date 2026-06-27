<template>
  <Dialog v-model="dialogVisible" :title="t('action.detail')" width="700px">
    <el-descriptions :column="2" label-class-name="desc-label">
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.kac63f2e3')">
        <el-tag size="small">{{ refundDetail.merchantRefundId }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k90b08b3a')">
        <el-tag type="success" size="small" v-if="refundDetail.channelRefundNo">{{
          refundDetail.channelRefundNo
        }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k9445bf49')">
        <el-tag size="small">{{ refundDetail.merchantOrderId }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k249a8a8d')">
        <el-tag type="success" size="small">{{ refundDetail.channelOrderNo }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k396d9d78')">{{
        refundDetail.appId
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k2d87d518')">{{
        refundDetail.appName
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.kb579703e')">
        <el-tag type="success" size="small">
          ￥{{ (refundDetail.payPrice / 100.0).toFixed(2) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.kf243aec2')">
        <el-tag size="mini" type="danger">
          ￥{{ (refundDetail.refundPrice / 100.0).toFixed(2) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k7d1f4b47')">
        <dict-tag :type="DICT_TYPE.PAY_REFUND_STATUS" :value="refundDetail.status" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k6afc75d2')">
        {{ formatDate(refundDetail.successTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(refundDetail.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.updateTime')">
        {{ formatDate(refundDetail.updateTime) }}
      </el-descriptions-item>
    </el-descriptions>
    <!-- 分割线 -->
    <el-divider />
    <el-descriptions :column="2" label-class-name="desc-label">
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k7917cc84')">
        <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="refundDetail.channelCode" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k6bbe1ee9')">{{
        refundDetail.reason
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.ke15bc757')">{{
        refundDetail.userIp
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k9fb2a0ae')">{{
        refundDetail.notifyUrl
      }}</el-descriptions-item>
    </el-descriptions>
    <!-- 分割线 -->
    <el-divider />
    <el-descriptions :column="2" label-class-name="desc-label">
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k886ef17e')">
        {{ refundDetail.channelErrorCode }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k463411d3')">
        {{ refundDetail.channelErrorMsg }}
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="1" label-class-name="desc-label" direction="vertical" border>
      <el-descriptions-item :label="t('auto.views.pay.refund.RefundDetail.k1b23b403')">
        <el-text style="white-space: pre-wrap; word-break: break-word">
          {{ refundDetail.channelNotifyData }}
        </el-text>
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as RefundApi from '@/api/pay/refund'
const { t } = useI18n()
defineOptions({ name: 'PayRefundDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const refundDetail = ref({})

/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    refundDetail.value = await RefundApi.getRefund(id)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
