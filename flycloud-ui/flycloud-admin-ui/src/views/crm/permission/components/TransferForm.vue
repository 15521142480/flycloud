<!-- 转移数据的表单弹窗，目前主要用于 CRM 客户、商机等详情界面 -->
<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="30%">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="150px"
    >
      <el-form-item
        :label="t('auto.views.crm.permission.components.TransferForm.k48815c2c')"
        prop="newOwnerUserId"
      >
        <el-select v-model="formData.newOwnerUserId">
          <el-option
            v-for="item in userOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.crm.permission.components.TransferForm.k6c6c2af1')">
        <el-radio-group v-model="oldOwnerHandler" @change="handleOwnerChange">
          <el-radio :value="false" size="large">{{
            t('auto.views.crm.permission.components.TransferForm.k2f752c00')
          }}</el-radio>
          <el-radio :value="true" size="large">{{
            t('auto.views.crm.permission.components.TransferForm.kce28cea8')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="oldOwnerHandler"
        :label="t('auto.views.crm.permission.components.TransferForm.k57239086')"
        prop="oldOwnerPermissionLevel"
      >
        <el-radio-group v-model="formData.oldOwnerPermissionLevel">
          <template
            v-for="dict in getIntDictOptions(DICT_TYPE.CRM_PERMISSION_LEVEL)"
            :key="dict.value"
          >
            <el-radio v-if="dict.value != PermissionLevelEnum.OWNER" :value="dict.value">
              {{ dict.label }}
            </el-radio>
          </template>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="bizType === BizTypeEnum.CRM_CUSTOMER" :label="t('extra.k67fe97ea')">
        <el-checkbox-group v-model="formData.toBizTypes">
          <el-checkbox :value="BizTypeEnum.CRM_CONTACT">{{
            t('auto.views.crm.business.detail.index.k2425bd4b')
          }}</el-checkbox>
          <el-checkbox :value="BizTypeEnum.CRM_BUSINESS">{{
            t('auto.views.crm.contact.detail.index.kc4beee85')
          }}</el-checkbox>
          <el-checkbox :value="BizTypeEnum.CRM_CONTRACT">{{
            t('auto.views.crm.business.detail.index.k59404d40')
          }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('extra.k008b8fcb')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as UserApi from '@/api/system/user'
import * as BusinessApi from '@/api/crm/business'
import * as ClueApi from '@/api/crm/clue'
import * as ContactApi from '@/api/crm/contact'
import * as CustomerApi from '@/api/crm/customer'
import * as ContractApi from '@/api/crm/contract'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { BizTypeEnum, PermissionLevelEnum, TransferReqVO } from '@/api/crm/permission'
const { t } = useI18n()
defineOptions({ name: 'CrmTransferForm' })

const props = defineProps<{
  bizType: number
}>()

const message = useMessage() // 消息弹窗
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const userOptions = ref<UserApi.UserVO[]>([]) // 用户列表
const oldOwnerHandler = ref(false) // 老负责人的处理方式
const formData = ref<TransferReqVO>({} as TransferReqVO)
const formRules = reactive({
  newOwnerUserId: [
    {
      required: true,
      message: t('auto.views.crm.permission.components.TransferForm.k3823a21a'),
      trigger: 'blur'
    }
  ],
  oldOwnerPermissionLevel: [
    {
      required: true,
      message: t('auto.views.crm.permission.components.TransferForm.kb6ec39f5'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (bizId: number) => {
  dialogVisible.value = true
  dialogTitle.value = getDialogTitle()
  resetForm()
  formData.value.id = bizId
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗
// 老负责人负责方式
const handleOwnerChange = (val: boolean) => {
  if (!val) {
    // 移除的话提交不带 oldOwnerPermissionLevel 参数
    formData.value.oldOwnerPermissionLevel = undefined
  }
}
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
    const data = formData.value
    await transfer(unref(data))
    message.success(dialogTitle.value + t('common.success'))
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}
const transfer = async (data: TransferReqVO) => {
  switch (props.bizType) {
    case BizTypeEnum.CRM_CLUE:
      return await ClueApi.transferClue(data)
    case BizTypeEnum.CRM_CUSTOMER:
      return await CustomerApi.transferCustomer(data)
    case BizTypeEnum.CRM_CONTACT:
      return await ContactApi.transferContact(data)
    case BizTypeEnum.CRM_BUSINESS:
      return await BusinessApi.transferBusiness(data)
    case BizTypeEnum.CRM_CONTRACT:
      return await ContractApi.transferContract(data)
    default:
      message.error(t('auto.views.crm.permission.components.TransferForm.k26a92b2a'))
      throw new Error(t('auto.views.crm.permission.components.TransferForm.k26a92b2a'))
  }
}
const getDialogTitle = () => {
  switch (props.bizType) {
    case BizTypeEnum.CRM_CLUE:
      return t('auto.views.crm.permission.components.TransferForm.k6bb9168c')
    case BizTypeEnum.CRM_CUSTOMER:
      return t('auto.views.crm.permission.components.TransferForm.kbe1ca1b5')
    case BizTypeEnum.CRM_CONTACT:
      return t('auto.views.crm.permission.components.TransferForm.kf1a1fe35')
    case BizTypeEnum.CRM_BUSINESS:
      return t('auto.views.crm.permission.components.TransferForm.k1abd432f')
    case BizTypeEnum.CRM_CONTRACT:
      return t('auto.views.crm.permission.components.TransferForm.k532b1bf1')
    default:
      return t('auto.views.crm.permission.components.TransferForm.k25e7ae11')
  }
}

/** 重置表单 */
const resetForm = () => {
  formRef.value?.resetFields()
  formData.value = {} as TransferReqVO
}
onMounted(async () => {
  // 获得用户列表
  userOptions.value = await UserApi.getSimpleUserList()
})
</script>
