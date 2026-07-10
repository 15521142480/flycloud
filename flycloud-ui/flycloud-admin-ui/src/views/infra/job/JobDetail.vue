<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.infra.job.JobDetail.kcb1587a2')"
    width="700px"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.k017af56c')" min-width="60">
        {{ detailData.id }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.k2e304698')">
        {{ detailData.name }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.k2e304698')">
        <dict-tag :type="DICT_TYPE.INFRA_JOB_STATUS" :value="detailData.status" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.kec311980')">
        {{ detailData.handlerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.kddc1dd16')">
        {{ detailData.handlerParam }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.k6859f82a')">
        {{ detailData.cronExpression }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.k7a35a7d3')">
        {{ detailData.retryCount }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.kf86d661b')">{{
        detailData.retryInterval + t('extra.k1f719018')
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.k4bdf128c')">
        {{
          detailData.monitorTimeout > 0
            ? detailData.monitorTimeout + t('extra.k1f719018')
            : t('extra.kf2b39a25')
        }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.infra.job.JobDetail.kb921a663')">
        <el-timeline>
          <el-timeline-item
            v-for="(nextTime, index) in nextTimes"
            :key="index"
            :timestamp="formatDate(nextTime)"
            >{{ t('extra.kfda3d6b1', { p0: index + 1 }) }}</el-timeline-item
          >
        </el-timeline>
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import * as JobApi from '@/api/infra/job'
const { t } = useI18n()
defineOptions({ name: 'InfraJobDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({} as JobApi.JobVO) // 详情数据
const nextTimes = ref([]) // 下一轮执行时间的数组

/** 打开弹窗 */
const open = async (id: string) => {
  dialogVisible.value = true
  // 查看，设置数据
  if (id) {
    detailLoading.value = true
    try {
      detailData.value = await JobApi.getJob(id)
      // 获取下一次执行时间
      nextTimes.value = await JobApi.getJobNextTimes(id)
    } finally {
      detailLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
