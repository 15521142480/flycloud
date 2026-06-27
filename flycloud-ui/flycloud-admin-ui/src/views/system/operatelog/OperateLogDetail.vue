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
        :label="t('auto.views.system.operatelog.OperateLogDetail.k4215760e')"
        min-width="120"
      >
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.system.operatelog.OperateLogDetail.k71654a4d')"
        v-if="detailData.traceId"
      >
        {{ detailData.traceId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k6e394f45')">
        {{ detailData.userId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k12b010c7')">
        {{ detailData.userName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.kcc23dd76')">
        {{ detailData.userIp }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k159fc8e4')">
        {{ detailData.userAgent }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k301c995f')">
        {{ detailData.type }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k6116e6e0')">
        {{ detailData.subType }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k33089c48')">
        {{ detailData.action }}
      </el-descriptions-item>
      <el-descriptions-item
        v-if="detailData.extra"
        :label="t('auto.views.system.operatelog.OperateLogDetail.k1be801c1')"
      >
        {{ detailData.extra }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k4686e3b2')">
        {{ detailData.requestMethod }} {{ detailData.requestUrl }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.kc7fc0c4d')">
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.system.operatelog.OperateLogDetail.k9c5f5763')">
        {{ detailData.bizId }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { formatDate } from '@/utils/formatTime'
import * as OperateLogApi from '@/api/system/operatelog'
const { t } = useI18n()
defineOptions({ name: 'SystemOperateLogDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({} as OperateLogApi.OperateLogVO) // 详情数据

/** 打开弹窗 */
const open = async (data: OperateLogApi.OperateLogVO) => {
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
