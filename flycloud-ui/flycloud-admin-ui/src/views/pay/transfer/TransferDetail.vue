<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.pay.transfer.TransferDetail.k8117369a')"
    width="700px"
  >
    <el-descriptions :column="2" label-class-name="desc-label">
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.k3dc2266a')">
        <el-tag size="small">{{ detailData.merchantTransferId }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.kd3f2bbea')">
        <el-tag type="warning" size="small" v-if="detailData.no">{{ detailData.no }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.k396d9d78')">{{
        detailData.appId
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.k1a3ddc07')">
        <dict-tag :type="DICT_TYPE.PAY_TRANSFER_STATUS" :value="detailData.status" size="small" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.ka6701065')">
        <el-tag type="success" size="small">￥{{ (detailData.price / 100.0).toFixed(2) }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.kb4ee942f')">
        {{ formatDate(detailData.successTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
    </el-descriptions>
    <!-- 分割线 -->
    <el-divider />
    <el-descriptions :column="2" label-class-name="desc-label">
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.kad69bce0')">{{
        detailData.userName
      }}</el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.pay.transfer.TransferDetail.k6605bf8f')"
        v-if="detailData.type === 1"
      >
        {{ detailData.alipayLogonId }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.pay.transfer.TransferDetail.kc6eb425b')"
        v-if="detailData.type === 2"
      >
        {{ detailData.openid }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.k88b37342')">
        <dict-tag :type="DICT_TYPE.PAY_CHANNEL_CODE" :value="detailData.channelCode" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.k60c8c7a4')">{{
        detailData.userIp
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.ke551deaa')">
        <el-tag size="mini" type="success" v-if="detailData.channelTransferNo">
          {{ detailData.channelTransferNo }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.k9fb2a0ae')">{{
        detailData.notifyUrl
      }}</el-descriptions-item>
    </el-descriptions>
    <el-divider />
    <el-descriptions :column="1" label-class-name="desc-label" direction="vertical" border>
      <el-descriptions-item :label="t('auto.views.pay.transfer.TransferDetail.k69ad2b59')">
        <el-text>{{ detailData.channelNotifyData }}</el-text>
      </el-descriptions-item>
    </el-descriptions>
    <el-divider />
    <div style="text-align: right">
      <el-button @click="dialogVisible = false">{{
        t('auto.views.pay.transfer.TransferDetail.kd54aeadc')
      }}</el-button>
    </div>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import * as TransferApi from '@/api/pay/transfer'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'PayTransferDetail' })
const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({})
/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    detailData.value = await TransferApi.getTransfer(id)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>

<style scoped></style>
