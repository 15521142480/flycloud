<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.product.ProductForm.kabc0ac79')" prop="name">
            <el-input
              v-model="formData.name"
              :placeholder="t('auto.views.crm.product.ProductForm.k8d76e9ae')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.product.ProductForm.k974d383f')"
            prop="ownerUserId"
          >
            <el-select
              v-model="formData.ownerUserId"
              :placeholder="t('auto.views.crm.product.ProductForm.k8608d655')"
              :disabled="formData.id"
              class="w-1/1"
            >
              <el-option
                v-for="user in userList"
                :key="user.id"
                :label="user.name"
                :value="user.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.product.ProductForm.k93b6f015')"
            prop="categoryId"
          >
            <el-cascader
              v-model="formData.categoryId"
              :options="productCategoryList"
              :props="defaultProps"
              class="w-1/1"
              clearable
              :placeholder="t('auto.views.crm.product.ProductForm.k9c73803c')"
              filterable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.product.ProductForm.k298ff4b9')" prop="unit">
            <el-select
              v-model="formData.unit"
              class="w-1/1"
              :placeholder="t('auto.views.crm.product.ProductForm.kb6a42312')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_PRODUCT_UNIT)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.product.ProductForm.k440ef986')" prop="no">
            <el-input
              v-model="formData.no"
              :placeholder="t('auto.views.crm.product.ProductForm.kc15c5c05')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.product.ProductForm.kb70c2d28')" prop="price">
            <el-input-number
              v-model="formData.price"
              :placeholder="t('auto.views.crm.product.ProductForm.k83b1aa54')"
              :min="0"
              :precision="2"
              :step="0.1"
              class="w-full!"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.crm.product.ProductForm.k03917006')"
            prop="description"
          >
            <el-input
              v-model="formData.description"
              :placeholder="t('auto.views.crm.product.ProductForm.k886d8ca8')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('auto.views.crm.product.ProductForm.k461446e9')" prop="status">
            <el-select
              v-model="formData.status"
              :placeholder="t('auto.views.crm.product.ProductForm.kdba277df')"
              class="w-1/1"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_PRODUCT_STATUS)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.crm.product.ProductForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.crm.product.ProductForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as ProductApi from '@/api/crm/product'
import * as ProductCategoryApi from '@/api/crm/product/category'
import { defaultProps, handleTree } from '@/utils/tree'
import { getSimpleUserList, UserVO } from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
const { t } = useI18n()
defineOptions({ name: 'CrmProductForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const userId = useUserStore().getUser.id // 当前登录的编号
const formData = ref({
  id: undefined,
  name: undefined,
  no: undefined,
  unit: undefined,
  price: Number(undefined),
  status: undefined,
  categoryId: undefined,
  description: undefined,
  ownerUserId: -1
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.crm.product.ProductForm.kcbfa98d4'), trigger: 'blur' }
  ],
  no: [
    { required: true, message: t('auto.views.crm.product.ProductForm.ke9edfbed'), trigger: 'blur' }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.crm.product.ProductForm.k1318b551'),
      trigger: 'change'
    }
  ],
  categoryId: [
    { required: true, message: t('auto.views.crm.product.ProductForm.kab76c80d'), trigger: 'blur' }
  ],
  ownerUserId: [
    { required: true, message: t('auto.views.crm.product.ProductForm.kc40a3652'), trigger: 'blur' }
  ],
  price: [
    { required: true, message: t('auto.views.crm.product.ProductForm.k65b3b945'), trigger: 'blur' }
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
      formData.value = await ProductApi.getProduct(id)
    } finally {
      formLoading.value = false
    }
  } else {
    formData.value.ownerUserId = userId
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
    const data = formData.value as unknown as ProductApi.ProductVO
    if (formType.value === 'create') {
      await ProductApi.createProduct(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductApi.updateProduct(data)
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
    unit: undefined,
    price: Number(undefined),
    status: undefined,
    categoryId: undefined,
    description: undefined,
    ownerUserId: -1
  }
  formRef.value?.resetFields()
}

/** 初始化 */
const productCategoryList = ref<any[]>([]) // 产品分类树
const userList = ref<UserVO[]>([]) // 系统用户
onMounted(async () => {
  // 产品分类树
  const data = await ProductCategoryApi.getProductCategoryList({})
  productCategoryList.value = handleTree(data, 'id', 'parentId')
  // 系统用户列表
  userList.value = await getSimpleUserList()
})
</script>
