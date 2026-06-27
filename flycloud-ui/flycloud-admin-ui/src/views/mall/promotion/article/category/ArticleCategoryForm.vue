<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item
        :label="t('auto.views.mall.promotion.article.category.ArticleCategoryForm.k3fc30355')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="
            t('auto.views.mall.promotion.article.category.ArticleCategoryForm.k56e43e46')
          "
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.mall.promotion.article.category.ArticleCategoryForm.k5585653a')"
        prop="picUrl"
      >
        <UploadImg v-model="formData.picUrl" height="80px" />
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
        <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.mall.promotion.article.category.ArticleCategoryForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.promotion.article.category.ArticleCategoryForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as ArticleCategoryApi from '@/api/mall/promotion/articleCategory'
import { CommonStatusEnum } from '@/utils/constants'
const { t } = useI18n()
defineOptions({ name: 'PromotionArticleCategoryForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  picUrl: undefined,
  status: undefined,
  sort: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.category.ArticleCategoryForm.ka1a3f673'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.category.ArticleCategoryForm.k1318b551'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.mall.promotion.article.category.ArticleCategoryForm.k3218602a'),
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
      formData.value = await ArticleCategoryApi.getArticleCategory(id)
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
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ArticleCategoryApi.ArticleCategoryVO
    if (formType.value === 'create') {
      await ArticleCategoryApi.createArticleCategory(data)
      message.success(t('common.createSuccess'))
    } else {
      await ArticleCategoryApi.updateArticleCategory(data)
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
    picUrl: undefined,
    status: CommonStatusEnum.ENABLE,
    sort: 0
  }
  formRef.value?.resetFields()
}
</script>
