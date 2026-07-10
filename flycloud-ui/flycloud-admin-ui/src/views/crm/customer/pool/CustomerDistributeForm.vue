<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.crm.customer.pool.CustomerDistributeForm.k01ae1bb2')"
  >
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item
        :label="t('auto.views.crm.customer.pool.CustomerDistributeForm.k974d383f')"
        prop="ownerUserId"
      >
        <el-select v-model="formData.ownerUserId" class="w-1/1">
          <el-option
            v-for="item in userOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.crm.customer.pool.CustomerDistributeForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.customer.pool.CustomerDistributeForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as CustomerApi from '@/api/crm/customer'
import * as UserApi from '@/api/system/user'
import { distributeCustomer } from '@/api/crm/customer'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const userOptions = ref<UserApi.UserVO[]>([]) // 用户列表
const formData = ref({
  id: undefined,
  ownerUserId: undefined
})
const formRules = reactive({
  ownerUserId: [
    {
      required: true,
      message: t('auto.views.crm.customer.pool.CustomerDistributeForm.kc40a3652'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (id: string) => {
  dialogVisible.value = true
  resetForm()
  formData.value.id = id
  // 获得用户列表
  userOptions.value = await UserApi.getSimpleUserList()
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
    await CustomerApi.distributeCustomer([formData.value.id], formData.value.ownerUserId)
    message.success(t('auto.views.crm.customer.pool.CustomerDistributeForm.k014df8b1'))
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
    ownerUserId: undefined
  }
  formRef.value?.resetFields()
}
</script>
