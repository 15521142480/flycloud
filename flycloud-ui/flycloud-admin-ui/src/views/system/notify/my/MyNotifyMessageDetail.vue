<template>
  <Dialog
    v-model="dialogVisible"
    :max-height="500"
    :scroll="true"
    :title="t('auto.views.system.notify.my.MyNotifyMessageDetail.k9574fa1e')"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item
        :label="t('auto.views.system.notify.my.MyNotifyMessageDetail.k3bc6e681')"
      >
        {{ detailData.templateNickname }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.my.MyNotifyMessageDetail.k98c64dd6')"
      >
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.my.MyNotifyMessageDetail.k15218877')"
      >
        <dict-tag :type="DICT_TYPE.SYSTEM_NOTIFY_TEMPLATE_TYPE" :value="detailData.templateType" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.my.MyNotifyMessageDetail.kbfc6b329')"
      >
        <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="detailData.readStatus" />
      </el-descriptions-item>
      <el-descriptions-item
        v-if="detailData.readStatus"
        :label="t('auto.views.system.notify.my.MyNotifyMessageDetail.k9af1a262')"
      >
        {{ formatDate(detailData.readTime) }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.notify.my.MyNotifyMessageDetail.k163aec91')"
      >
        {{ detailData.templateContent }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as NotifyMessageApi from '@/api/system/notify/message'
const { t } = useI18n()
defineOptions({ name: 'MyNotifyMessageDetailDetail' })

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
