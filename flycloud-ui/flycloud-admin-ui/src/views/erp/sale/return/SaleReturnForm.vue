<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1440">
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
          <el-form-item label="退货单号" prop="no">
            <el-input disabled v-model="formData.no" placeholder="保存时自动生成" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="退货时间" prop="returnTime">
            <el-date-picker
              v-model="formData.returnTime"
              type="date"
              value-format="x"
              placeholder="选择退货时间"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="关联订单" prop="orderNo">
            <el-input v-model="formData.orderNo" readonly>
              <template #append>
                <el-button @click="openSaleOrderReturnEnableList">
                  <Icon icon="ep:search" /> 选择
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="客户" prop="customerId">
            <el-select
              v-model="formData.customerId"
              clearable
              filterable
              disabled
              placeholder="请选择客户"
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
        <el-col :span="8">
          <el-form-item label="销售人员" prop="saleUserId">
            <el-select
              v-model="formData.saleUserId"
              clearable
              filterable
              placeholder="请选择销售人员"
              class="!w-1/1"
            >
              <el-option
                v-for="item in userList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item label="备注" prop="remark">
            <el-input
              type="textarea"
              v-model="formData.remark"
              :rows="1"
              placeholder="请输入备注"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="附件" prop="fileUrl">
            <UploadFile :is-show-tip="false" v-model="formData.fileUrl" :limit="1" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 子表的表单 -->
      <ContentWrap>
        <el-tabs v-model="subTabsName" class="-mt-15px -mb-10px">
          <el-tab-pane label="退货产品清单" name="item">
            <SaleReturnItemForm ref="itemFormRef" :items="formData.items" :disabled="disabled" />
          </el-tab-pane>
        </el-tabs>
      </ContentWrap>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="优惠率（%）" prop="discountPercent">
            <el-input-number
              v-model="formData.discountPercent"
              controls-position="right"
              :min="0"
              :precision="2"
              placeholder="请输入优惠率"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="退款优惠" prop="discountPrice">
            <el-input
              disabled
              v-model="formData.discountPrice"
              :formatter="erpPriceInputFormatter"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="优惠后金额">
            <el-input
              disabled
              :model-value="formData.totalPrice - formData.otherPrice"
              :formatter="erpPriceInputFormatter"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="其它费用" prop="otherPrice">
            <el-input-number
              v-model="formData.otherPrice"
              controls-position="right"
              :min="0"
              :precision="2"
              placeholder="请输入其它费用"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="结算账户" prop="accountId">
            <el-select
              v-model="formData.accountId"
              clearable
              filterable
              placeholder="请选择结算账户"
              class="!w-1/1"
            >
              <el-option
                v-for="item in accountList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="应退金额" prop="totalPrice">
            <el-input disabled v-model="formData.totalPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading" v-if="!disabled">
        确 定
      </el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>

  <!-- 可退货的订单列表 -->
  <SaleOrderReturnEnableList ref="saleOrderReturnEnableListRef" @success="handleSaleOrderChange" />
</template>
<script setup lang="ts">
import { SaleReturnApi, SaleReturnVO } from '@/api/erp/sale/return'
import SaleReturnItemForm from './components/SaleReturnItemForm.vue'
import { CustomerApi, CustomerVO } from '@/api/erp/sale/customer'
import { AccountApi, AccountVO } from '@/api/erp/finance/account'
import { erpPriceInputFormatter, erpPriceMultiply } from '@/utils'
import SaleOrderReturnEnableList from '@/views/erp/sale/order/components/SaleOrderReturnEnableList.vue'
import { SaleOrderVO } from '@/api/erp/sale/order'
import * as UserApi from '@/api/system/user'

/** ERP 销售退货表单 */
defineOptions({ name: 'SaleReturnForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改；detail - 详情
const formData = ref({
  id: undefined,
  customerId: undefined,
  accountId: undefined,
  saleUserId: undefined,
  returnTime: undefined,
  remark: undefined,
  fileUrl: '',
  discountPercent: 0,
  discountPrice: 0,
  totalPrice: 0,
  otherPrice: 0,
  orderNo: undefined,
  items: [],
  no: undefined // 退货单号，后端返回
})
const formRules = reactive({
  customerId: [{ required: true, message: '客户不能为空', trigger: 'blur' }],
  returnTime: [{ required: true, message: '退货时间不能为空', trigger: 'blur' }]
})
const disabled = computed(() => formType.value === 'detail')
const formRef = ref() // 表单 Ref
const customerList = ref<CustomerVO[]>([]) // 客户列表
const accountList = ref<AccountVO[]>([]) // 账户列表
const userList = ref<UserApi.UserVO[]>([]) // 用户列表

/** 子表的表单 */
const subTabsName = ref('item')
const itemFormRef = ref()

/** 计算 discountPrice、totalPrice 价格 */
watch(
  () => formData.value,
  (val) => {
    if (!val) {
      return
    }
    // 计算
    const totalPrice = val.items.reduce((prev, curr) => prev + curr.totalPrice, 0)
    const discountPrice =
      val.discountPercent != null ? erpPriceMultiply(totalPrice, val.discountPercent / 100.0) : 0
    formData.value.totalPrice = totalPrice - discountPrice + val.otherPrice
  },
  { deep: true }
)

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
      formData.value = await SaleReturnApi.getSaleReturn(id)
    } finally {
      formLoading.value = false
    }
  }
  // 加载客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
  // 加载用户列表
  userList.value = await UserApi.getSimpleUserList()
  // 加载账户列表
  accountList.value = await AccountApi.getAccountSimpleList()
  const defaultAccount = accountList.value.find((item) => item.defaultStatus)
  if (defaultAccount) {
    formData.value.accountId = defaultAccount.id
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 打开【可退货的订单列表】弹窗 */
const saleOrderReturnEnableListRef = ref() // 可退货的订单列表 Ref
const openSaleOrderReturnEnableList = () => {
  saleOrderReturnEnableListRef.value.open()
}

const handleSaleOrderChange = (order: SaleOrderVO) => {
  // 将订单设置到退货单
  formData.value.orderId = order.id
  formData.value.orderNo = order.no
  formData.value.customerId = order.customerId
  formData.value.accountId = order.accountId
  formData.value.saleUserId = order.saleUserId
  formData.value.discountPercent = order.discountPercent
  formData.value.remark = order.remark
  formData.value.fileUrl = order.fileUrl
  // 将订单项设置到退货单项
  order.items.forEach((item) => {
    item.count = item.outCount - item.returnCount
    item.orderItemId = item.id
    item.id = undefined
  })
  formData.value.items = order.items.filter((item) => item.count > 0)
}

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  await itemFormRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as SaleReturnVO
    if (formType.value === 'create') {
      await SaleReturnApi.createSaleReturn(data)
      message.success(t('common.createSuccess'))
    } else {
      await SaleReturnApi.updateSaleReturn(data)
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
    accountId: undefined,
    saleUserId: undefined,
    returnTime: undefined,
    remark: undefined,
    fileUrl: undefined,
    discountPercent: 0,
    discountPrice: 0,
    totalPrice: 0,
    otherPrice: 0,
    items: []
  }
  formRef.value?.resetFields()
}
</script>
