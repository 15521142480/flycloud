<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="110px"
    >
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.k11c83e06')" prop="key">
        <el-input
          v-model="formData.key"
          :disabled="!!formData.id"
          :placeholder="t('auto.views.bpm.model.ModelForm.k4c13de5c')"
        />
        <el-tooltip
          v-if="!formData.id"
          class="item"
          :content="t('auto.views.bpm.model.ModelForm.k57e77f5d')"
          effect="light"
          placement="top"
        >
          <i class="el-icon-question" style="padding-left: 5px"></i>
        </el-tooltip>
        <el-tooltip
          v-else
          class="item"
          :content="t('auto.views.bpm.model.ModelForm.kb5207355')"
          effect="light"
          placement="top"
        >
          <i class="el-icon-question" style="padding-left: 5px"></i>
        </el-tooltip>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.k323a8305')" prop="name">
        <el-input
          v-model="formData.name"
          :disabled="!!formData.id"
          clearable
          :placeholder="t('auto.views.bpm.model.ModelForm.ke9399a13')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.k8377bb01')" prop="category">
        <el-select
          v-model="formData.category"
          clearable
          :placeholder="t('auto.views.bpm.model.ModelForm.k8c96f9ce')"
          style="width: 100%"
        >
          <el-option
            v-for="category in categoryList"
            :key="category.code"
            :label="category.name"
            :value="category.code"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.kb46a9bd4')" prop="icon">
        <UploadImg v-model="formData.icon" :limit="1" height="64px" width="64px" />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.kd344413f')" prop="description">
        <el-input v-model="formData.description" clearable type="textarea" />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.k0adccff7')" prop="type">
        <el-radio-group v-model="formData.type">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.BPM_MODEL_TYPE)"
            :key="dict.value"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.k496ea0cd')" prop="formType">
        <el-radio-group v-model="formData.formType">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.BPM_MODEL_FORM_TYPE)"
            :key="dict.value"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        v-if="formData.formType === 10"
        :label="t('auto.views.bpm.model.ModelForm.kb928cc5c')"
        prop="formId"
      >
        <el-select v-model="formData.formId" clearable style="width: 100%">
          <el-option v-for="form in formList" :key="form.id" :label="form.name" :value="form.id" />
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="formData.formType === 20"
        :label="t('auto.views.bpm.model.ModelForm.k5b6c75df')"
        prop="formCustomCreatePath"
      >
        <el-input
          v-model="formData.formCustomCreatePath"
          :placeholder="t('auto.views.bpm.model.ModelForm.k0ee0586e')"
          style="width: 330px"
        />
        <el-tooltip
          class="item"
          :content="t('auto.views.bpm.model.ModelForm.k7de2d22c')"
          effect="light"
          placement="top"
        >
          <i class="el-icon-question" style="padding-left: 5px"></i>
        </el-tooltip>
      </el-form-item>
      <el-form-item
        v-if="formData.formType === 20"
        :label="t('auto.views.bpm.model.ModelForm.k900c9062')"
        prop="formCustomViewPath"
      >
        <el-input
          v-model="formData.formCustomViewPath"
          :placeholder="t('auto.views.bpm.model.ModelForm.ka302f6b4')"
          style="width: 330px"
        />
        <el-tooltip
          class="item"
          :content="t('auto.views.bpm.model.ModelForm.ka850d5db')"
          effect="light"
          placement="top"
        >
          <i class="el-icon-question" style="padding-left: 5px"></i>
        </el-tooltip>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.kb23168b4')" prop="visible">
        <el-radio-group v-model="formData.visible">
          <el-radio
            v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
            :key="dict.value as string"
            :label="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.k0c236d4d')" prop="startUserIds">
        <el-select
          v-model="formData.startUserIds"
          multiple
          :placeholder="t('auto.views.bpm.model.ModelForm.k9f53ba37')"
        >
          <el-option v-for="user in userList" :key="user.id" :label="user.name" :value="user.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.model.ModelForm.kf14a5fa7')" prop="managerUserIds">
        <el-select
          v-model="formData.managerUserIds"
          multiple
          :placeholder="t('auto.views.bpm.model.ModelForm.k73d3e69f')"
        >
          <el-option v-for="user in userList" :key="user.id" :label="user.name" :value="user.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.bpm.model.ModelForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.bpm.model.ModelForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { propTypes } from '@/utils/propTypes'
