<template>
  <Dialog
    v-model="dialogVisible"
    :max-height="500"
    :scroll="true"
    :title="t('action.detail')"
    width="800"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item
        :label="t('auto.views.system.sms.log.SmsLogDetail.k4215760e')"
        min-width="120"
      >
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k7575f737')">
        {{ channelList.find((channel) => channel.id === detailData.channelId)?.signature }}
        <dict-tag :type="DICT_TYPE.SYSTEM_SMS_CHANNEL_CODE" :value="detailData.channelCode" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k70aa4b5b')">
        {{ detailData.templateId }} | {{ detailData.templateCode }}
        <dict-tag :type="DICT_TYPE.SYSTEM_SMS_TEMPLATE_TYPE" :value="detailData.templateType" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.kf626f98e')">
        {{ detailData.apiTemplateId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k55c26aba')">
        {{ detailData.mobile }}
        <span v-if="detailData.userType && detailData.userId">
          <dict-tag :type="DICT_TYPE.USER_TYPE" :value="detailData.userType" />
          ({{ detailData.userId }})
        </span>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k687fd112')">
        {{ detailData.templateContent }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k84ca12ff')">
        {{ detailData.templateParams }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k9fa54101')">
        <dict-tag :type="DICT_TYPE.SYSTEM_SMS_SEND_STATUS" :value="detailData.sendStatus" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k98c64dd6')">
        {{ formatDate(detailData.sendTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k721d35f1')">
        {{ detailData.apiSendCode }} | {{ detailData.apiSendMsg }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.ka7eb74f9')">
        {{ detailData.apiSerialNo }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k2882da04')">
        {{ detailData.apiRequestId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.kbb48cecc')">
        <dict-tag :type="DICT_TYPE.SYSTEM_SMS_RECEIVE_STATUS" :value="detailData.receiveStatus" />
        {{ formatDate(detailData.receiveTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.sms.log.SmsLogDetail.k00232a88')">
        {{ detailData.apiReceiveCode }} | {{ detailData.apiReceiveMsg }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as SmsLogApi from '@/api/system/sms/smsLog'
import * as SmsChannelApi from '@/api/system/sms/smsChannel'
const { t } = useI18n()
defineOptions({ name: 'SystemSmsLogDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref() // 详情数据
const channelList = ref([]) // 短信渠道列表

/** 打开弹窗 */
const open = async (data: SmsLogApi.SmsLogVO) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    detailData.value = data
  } finally {
    detailLoading.value = false
  }
  // 加载渠道列表
  channelList.value = await SmsChannelApi.getSimpleSmsChannelList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
