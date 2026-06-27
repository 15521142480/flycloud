<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.mall.product.category.CategoryForm.k0cacc9c3')"
        prop="parentId"
      >
        <el-select
          v-model="formData.parentId"
          :placeholder="t('auto.views.mall.product.category.CategoryForm.k3ba62bd1')"
        >
          <el-option
            :key="0"
            :label="t('auto.views.mall.product.category.CategoryForm.k419eab88')"
            :value="0"
          />
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.product.category.CategoryForm.k3fc30355')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.mall.product.category.CategoryForm.k56e43e46')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.product.category.CategoryForm.k48d796b0')"
        prop="picUrl"
      >
        <UploadImg v-model="formData.picUrl" :limit="1" :is-show-tip="false" />
        <div style="font-size: 10px" class="pl-10px">{{
          t('auto.views.mall.product.category.CategoryForm.k0fe3d1b5')
        }}</div>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.product.category.CategoryForm.k2b77925f')"
        prop="sort"
      >
        <el-input-number v-model="formData.sort" controls-position="right" :min="0" />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.product.category.CategoryForm.k6bbda1b1')"
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
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.mall.product.category.CategoryForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.product.category.CategoryForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { CommonStatusEnum } from '@/utils/constants'
import * as ProductCategoryApi from '@/api/mall/product/category'
const { t } = useI18n()
defineOptions({ name: 'ProductCategory' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  picUrl: '',
  status: CommonStatusEnum.ENABLE
})
const formRules = reactive({
  parentId: [
    {
      required: true,
      message: t('auto.views.mall.product.category.CategoryForm.k3ba62bd1'),
      trigger: 'blur'
    }
  ],
  name: [
    {
      required: true,
      message: t('auto.views.mall.product.category.CategoryForm.ka1a3f673'),
      trigger: 'blur'
    }
  ],
  picUrl: [
    {
      required: true,
      message: t('auto.views.mall.product.category.CategoryForm.k14a20fa3'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.mall.product.category.CategoryForm.k331d6aa3'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.mall.product.category.CategoryForm.k03991f81'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const categoryList = ref<any[]>([]) // 分类树

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
      formData.value = await ProductCategoryApi.getCategory(id)
    } finally {
      formLoading.value = false
    }
  }
  // 获得分类树
  categoryList.value = await ProductCategoryApi.getCategoryList({ parentId: 0 })
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
    const data = formData.value as ProductCategoryApi.CategoryVO
    if (formType.value === 'create') {
      await ProductCategoryApi.createCategory(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductCategoryApi.updateCategory(data)
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
    name: '',
    picUrl: '',
    status: CommonStatusEnum.ENABLE
  }
  formRef.value?.resetFields()
}
</script>
