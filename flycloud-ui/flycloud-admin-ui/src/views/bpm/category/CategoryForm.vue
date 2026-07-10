<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.bpm.category.CategoryForm.k72a5ce04')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.bpm.category.CategoryForm.kdb2ed286')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.category.CategoryForm.k2908a7c9')" prop="code">
        <el-input
          v-model="formData.code"
          :placeholder="t('auto.views.bpm.category.CategoryForm.k21711601')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.category.CategoryForm.k155818e5')" prop="status">
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
      <el-form-item :label="t('auto.views.bpm.category.CategoryForm.k2b77925f')" prop="sort">
        <el-input-number
          v-model="formData.sort"
          :placeholder="t('auto.views.bpm.category.CategoryForm.kb01ad622')"
          class="!w-1/1"
          :precision="0"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.bpm.category.CategoryForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.bpm.category.CategoryForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { CategoryApi, CategoryVO } from '@/api/bpm/category'
import { CommonStatusEnum } from '@/utils/constants'

/** BPM 流程分类 表单 */
const { t } = useI18n()
defineOptions({ name: 'CategoryForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  code: undefined,
  status: CommonStatusEnum.ENABLE,
  sort: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.bpm.category.CategoryForm.k4ed5a332'),
      trigger: 'blur'
    }
  ],
  code: [
    {
      required: true,
      message: t('auto.views.bpm.category.CategoryForm.k0557a246'),
      trigger: 'blur'
    }
  ],
  status: [
    {
      required: true,
      message: t('auto.views.bpm.category.CategoryForm.kcbb13bf5'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.bpm.category.CategoryForm.k331d6aa3'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

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
      formData.value = await CategoryApi.getCategory(id)
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
    const data = formData.value as unknown as CategoryVO
    if (formType.value === 'create') {
      await CategoryApi.createCategory(data)
      message.success(t('common.createSuccess'))
    } else {
      await CategoryApi.updateCategory(data)
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
    code: undefined,
    status: CommonStatusEnum.ENABLE,
    sort: undefined
  }
  formRef.value?.resetFields()
}
</script>