import { DICT_TYPE, getBoolDictOptions, getIntDictOptions } from '@/utils/dict'
import { ElMessageBox } from 'element-plus'
import * as ModelApi from '@/api/bpm/model'
import * as FormApi from '@/api/bpm/form'
import { CategoryApi } from '@/api/bpm/category'
import { BpmModelFormType, BpmModelType } from '@/utils/constants'
import { UserVO } from '@/api/system/user'
import * as UserApi from '@/api/system/user'
import { useUserStoreWithOut } from '@/store/modules/user'
const { t } = useI18n()
defineOptions({ name: 'ModelForm' })
const message = useMessage() // 消息弹窗
const userStore = useUserStoreWithOut() // 用户信息缓存
const props = defineProps({
  categoryId: propTypes.number
})
const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: '',
  key: '',
  category: undefined,
  icon: undefined,
  description: '',
  type: BpmModelType.BPMN,
  formType: BpmModelFormType.NORMAL,
  formId: '',
  formCustomCreatePath: '',
  formCustomViewPath: '',
  visible: true,
  startUserIds: [],
  managerUserIds: []
})
const formRules = reactive({
  name: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k7a8c4d41'), trigger: 'blur' }
  ],
  key: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.kdb0aae73'), trigger: 'blur' }
  ],
  category: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k91f1baba'), trigger: 'blur' }
  ],
  // icon: [{ required: true, message: '流程图标不能为空', trigger: 'blur' }],
  type: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k697ac8ed'), trigger: 'blur' }
  ],
  formType: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k697ac8ed'), trigger: 'blur' }
  ],
  formId: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k3237f306'), trigger: 'blur' }
  ],
  formCustomCreatePath: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k165e65a7'), trigger: 'blur' }
  ],
  formCustomViewPath: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.ke70621f9'), trigger: 'blur' }
  ],
  visible: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k697ac8ed'), trigger: 'blur' }
  ],
  managerUserIds: [
    { required: true, message: t('auto.views.bpm.model.ModelForm.k5d4515ce'), trigger: 'blur' }
  ]
})
const formRef = ref() // 表单 Ref
const formList = ref([]) // 流程表单的下拉框的数据
const categoryList = ref([]) // 流程分类列表
const userList = ref<UserVO[]>([]) // 用户列表

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
      formData.value = await ModelApi.getModel(id)
    } finally {
      formLoading.value = false
    }
  } else {
    formData.value.managerUserIds.push(userStore.getUser.id)
  }
  // 获得流程表单的下拉框的数据
  formList.value = await FormApi.getFormSimpleList()
  // 查询流程分类列表
  categoryList.value = await CategoryApi.getCategorySimpleList()
  // 查询用户列表
  userList.value = await UserApi.getSimpleUserList()
  if (props.categoryId) {
    formData.value.category = props.categoryId
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
    const data = formData.value as unknown as ModelApi.ModelVO
    if (formType.value === 'create') {
      await ModelApi.createModel(data)
      // 提示，引导用户做后续的操作
      await ElMessageBox.alert(
        t('auto.views.bpm.model.ModelForm.k695a880d') +
          t('auto.views.bpm.model.ModelForm.k8c2a2f67') +
          t('auto.views.bpm.model.ModelForm.k2d428126') +
          t('auto.views.bpm.model.ModelForm.k3166888a'),
        t('auto.views.bpm.model.ModelForm.kd592e016'),
        {
          dangerouslyUseHTMLString: true,
          type: 'success'
        }
      )
    } else {
      await ModelApi.updateModel(data)
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
    key: '',
    category: undefined,
    icon: undefined,
    description: '',
    type: BpmModelType.BPMN,
    formType: BpmModelFormType.NORMAL,
    formId: '',
    formCustomCreatePath: '',
    formCustomViewPath: '',
    visible: true,
    startUserIds: [],
    managerUserIds: []
  }
  formRef.value?.resetFields()
}
</script>
