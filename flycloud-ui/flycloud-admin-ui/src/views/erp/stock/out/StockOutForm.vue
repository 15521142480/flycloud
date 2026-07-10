<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
      :disabled="disabled"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('auto.views.erp.stock.out.StockOutForm.k76097d27')" prop="no">
            <el-input
              disabled
              v-model="formData.no"
              :placeholder="t('auto.views.erp.stock.out.StockOutForm.kf914a47d')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            :label="t('auto.views.erp.stock.out.StockOutForm.kd9a6d1b0')"
            prop="outTime"
          >
            <el-date-picker
              v-model="formData.outTime"
              type="date"
              value-format="x"
              :placeholder="t('auto.views.erp.stock.out.StockOutForm.k86df4f2c')"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            :label="t('auto.views.erp.stock.out.StockOutForm.kf2068706')"
            prop="customerId"
          >
            <el-select
              v-model="formData.customerId"
              clearable
              filterable
              :placeholder="t('auto.views.erp.stock.out.StockOutForm.k6bdb05d6')"
              class="!w-1/1"
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
        <el-col :span="16">
          <el-form-item :label="t('common.remark')" prop="remark">
            <el-input
              type="textarea"
              v-model="formData.remark"
              :rows="1"
              :placeholder="t('auto.views.erp.stock.out.StockOutForm.k57e709d9')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            :label="t('auto.views.erp.stock.out.StockOutForm.k99f6fe6c')"
            prop="fileUrl"
          >
            <UploadFile :is-show-tip="false" v-model="formData.fileUrl" directory="erp" :limit="1" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- 子表的表单 -->
    <ContentWrap>
      <el-tabs v-model="subTabsName" class="-mt-15px -mb-10px">
        <el-tab-pane :label="t('auto.views.erp.stock.out.StockOutForm.k8b3df882')" name="item">
          <StockOutItemForm ref="itemFormRef" :items="formData.items" :disabled="disabled" />
        </el-tab-pane>
      </el-tabs>
    </ContentWrap>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading" v-if="!disabled">
        {{ t('extra.kc6a00fe9') }}
      </el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.erp.stock.out.StockOutForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
// @ts-nocheck
import { StockOutApi, StockOutVO } from '@/api/erp/stock/out'
import StockOutItemForm from './components/StockOutItemForm.vue'
import { CustomerApi, CustomerVO } from '@/api/erp/sale/customer'

/** ERP 其它出库单表单 */
const { t } = useI18n()
defineOptions({ name: 'StockOutForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改；detail - 详情
const formData = ref({
  id: undefined,
  customerId: undefined,
  outTime: undefined,
  remark: undefined,
  fileUrl: '',
  items: []
})
const formRules = reactive({
  outTime: [
    {
      required: true,
      message: t('auto.views.erp.stock.out.StockOutForm.k2612cd85'),
      trigger: 'blur'
    }
  ]
})
const disabled = computed(() => formType.value === 'detail')
const formRef = ref() // 表单 Ref
const customerList = ref<CustomerVO[]>([]) // 客户列表

/** 子表的表单 */
const subTabsName = ref('item')
const itemFormRef = ref()

/** 打开弹窗 */
const open = async (type: string, id?: string) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await StockOutApi.getStockOut(id)
    } finally {
      formLoading.value = false
    }
  }
  // 加载客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  await itemFormRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as StockOutVO
    if (formType.value === 'create') {
      await StockOutApi.createStockOut(data)
      message.success(t('common.createSuccess'))
    } else {
      await StockOutApi.updateStockOut(data)
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
    customerId: undefined,
    outTime: undefined,
    remark: undefined,
    fileUrl: undefined,
    items: []
  }
  formRef.value?.resetFields()
}
</script>
