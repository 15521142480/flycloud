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
        :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k4215760e')"
        min-width="120"
      >
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k71654a4d')">
        {{ detailData.traceId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k6c19fe01')">
        {{ detailData.applicationName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.kec750ef6')">
        {{ detailData.userId }}
        <dict-tag :type="DICT_TYPE.USER_TYPE" :value="detailData.userType" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.kcde99f33')">
        {{ detailData.userIp }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k8a041575')">
        {{ detailData.userAgent }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.ke0d15ee3')">
        {{ detailData.requestMethod }} {{ detailData.requestUrl }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k1f9ac54b')">
        {{ detailData.requestParams }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.kdf79ec03')">
        {{ formatDate(detailData.exceptionTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.kaaa3d222')">
        {{ detailData.exceptionName }}
      </el-descriptions-item>
      <el-descriptions-item
        v-if="detailData.exceptionStackTrace"
        :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k4655510c')"
      >
        <el-input
          v-model="detailData.exceptionStackTrace"
          :autosize="{ maxRows: 20 }"
          :readonly="true"
          type="textarea"
        />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k8542beb9')">
        <dict-tag
          :type="DICT_TYPE.INFRA_API_ERROR_LOG_PROCESS_STATUS"
          :value="detailData.processStatus"
        />
      </el-descriptions-item>
      <el-descriptions-item
        v-if="detailData.processUserId"
        :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.k3e132a2a')"
      >
        {{ detailData.processUserId }}
      </el-descriptions-item>
      <el-descriptions-item
        v-if="detailData.processTime"
        :label="t('auto.views.infra.apiErrorLog.ApiErrorLogDetail.kf42bdf4f')"
      >
        {{ formatDate(detailData.processTime) }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as ApiErrorLog from '@/api/infra/apiErrorLog'
const { t } = useI18n()
defineOptions({ name: 'ApiErrorLogDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({} as ApiErrorLog.ApiErrorLogVO) // 详情数据

/** 打开弹窗 */
const open = async (data: ApiErrorLog.ApiErrorLogVO) => {
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
