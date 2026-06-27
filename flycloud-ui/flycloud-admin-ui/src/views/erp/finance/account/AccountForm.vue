<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.erp.finance.account.AccountForm.k1be7ae4f')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.erp.finance.account.AccountForm.kc2afb255')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.erp.finance.account.AccountForm.kc94faa71')" prop="no">
        <el-input
          v-model="formData.no"
          :placeholder="t('auto.views.erp.finance.account.AccountForm.k76745b52')"
        />
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.erp.finance.account.AccountForm.k57e709d9')"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
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
      <el-form-item :label="t('common.sort')" prop="sort">
        <el-input
          v-model="formData.sort"
          :placeholder="t('auto.views.erp.finance.account.AccountForm.k242d8da1')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.erp.finance.account.AccountForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.erp.finance.account.AccountForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { AccountApi, AccountVO } from '@/api/erp/finance/account'

/** ERP 结算 表单 */
const { t } = useI18n()
defineOptions({ name: 'AccountForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  no: undefined,
  remark: undefined,
  status: undefined,
  sort: undefined,
  defaultStatus: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.erp.finance.account.AccountForm.kca898456'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.erp.finance.account.AccountForm.k03991f81'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.erp.finance.account.AccountForm.k3218602a'),
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
      formData.value = await AccountApi.getAccount(id)
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
    const data = formData.value as unknown as AccountVO
    if (formType.value === 'create') {
      await AccountApi.createAccount(data)
      message.success(t('common.createSuccess'))
    } else {
      await AccountApi.updateAccount(data)
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
    no: undefined,
    remark: undefined,
    status: undefined,
    sort: undefined
  }
  formRef.value?.resetFields()
}
</script>
