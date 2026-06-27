<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="110px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k471b88ce')"
            prop="period"
          >
            <el-input
              v-model="formData.period"
              disabled
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.kf914a47d')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k974d383f')"
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
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.ke941d410')"
            prop="customerId"
          >
            <el-select
              v-model="formData.customerId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              filterable
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k6bdb05d6')"
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
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.kb8fbf277')"
            prop="contractId"
          >
            <el-select
              v-model="formData.contractId"
              :disabled="formType !== 'create' || !formData.customerId"
              class="w-1/1"
              filterable
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.kde1ab089')"
            >
              <el-option
                v-for="data in contractList"
                :key="data.id"
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
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k2eaffb78')"
            prop="price"
          >
            <el-input-number
              v-model="formData.price"
              :min="0.01"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k6bd326d8')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.kf5888bd3')"
            prop="returnTime"
          >
            <el-date-picker
              v-model="formData.returnTime"
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k7fdee07e')"
              type="date"
              value-format="x"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k4eb4f0ab')"
            prop="remindDays"
          >
            <el-input-number
              v-model="formData.remindDays"
              class="!w-100%"
              controls-position="right"
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k17943b00')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k6c0d9ca0')"
            prop="returnType"
          >
            <el-select
              v-model="formData.returnType"
              class="w-1/1"
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.kc813ab05')"
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
        <el-col :span="24">
          <el-form-item :label="t('common.remark')" prop="remark">
            <el-input
              v-model="formData.remark"
              :placeholder="t('auto.views.crm.receivable.plan.ReceivablePlanForm.k57e709d9')"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.crm.receivable.plan.ReceivablePlanForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.receivable.plan.ReceivablePlanForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import * as UserApi from '@/api/system/user'
import * as CustomerApi from '@/api/crm/customer'
import * as ContractApi from '@/api/crm/contract'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { cloneDeep } from 'lodash-es'
const { t } = useI18n()
const message = useMessage() // 消息弹窗
const userOptions = ref<UserApi.UserVO[]>([]) // 用户列表
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref<ReceivablePlanApi.ReceivablePlanVO>({} as ReceivablePlanApi.ReceivablePlanVO)
const formRules = reactive({
  price: [
    {
      required: true,
      message: t('auto.views.crm.receivable.plan.ReceivablePlanForm.kf2e492e5'),
      trigger: 'blur'
    }
  ],
  returnTime: [
    {
      required: true,
      message: t('auto.views.crm.receivable.plan.ReceivablePlanForm.kcaa605a1'),
      trigger: 'blur'
    }
  ],
  customerId: [
    {
      required: true,
      message: t('auto.views.crm.receivable.plan.ReceivablePlanForm.kef114dde'),
      trigger: 'blur'
    }
  ],
  contractId: [
    {
      required: true,
      message: t('auto.views.crm.receivable.plan.ReceivablePlanForm.kbee5d669'),
      trigger: 'blur'
    }
  ],
  ownerUserId: [
    {
      required: true,
      message: t('auto.views.crm.receivable.plan.ReceivablePlanForm.kc40a3652'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const customerList = ref<CustomerApi.CustomerVO[]>([]) // 客户列表
const contractList = ref<ContractApi.ContractVO[]>([]) // 合同列表

/** 打开弹窗 */
const open = async (type: string, id?: number, customerId?: number, contractId?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await ReceivablePlanApi.getReceivablePlan(id)
      formData.value = cloneDeep(data)
      await handleCustomerChange(data.customerId!)
      formData.value.contractId = data?.contractId
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
  // 设置 customerId 和 contractId 默认值
  if (customerId) {
    formData.value.customerId = customerId
    await handleCustomerChange(customerId)
  }
  if (contractId) {
    formData.value.contractId = contractId
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
    const data = formData.value as unknown as ReceivablePlanApi.ReceivablePlanVO
    if (formType.value === 'create') {
      await ReceivablePlanApi.createReceivablePlan(data)
      message.success(t('common.createSuccess'))
    } else {
      await ReceivablePlanApi.updateReceivablePlan(data)
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
  formData.value = {} as ReceivablePlanApi.ReceivablePlanVO
  formRef.value?.resetFields()
}

/** 处理切换客户 */
const handleCustomerChange = async (customerId: number) => {
  // 重置合同编号
  formData.value.contractId = undefined
  // 获得合同列表
  if (customerId) {
    contractList.value = []
    contractList.value = await ContractApi.getContractSimpleList(customerId)
  }
}
</script>
