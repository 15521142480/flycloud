<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.infra.job.logger.JobLogDetail.kcb1587a2')"
    width="700px"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item
        :label="t('auto.views.infra.job.logger.JobLogDetail.k8cac83c8')"
        min-width="60"
      >
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.k017af56c')">
        {{ detailData.jobId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.kec311980')">
        {{ detailData.handlerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.kddc1dd16')">
        {{ detailData.handlerParam }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.k038b052f')">
        {{ detailData.executeIndex }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.k68f17a6c')">
        {{ formatDate(detailData.beginTime) + ' ~ ' + formatDate(detailData.endTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.kf74f1dd2')">{{
        detailData.duration + t('extra.k46fc7bd3')
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.kb7d4128d')">
        <dict-tag :type="DICT_TYPE.INFRA_JOB_LOG_STATUS" :value="detailData.status" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.logger.JobLogDetail.k1b213f40')">
        {{ detailData.result }}
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as JobLogApi from '@/api/infra/jobLog'
const { t } = useI18n()
defineOptions({ name: 'JobLogDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({} as JobLogApi.JobLogVO) // 详情数据

/** 打开弹窗 */
const open = async (id: string) => {
  dialogVisible.value = true
  // 查看，设置数据
  if (id) {
    detailLoading.value = true
    try {
      detailData.value = await JobLogApi.getJobLog(id)
    } finally {
      detailLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
