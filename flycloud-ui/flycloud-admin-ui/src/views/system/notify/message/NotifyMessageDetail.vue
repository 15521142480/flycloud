<template>
  <Dialog v-model="dialogVisible" :max-height="500" :scroll="true" :title="t('action.detail')">
    <el-descriptions :column="1" border>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k9f42dac6')"
        min-width="120"
      >
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k31ab92d1')"
      >
        <dict-tag :type="DICT_TYPE.USER_TYPE" :value="detailData.userType" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.kec750ef6')"
      >
        {{ detailData.userId }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.kc91253f7')"
      >
        {{ detailData.templateId }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k5695a649')"
      >
        {{ detailData.templateCode }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k008bf359')"
      >
        {{ detailData.templateNickname }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k54ebbb0d')"
      >
        {{ detailData.templateContent }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k59bcfa64')"
      >
        {{ detailData.templateParams }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k653d8b5b')"
      >
        <dict-tag :type="DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE" :value="detailData.templateType" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.kbfc6b329')"
      >
        <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="detailData.readStatus" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.message.NotifyMessageDetail.k9af1a262')"
      >
        {{ formatDate(detailData.readTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as NotifyMessageApi from '@/api/system/notify/message'
const { t } = useI18n()
defineOptions({ name: 'SystemNotifyMessageDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({} as NotifyMessageApi.NotifyMessageVO) // 详情数据

/** 打开弹窗 */
const open = async (data: NotifyMessageApi.NotifyMessageVO) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    detailData.value = data
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
