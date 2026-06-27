<template>
  <Dialog
    v-model="dialogVisible"
    :title="t('auto.views.member.user.components.UserBalanceUpdateForm.k5111941e')"
    width="600"
  >
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="130px"
    >
      <el-form-item
        :label="t('auto.views.member.user.components.UserBalanceUpdateForm.kec750ef6')"
        prop="id"
      >
        <el-input v-model="formData.id" class="!w-240px" disabled />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.components.UserBalanceUpdateForm.k90542e0a')"
        prop="name"
      >
        <el-input v-model="formData.name" class="!w-240px" disabled />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.components.UserBalanceUpdateForm.k1ba6c9e6')"
        prop="balance"
      >
        <el-input :model-value="formData.balance" class="!w-240px" disabled />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.components.UserBalanceUpdateForm.k85e327a1')"
        prop="changeType"
      >
        <el-radio-group v-model="formData.changeType">
          <el-radio :label="1">{{
            t('auto.views.member.user.components.UserBalanceUpdateForm.k605806b6')
          }}</el-radio>
          <el-radio :label="-1">{{
            t('auto.views.member.user.components.UserBalanceUpdateForm.kfe43f157')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.member.user.components.UserBalanceUpdateForm.kf49635d8')"
        prop="changeBalance"
      >
        <el-input-number
          v-model="formData.changeBalance"
          :min="0"
          :precision="2"
          :step="0.1"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.components.UserBalanceUpdateForm.kd3370a49')">
        <el-input :model-value="balanceResult" class="!w-240px" disabled />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.member.user.components.UserBalanceUpdateForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.member.user.components.UserBalanceUpdateForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as UserApi from '@/api/member/user'
import * as WalletApi from '@/api/pay/wallet/balance'
import { convertToInteger, formatToFraction } from '@/utils'

/** 修改用户余额表单 */
const { t } = useI18n()
defineOptions({ name: 'UpdateBalanceForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined,
  name: undefined,
  balance: '0',
  changeBalance: 0,
  changeType: 1
})
const formRules = reactive({
  changeBalance: [
    {
      required: true,
      message: t('auto.views.member.user.components.UserBalanceUpdateForm.kb6dc6bc9'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (id?: number) => {
  dialogVisible.value = true
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const user = await UserApi.getUser(id)
      const wallet = await WalletApi.getWallet({ userId: user.id || 0 })
      formData.value.id = user.id
      formData.value.name = user.name
      formData.value.balance = formatToFraction(wallet.balance)
      formData.value.changeType = 1 // 默认增加余额
      formData.value.changeBalance = 0 // 变动余额默认0
    } finally {
      formLoading.value = false
    }
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

  if (formData.value.changeBalance <= 0) {
    message.error(t('auto.views.member.user.components.UserBalanceUpdateForm.k397a6b8d'))
    return
  }
  if (convertToInteger(balanceResult.value) < 0) {
    message.error(t('auto.views.member.user.components.UserBalanceUpdateForm.k75496128'))
    return
  }

  // 提交请求
  formLoading.value = true
  try {
    await WalletApi.updateWalletBalance({
      userId: formData.value.id,
      balance: convertToInteger(formData.value.changeBalance) * formData.value.changeType
    })

    message.success(t('common.updateSuccess'))
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
    name: undefined,
    balance: '0',
    changeBalance: 0,
    changeType: 1
  }
  formRef.value?.resetFields()
}

/** 变动后的余额 */
const balanceResult = computed(() =>
  formatToFraction(
    convertToInteger(formData.value.balance) +
      convertToInteger(formData.value.changeBalance) * formData.value.changeType
  )
)
</script>
