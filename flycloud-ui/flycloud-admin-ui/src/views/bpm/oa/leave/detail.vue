<template>
  <el-card v-loading="detailLoading" class="leave-detail-card" shadow="never">
    <template #header>
      <div class="leave-detail-header">
        <span class="leave-detail-title">请假详情</span>
      </div>
    </template>

    <div class="leave-detail-body">
      <el-form class="leave-detail-form" label-width="90px">
        <el-form-item :label="t('auto.views.bpm.oa.leave.detail.k1509ea5a')">
          <el-select v-model="detailData.type" class="leave-form-control" disabled>
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.BPM_OA_LEAVE_TYPE)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('auto.views.bpm.oa.leave.detail.ke8868af6')">
          <el-input
            class="leave-form-control"
            :model-value="formatDate(detailData.startTime)"
            readonly
          />
        </el-form-item>
        <el-form-item :label="t('auto.views.bpm.oa.leave.detail.ka0bb9f49')">
          <el-input class="leave-form-control" :model-value="formatDate(detailData.endTime)" readonly />
        </el-form-item>
        <el-form-item :label="t('auto.views.bpm.oa.leave.detail.k1ff9c3d0')">
          <el-input
            class="leave-form-control"
            :model-value="detailData.reason"
            :rows="5"
            readonly
            type="textarea"
          />
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { propTypes } from '@/utils/propTypes'
import * as LeaveApi from '@/api/bpm/leave'
const { t } = useI18n()
defineOptions({ name: 'BpmOALeaveDetail' })

const { query } = useRoute() // 查询参数

const props = defineProps({
  id: propTypes.string.def(undefined)
})
const detailLoading = ref(false) // 表单的加载中
const detailData = ref<any>({}) // 详情数据
const queryId = String(query.id) // 从 URL 传递过来的 id 编号

/** 获得数据 */
const getInfo = async () => {
  detailLoading.value = true
  try {
    detailData.value = await LeaveApi.getLeave(props.id || queryId)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open: getInfo }) // 提供 open 方法，用于打开弹窗

/** 初始化 **/
onMounted(() => {
  getInfo()
})
</script>

<style lang="scss" scoped>
.leave-detail-card {
  max-width: 920px;
  margin: 24px auto;

  :deep(.el-card__header) {
    padding: 16px 24px;
  }
}

.leave-detail-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.leave-detail-header {
  text-align: center;
}

.leave-detail-body {
  display: flex;
  justify-content: center;
  padding: 12px 0 4px;
}

.leave-detail-form {
  width: 620px;
  max-width: 100%;
}

.leave-form-control {
  width: 100%;
}
</style>
