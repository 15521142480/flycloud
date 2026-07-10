<template>
  <Dialog
    :title="t('auto.views.crm.business.BusinessUpdateStatusForm.kca44e801')"
    v-model="dialogVisible"
    width="400"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="80px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.crm.business.BusinessUpdateStatusForm.kc0a20f9a')"
        prop="status"
      >
        <el-select
          v-model="formData.status"
          :placeholder="t('auto.views.crm.business.BusinessUpdateStatusForm.k01e5c356')"
          class="w-1/1"
        >
          <el-option
            v-for="item in statusList"
            :key="item.id"
            :label="item.name + t('extra.k43923921') + item.percent + '%)'"
            :value="item.id"
          />
          <el-option
            v-for="item in BusinessStatusApi.DEFAULT_STATUSES"
            :key="item.endStatus"
            :label="item.name + t('extra.k43923921') + item.percent + '%)'"
            :value="-item.endStatus"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.crm.business.BusinessUpdateStatusForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.business.BusinessUpdateStatusForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
// @ts-nocheck
import * as BusinessApi from '@/api/crm/business'
import * as BusinessStatusApi from '@/api/crm/business/status'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const formData = ref({
  id: undefined,
  statusId: undefined,
  endStatus: undefined,
  status: undefined
})
const formRules = reactive({
  status: [
    {
      required: true,
      message: t('auto.views.crm.business.BusinessUpdateStatusForm.k11298f9b'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const statusList = ref([]) // 商机状态列表

/** 打开弹窗 */
const open = async (business: BusinessApi.BusinessVO) => {
  dialogVisible.value = true
  resetForm()
  formData.value = {
    id: business.id,
    statusId: business.statusId,
    endStatus: business.endStatus,
    status: business.endStatus != null ? -business.endStatus : business.statusId
  }
  // 加载状态列表
  formLoading.value = true
  try {
    statusList.value = await BusinessStatusApi.getBusinessStatusSimpleList(business.statusTypeId)
  } finally {
    formLoading.value = false
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    await BusinessApi.updateBusinessStatus({
      id: formData.value.id,
      statusId: formData.value.status > 0 ? formData.value.status : undefined,
      endStatus: formData.value.status < 0 ? -formData.value.status : undefined
    })
    message.success(t('auto.views.crm.business.BusinessUpdateStatusForm.k37f656ba'))
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    statusId: undefined,
    endStatus: undefined,
    status: undefined
  }
  formRef.value?.resetFields()
}
</script>
