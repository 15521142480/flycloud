<template>
  <ReceivablePlanDetailsHeader v-loading="loading" :receivable-plan="receivablePlan">
    <el-button
      v-if="permissionListRef?.validateWrite"
      @click="openForm('update', receivablePlan.id)"
    >
      {{ t('extra.kbb16f31d') }}
    </el-button>
  </ReceivablePlanDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('auto.views.crm.receivable.plan.detail.index.kbf455584')">
        <ReceivablePlanDetailsInfo :receivable-plan="receivablePlan" />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.crm.receivable.plan.detail.index.kf4bc877c')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane :label="t('auto.views.crm.receivable.plan.detail.index.k7de0251f')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="receivablePlan.id!"
          :biz-type="BizTypeEnum.CRM_RECEIVABLE_PLAN"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>

  <!-- 表单弹窗：添加/修改 -->
  <ReceivablePlanForm ref="formRef" @success="getReceivablePlan(receivablePlan.id)" />
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import ReceivablePlanDetailsHeader from './ReceivablePlanDetailsHeader.vue'
import ReceivablePlanDetailsInfo from './ReceivablePlanDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue' // 团队成员列表（权限）
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import ReceivablePlanForm from '@/views/crm/receivable/plan/ReceivablePlanForm.vue'
const { t } = useI18n()
defineOptions({ name: 'CrmReceivablePlanDetail' })

const message = useMessage()

const receivablePlanId = ref(0) // 回款计划编号
const loading = ref(true) // 加载中
const receivablePlan = ref<ReceivablePlanApi.ReceivablePlanVO>(
  {} as ReceivablePlanApi.ReceivablePlanVO
) // 回款计划详情
const permissionListRef = ref<InstanceType<typeof PermissionList>>() // 团队成员列表 Ref

/** 获取详情 */
const getReceivablePlan = async (id: string) => {
  loading.value = true
  try {
    receivablePlan.value = await ReceivablePlanApi.getReceivablePlan(id)
    await getOperateLog(id)
  } finally {
    loading.value = false
  }
}

/** 编辑 */
const formRef = ref()
const openForm = (type: string, id?: string) => {
  formRef.value.open(type, id)
}

/** 获取操作日志 */
const logList = ref<OperateLogVO[]>([]) // 操作日志列表
const getOperateLog = async (receivablePlanId: string) => {
  if (!receivablePlanId) {
    return
  }
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_RECEIVABLE_PLAN,
    bizId: receivablePlanId
  })
  logList.value = data.list
}

/** 关闭窗口 */
const { delView } = useTagsViewStore() // 视图操作
const { currentRoute } = useRouter() // 路由
const close = () => {
  delView(unref(currentRoute))
}

/** 初始化 */
const { params } = useRoute()
onMounted(async () => {
  if (!params.id) {
    message.warning(t('auto.views.crm.receivable.plan.detail.index.k88095162'))
    close()
    return
  }
  receivablePlanId.value = params.id as unknown as number
  await getReceivablePlan(receivablePlanId.value)
})
</script>
