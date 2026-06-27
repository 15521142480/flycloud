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
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k4215760e')"
        min-width="120"
      >
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k71654a4d')"
      >
        {{ detailData.traceId }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k6c19fe01')"
      >
        {{ detailData.applicationName }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k55c26aba')"
      >
        {{ detailData.userId }}
        <dict-tag :type="DICT_TYPE.USER_TYPE" :value="detailData.userType" />
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.kcde99f33')"
      >
        {{ detailData.userIp }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k8a041575')"
      >
        {{ detailData.userAgent }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.ke0d15ee3')"
      >
        {{ detailData.requestMethod }} {{ detailData.requestUrl }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k1f9ac54b')"
      >
        {{ detailData.requestParams }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k93f50e28')"
      >
        {{ detailData.responseBody }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.ke8b5eda0')"
      >
        {{ formatDate(detailData.beginTime) }} ~ {{ formatDate(detailData.endTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k94b035f8')"
        >{{ detailData.duration }} ms</el-descriptions-item
      >
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k99d2f412')"
      >
        <div v-if="detailData.resultCode === 0">{{ t('common.normal') }}</div>
        <div v-else-if="detailData.resultCode > 0">{{
          t('extra.k9b4b4ef1', { p0: detailData.resultCode, p1: detailData.resultMsg })
        }}</div>
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k301c995f')"
      >
        {{ detailData.operateModule }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k6116e6e0')"
      >
        {{ detailData.operateName }}
      </el-descriptions-item>
      <el-descriptions-item
        :label="t('auto.views.infra.apiAccessLog.ApiAccessLogDetail.k6116e6e0')"
      >
        <dict-tag :type="DICT_TYPE.INFRA_OPERATE_TYPE" :value="detailData.operateType" />
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as ApiAccessLog from '@/api/infra/apiAccessLog'
const { t } = useI18n()
defineOptions({ name: 'ApiAccessLogDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单地加载中
const detailData = ref({} as ApiAccessLog.ApiAccessLogVO) // 详情数据

/** 打开弹窗 */
const open = async (data: ApiAccessLog.ApiAccessLogVO) => {
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
