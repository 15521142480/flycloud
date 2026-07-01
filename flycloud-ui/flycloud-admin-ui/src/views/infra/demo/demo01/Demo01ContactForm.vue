<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.infra.demo.demo01.Demo01ContactForm.k364bd1bf')"
        prop="name"
      >
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.infra.demo.demo01.Demo01ContactForm.k010c1585')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.infra.demo.demo01.Demo01ContactForm.kfe8aa4ef')"
        prop="sex"
      >
        <el-radio-group v-model="formData.sex">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_USER_SEX)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.infra.demo.demo01.Demo01ContactForm.k6165c127')"
        prop="birthday"
      >
        <el-date-picker
          v-model="formData.birthday"
          type="date"
          value-format="x"
          :placeholder="t('auto.views.infra.demo.demo01.Demo01ContactForm.k6e027959')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.infra.demo.demo01.Demo01ContactForm.k5ea2e0cd')"
        prop="description"
      >
        <Editor v-model="formData.description" height="150px" />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.infra.demo.demo01.Demo01ContactForm.k4ceeeb31')"
        prop="avatar"
      >
        <UploadImg v-model="formData.avatar" directory="demo" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.infra.demo.demo01.Demo01ContactForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.infra.demo.demo01.Demo01ContactForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import * as Demo01ContactApi from '@/api/infra/demo/demo01'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  sex: undefined,
  birthday: undefined,
  description: undefined,
  avatar: undefined
})
const formRules = reactive({
  name: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo01.Demo01ContactForm.k46f3776c'),
      trigger: 'blur'
    }
  ],
  sex: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo01.Demo01ContactForm.k2b07e8d6'),
      trigger: 'blur'
    }
  ],
  birthday: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo01.Demo01ContactForm.k6f982400'),
      trigger: 'blur'
    }
  ],
  description: [
    {
      required: true,
      message: t('auto.views.infra.demo.demo01.Demo01ContactForm.k8803d8fa'),
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
      formData.value = await Demo01ContactApi.getDemo01Contact(id)
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
    const data = formData.value as unknown as Demo01ContactApi.Demo01ContactVO
    if (formType.value === 'create') {
      await Demo01ContactApi.createDemo01Contact(data)
      message.success(t('common.createSuccess'))
    } else {
      await Demo01ContactApi.updateDemo01Contact(data)
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
    sex: undefined,
    birthday: undefined,
    description: undefined,
    avatar: undefined
  }
  formRef.value?.resetFields()
}
</script>
