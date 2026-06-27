<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.k1be7ae4f')"
            prop="name"
          >
            <el-input
              v-model="formData.name"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.kc2afb255')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.k2425bd4b')"
            prop="contact"
          >
            <el-input
              v-model="formData.contact"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k9feabe27')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.k9f6834dc')"
            prop="mobile"
          >
            <el-input
              v-model="formData.mobile"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k4f0c3a4e')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.ke02f6e57')"
            prop="telephone"
          >
            <el-input
              v-model="formData.telephone"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k4597687d')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.ka7a315b8')"
            prop="email"
          >
            <el-input
              v-model="formData.email"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k704aa1ff')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.kd1dc1f81')"
            prop="fax"
          >
            <el-input
              v-model="formData.fax"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k6d91e7b3')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.k6bbda1b1')"
            prop="status"
          >
            <el-radio-group v-model="formData.status">
              <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('common.sort')" prop="sort">
            <el-input-number
              v-model="formData.sort"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k242d8da1')"
              class="!w-1/1"
              :precision="0"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.kc237c1b3')"
            prop="taxNo"
          >
            <el-input
              v-model="formData.taxNo"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.keb2373c8')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.k04ab4761')"
            prop="taxPercent"
          >
            <el-input-number
              v-model="formData.taxPercent"
              :min="0"
              :precision="2"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.kf3db9d64')"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.k48a64eb4')"
            prop="bankName"
          >
            <el-input
              v-model="formData.bankName"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k1b4c8d98')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.k0b959af0')"
            prop="bankAccount"
          >
            <el-input
              v-model="formData.bankAccount"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k9fad3c04')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.sale.customer.CustomerForm.ka7f215bd')"
            prop="bankAddress"
          >
            <el-input
              v-model="formData.bankAddress"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.kf298f36e')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="t('common.remark')" prop="remark">
            <el-input
              type="textarea"
              v-model="formData.remark"
              :placeholder="t('auto.views.erp.sale.customer.CustomerForm.k57e709d9')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.erp.sale.customer.CustomerForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.erp.sale.customer.CustomerForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { CustomerApi, CustomerVO } from '@/api/erp/sale/customer'
import { CommonStatusEnum } from '@/utils/constants'

/** ERP 客户 表单 */
const { t } = useI18n()
defineOptions({ name: 'CustomerForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  contact: undefined,
  mobile: undefined,
  telephone: undefined,
  email: undefined,
  fax: undefined,
  remark: undefined,
  status: undefined,
  sort: undefined,
  taxNo: undefined,
  taxPercent: undefined,
  bankName: undefined,
  bankAccount: undefined,
  bankAddress: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.erp.sale.customer.CustomerForm.k009abeb7'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.erp.sale.customer.CustomerForm.k03991f81'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.erp.sale.customer.CustomerForm.k3218602a'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await CustomerApi.getCustomer(id)
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
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as CustomerVO
    if (formType.value === 'create') {
      await CustomerApi.createCustomer(data)
      message.success(t('common.createSuccess'))
    } else {
      await CustomerApi.updateCustomer(data)
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
  formData.value = {
    id: undefined,
    name: undefined,
    contact: undefined,
    mobile: undefined,
    telephone: undefined,
    email: undefined,
    fax: undefined,
    remark: undefined,
    status: CommonStatusEnum.ENABLE,
    sort: undefined,
    taxNo: undefined,
    taxPercent: undefined,
    bankName: undefined,
    bankAccount: undefined,
    bankAddress: undefined
  }
  formRef.value?.resetFields()
}
</script>
