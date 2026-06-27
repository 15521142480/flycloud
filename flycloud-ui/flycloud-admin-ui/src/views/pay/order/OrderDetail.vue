<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.pay.order.OrderDetail.kdea71f6f')"
    width="700px"
  >
    <el-descriptions :column="2" label-class-name="desc-label">
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k3dc2266a')">
        <el-tag size="small">{{ detailData.merchantOrderId }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k956bb6be')">
        <el-tag type="warning" size="small" v-if="detailData.no">{{ detailData.no }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k396d9d78')">{{
        detailData.appId
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k2d87d518')">{{
        detailData.appName
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.kb9f11a33')">
        <dict-tag :type="DICT_TYPE.PAY_ORDER_STATUS" :value="detailData.status" size="small" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.kb579703e')">
        <el-tag type="success" size="small">￥{{ (detailData.price / 100.0).toFixed(2) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k307d6667')">
        <el-tag type="warning" size="small">
          ￥{{ (detailData.channelFeePrice / 100.0).toFixed(2) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.ka0757b4b')">
        {{ (detailData.channelFeeRate / 100.0).toFixed(2) }}%
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.kd4c9603f')">
        {{ formatDate(detailData.successTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k5892dd47')">
        {{ formatDate(detailData.expireTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.updateTime')">
        {{ formatDate(detailData.updateTime) }}
      </el-descriptions-item>
    </el-descriptions>
    <!-- 分割线 -->
    <el-divider />
    <el-descriptions :column="2" label-class-name="desc-label">
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.kd415beac')">{{
        detailData.subject
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k8f1a3358')">{{
        detailData.body
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k88b37342')">
        <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="detailData.channelCode" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k60c8c7a4')">{{
        detailData.userIp
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.ke551deaa')">
        <el-tag size="mini" type="success" v-if="detailData.channelOrderNo">
          {{ detailData.channelOrderNo }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k3915600b')">{{
        detailData.channelUserId
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.kf243aec2')">
        <el-tag size="mini" type="danger">
          ￥{{ (detailData.refundPrice / 100.0).toFixed(2) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k9fb2a0ae')">{{
        detailData.notifyUrl
      }}</el-descriptions-item>
    </el-descriptions>
    <!-- 分割线 -->
    <el-divider />
    <el-descriptions :column="1" label-class-name="desc-label" direction="vertical" border>
      <el-descriptions-item :label="t('auto.views.pay.order.OrderDetail.k1b23b403')">
        <el-text style="white-space: pre-wrap; word-break: break-word">
          {{ detailData.extension.channelNotifyData }}
        </el-text>
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import * as OrderApi from '@/api/pay/order'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'PayOrderDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({
  extension: {}
})

/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    detailData.value = await OrderApi.getOrderDetail(id)
    if (!detailData.value.extension) {
      detailData.value.extension = {}
    }
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
<style>
.tag-purple {
  color: #722ed1;
  background: #f9f0ff;
  border-color: #d3adf7;
}

.tag-pink {
  color: #eb2f96;
  background: #fff0f6;
  border-color: #ffadd2;
}
</style>
