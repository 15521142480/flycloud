<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.pay.notify.NotifyDetail.k779398d0')"
    width="50%"
  >
    <el-descriptions :column="2">
      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.kf51b03a6')">
        <el-tag>{{ detailData.merchantOrderId }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.kcca88a70')">
        <dict-tag :type="DICT_TYPE.PAY_NOTIFY_STATUS" :value="detailData.status" />
      </el-descriptions-item>

      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.k396d9d78')">{{
        detailData.appId
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.k2d87d518')">{{
        detailData.appName
      }}</el-descriptions-item>

      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.k8913e0a8')">{{
        detailData.dataId
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.k1cf9ae8f')">
        <dict-tag :type="DICT_TYPE.PAY_NOTIFY_TYPE" :value="detailData.type" />
      </el-descriptions-item>

      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.kec63d1a8')">{{
        detailData.notifyTimes
      }}</el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.k036388c2')">
        {{ detailData.maxNotifyTimes }}
      </el-descriptions-item>

      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.ka8943cc4')">
        {{ formatDate(detailData.lastExecuteTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.kf9dac926')">
        {{ formatDate(detailData.nextNotifyTime) }}
      </el-descriptions-item>

      <el-descriptions-item :label="t('common.createTime')">
        {{ formatDate(detailData.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('common.updateTime')">
        {{ formatDate(detailData.updateTime) }}
      </el-descriptions-item>
    </el-descriptions>

    <!-- 分割线 -->
    <el-divider />

    <el-descriptions :column="1" direction="vertical" border>
      <el-descriptions-item :label="t('auto.views.pay.notify.NotifyDetail.kfd16b473')">
        <el-table :data="detailData.logs">
          <el-table-column
            :label="t('auto.views.pay.notify.NotifyDetail.k8cac83c8')"
            align="center"
            prop="id"
          />
          <el-table-column
            :label="t('auto.views.pay.notify.NotifyDetail.kcca88a70')"
            align="center"
            prop="status"
          >
            <template #default="scope">
              <dict-tag :type="DICT_TYPE.PAY_NOTIFY_STATUS" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column
            :label="t('auto.views.pay.notify.NotifyDetail.kec63d1a8')"
            align="center"
            prop="notifyTimes"
          />
          <el-table-column
            :label="t('extra.k3f6194f8')"
            align="center"
            prop="lastExecuteTime"
            width="180"
          >
            <template #default="scope">
              <span>{{ formatDate(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="t('extra.k15d5fffa')" align="center" prop="response" />
        </el-table>
      </el-descriptions-item>
    </el-descriptions>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import * as PayNotifyApi from '@/api/pay/notify'
import { formatDate } from '@/utils/formatTime'
const { t } = useI18n()
defineOptions({ name: 'PayNotifyDetail' })

const dialogVisible = ref(false) // 弹窗的是否展示
const detailLoading = ref(false) // 表单的加载中
const detailData = ref({})

/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  // 设置数据
  detailLoading.value = true
  try {
    detailData.value = await PayNotifyApi.getNotifyTaskDetail(id)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
</script>
