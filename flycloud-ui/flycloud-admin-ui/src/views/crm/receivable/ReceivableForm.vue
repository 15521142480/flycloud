<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.receivable.ReceivableForm.k89c860f8')" prop="no">
            <el-input
              v-model="formData.no"
              disabled
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.kf914a47d')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.ReceivableForm.k974d383f')"
            prop="ownerUserId"
          >
            <el-select
              v-model="formData.ownerUserId"
              :disabled="formType !== 'create'"
              class="w-1/1"
            >
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.ReceivableForm.ke941d410')"
            prop="customerId"
          >
            <el-select
              v-model="formData.customerId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              filterable
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.k6bdb05d6')"
              @change="handleCustomerChange"
            >
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.ReceivableForm.kb8fbf277')"
            prop="contractId"
          >
            <el-select
              v-model="formData.contractId"
              :disabled="formType !== 'create' || !formData.customerId"
              class="w-1/1"
              filterable
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.kde1ab089')"
              @change="handleContractChange"
            >
              <el-option
                v-for="data in contractList"
                :key="data.id"
                :disabled="data.auditStatus !== 20"
                :label="data.name"
                :value="data.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.ReceivableForm.kcaff0fdf')"
            prop="planId"
          >
            <el-select
              v-model="formData.planId"
              :disabled="formType !== 'create' || !formData.contractId"
              class="!w-1/1"
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.k07180071')"
              @change="handleReceivablePlanChange"
            >
              <el-option
                v-for="data in receivablePlanList"
                :key="data.id"
                :disabled="data.receivableId"
                :label="t('extra.k50cb6af8') + data.period + t('extra.k6289ac17')"
                :value="data.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.ReceivableForm.k6c0d9ca0')"
            prop="returnType"
          >
            <el-select
              v-model="formData.returnType"
              class="w-1/1"
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.kc813ab05')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_RECEIVABLE_RETURN_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.ReceivableForm.kc60d7ff5')"
            prop="price"
          >
            <el-input-number
              v-model="formData.price"
              :min="0.01"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.k43183e2c')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.ReceivableForm.kd0132503')"
            prop="returnTime"
          >
            <el-date-picker
              v-model="formData.returnTime"
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.kc7d198c1')"
              type="date"
              value-format="x"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('common.remark')" prop="remark">
            <el-input
              v-model="formData.remark"
              :placeholder="t('auto.views.crm.receivable.ReceivableForm.k57e709d9')"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.crm.receivable.ReceivableForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.receivable.ReceivableForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
// @ts-nocheck
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import * as ReceivableApi from '@/api/crm/receivable'
import { ReceivableVO } from '@/api/crm/receivable'
import * as UserApi from '@/api/system/user'
import * as CustomerApi from '@/api/crm/customer'
import * as ContractApi from '@/api/crm/contract'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
const { t } = useI18n()
const message = useMessage() // 消息弹窗
const userOptions = ref<UserApi.UserVO[]>([]) // 用户列表
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref<ReceivableApi.ReceivableVO>({} as ReceivableApi.ReceivableVO)
const formRules = reactive({
  customerId: [
    {
      required: true,
      message: t('auto.views.crm.receivable.ReceivableForm.k920199e1'),
      trigger: 'blur'
    }
  ],
  contractId: [
    {
      required: true,
      message: t('auto.views.crm.receivable.ReceivableForm.k0772264d'),
      trigger: 'blur'
    }
  ],
  returnTime: [
    {
      required: true,
      message: t('auto.views.crm.receivable.ReceivableForm.k0eaf1600'),
      trigger: 'blur'
    }
  ],
  price: [
    {
      required: true,
      message: t('auto.views.crm.receivable.ReceivableForm.k01be6f58'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const customerList = ref<CustomerApi.CustomerVO[]>([]) // 客户列表
const contractList = ref<ContractApi.ContractVO[]>([]) // 合同列表
const receivablePlanList = ref<ReceivablePlanApi.ReceivablePlanVO[]>([]) // 回款计划列表

/** 打开弹窗 */
const open = async (
  type: string,
  id?: string,
  receivablePlan?: ReceivablePlanApi.ReceivablePlanVO
) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = (await ReceivableApi.getReceivable(id)) as ReceivableVO
      formData.value = data
      await handleCustomerChange(data.customerId!)
      formData.value.contractId = data?.contract?.id
    } finally {
      formLoading.value = false
    }
  }
  // 获得用户列表
  userOptions.value = await UserApi.getSimpleUserList()
  // 获得客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
  // 默认新建时选中自己
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
  }
  // 从回款计划创建回款
  if (receivablePlan) {
    formData.value.customerId = receivablePlan.customerId
    await handleCustomerChange(receivablePlan.customerId)
    formData.value.contractId = receivablePlan.contractId
    await handleContractChange(receivablePlan.contractId)
    if (receivablePlan.id) {
      formData.value.planId = receivablePlan.id
      formData.value.price = receivablePlan.price
      formData.value.returnType = receivablePlan.returnType
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
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ReceivableApi.ReceivableVO
    if (formType.value === 'create') {
      await ReceivableApi.createReceivable(data)
      message.success(t('common.createSuccess'))
    } else {
      await ReceivableApi.updateReceivable(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {} as ReceivableApi.ReceivableVO
  formRef.value?.resetFields()
}

/** 处理切换客户 */
const handleCustomerChange = async (customerId: string) => {
  // 重置合同编号
  formData.value.contractId = undefined
  // 获得合同列表
  if (customerId) {
    contractList.value = []
    contractList.value = await ContractApi.getContractSimpleList(customerId)
  }
}

/** 处理切换合同 */
const handleContractChange = async (contractId: string) => {
  // 重置回款计划编号
  formData.value.planId = undefined
  if (contractId) {
    // 获得回款计划列表
    receivablePlanList.value = []
    receivablePlanList.value = await ReceivablePlanApi.getReceivablePlanSimpleList(
      formData.value.customerId!,
      contractId
    )
    // 设置金额
    const contract = contractList.value.find((item) => item.id === contractId)
    if (contract) {
      formData.value.price = contract.totalPrice - contract.totalReceivablePrice
    }
  }
}

/** 处理切换回款计划 */
const handleReceivablePlanChange = (planId: string) => {
  if (!planId) {
    return
  }
  const receivablePlan = receivablePlanList.value.find((item) => item.id === planId)
  if (!receivablePlan) {
    return
  }
  formData.value.price = receivablePlan.price
  formData.value.returnType = receivablePlan.returnType
}
</script>
