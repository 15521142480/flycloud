<!-- ERP 产品的新增/修改 -->
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
            :label="t('auto.views.erp.product.product.ProductForm.k1be7ae4f')"
            prop="name"
          >
            <el-input
              v-model="formData.name"
              :placeholder="t('auto.views.erp.product.product.ProductForm.kc2afb255')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.kb1085310')"
            prop="barCode"
          >
            <el-input
              v-model="formData.barCode"
              :placeholder="t('auto.views.erp.product.product.ProductForm.kb1efb2f5')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k435c5259')"
            prop="categoryId"
          >
            <el-tree-select
              v-model="formData.categoryId"
              :data="categoryList"
              :props="defaultProps"
              check-strictly
              default-expand-all
              :placeholder="t('auto.views.erp.product.product.ProductForm.k84037620')"
              class="w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k8ec8c8c7')"
            prop="unitId"
          >
            <el-select
              v-model="formData.unitId"
              clearable
              :placeholder="t('auto.views.erp.product.product.ProductForm.kb6a42312')"
              class="w-1/1"
            >
              <el-option
                v-for="unit in unitList"
                :key="unit.id"
                :label="unit.name"
                :value="unit.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
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
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k53af00e1')"
            prop="standard"
          >
            <el-input
              v-model="formData.standard"
              :placeholder="t('auto.views.erp.product.product.ProductForm.kd4f2c343')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k5d6e2204')"
            prop="expiryDay"
          >
            <el-input-number
              v-model="formData.expiryDay"
              :placeholder="t('auto.views.erp.product.product.ProductForm.kfe41a848')"
              :min="0"
              :precision="0"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k97921672')"
            prop="weight"
          >
            <el-input-number
              v-model="formData.weight"
              :placeholder="t('auto.views.erp.product.product.ProductForm.k975e9aa1')"
              :min="0"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k23c5b674')"
            prop="purchasePrice"
          >
            <el-input-number
              v-model="formData.purchasePrice"
              :placeholder="t('auto.views.erp.product.product.ProductForm.k719df3c4')"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k5b26b853')"
            prop="salePrice"
          >
            <el-input-number
              v-model="formData.salePrice"
              :placeholder="t('auto.views.erp.product.product.ProductForm.kb1292fa0')"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            :label="t('auto.views.erp.product.product.ProductForm.k6c760d86')"
            prop="minPrice"
          >
            <el-input-number
              v-model="formData.minPrice"
              :placeholder="t('auto.views.erp.product.product.ProductForm.k9de22fe4')"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="t('common.remark')" prop="remark">
            <el-input
              type="textarea"
              v-model="formData.remark"
              :placeholder="t('auto.views.erp.product.product.ProductForm.k57e709d9')"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.erp.product.product.ProductForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.erp.product.product.ProductForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { ProductUnitApi, ProductUnitVO } from '@/api/erp/product/unit'
import { CommonStatusEnum } from '@/utils/constants'
import { defaultProps, handleTree } from '@/utils/tree'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** ERP 产品 表单 */
const { t } = useI18n()
defineOptions({ name: 'ProductForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  barCode: undefined,
  categoryId: undefined,
  unitId: undefined,
  status: undefined,
  standard: undefined,
  remark: undefined,
  expiryDay: undefined,
  weight: undefined,
  purchasePrice: undefined,
  salePrice: undefined,
  minPrice: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.erp.product.product.ProductForm.kcbfa98d4'),
      trigger: 'blur'
    }
  ],
  barCode: [
    {
      required: true,
      message: t('auto.views.erp.product.product.ProductForm.k909ab2be'),
      trigger: 'blur'
    }
  ],
  categoryId: [
    {
      required: true,
      message: t('auto.views.erp.product.product.ProductForm.k2ec46e5e'),
      trigger: 'blur'
    }
  ],
  unitId: [
    {
      required: true,
      message: t('auto.views.erp.product.product.ProductForm.k897fe419'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.erp.product.product.ProductForm.k5b278370'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const categoryList = ref<ProductCategoryVO[]>([]) // 产品分类列表
const unitList = ref<ProductUnitVO[]>([]) // 产品单位列表

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
      formData.value = await ProductApi.getProduct(id)
    } finally {
      formLoading.value = false
    }
  }
  // 产品分类
  const categoryData = await ProductCategoryApi.getProductCategorySimpleList()
  categoryList.value = handleTree(categoryData, 'id', 'parentId')
  // 产品单位
  unitList.value = await ProductUnitApi.getProductUnitSimpleList()
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
    const data = formData.value as unknown as ProductVO
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
    barCode: undefined,
    categoryId: undefined,
    unitId: undefined,
    status: CommonStatusEnum.ENABLE,
    standard: undefined,
    remark: undefined,
    expiryDay: undefined,
    weight: undefined,
    purchasePrice: undefined,
    salePrice: undefined,
    minPrice: undefined
  }
  formRef.value?.resetFields()
}
</script>
