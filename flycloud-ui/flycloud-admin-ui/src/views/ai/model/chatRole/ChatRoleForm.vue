<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.ai.model.chatRole.ChatRoleForm.k3aa1f085')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.ai.model.chatRole.ChatRoleForm.kb7c17b9e')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.ai.model.chatRole.ChatRoleForm.k9b7423ef')" prop="avatar">
        <UploadImg v-model="formData.avatar" directory="ai" height="60px" width="60px" />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.k5b7d951b')"
        prop="modelId"
        v-if="!isUser"
      >
        <el-select
          v-model="formData.modelId"
          :placeholder="t('auto.views.ai.model.chatRole.ChatRoleForm.k7330bf23')"
          clearable
        >
          <el-option
            v-for="chatModel in chatModelList"
            :key="chatModel.id"
            :label="chatModel.name"
            :value="chatModel.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.kcda79ba6')"
        prop="category"
        v-if="!isUser"
      >
        <el-input
          v-model="formData.category"
          :placeholder="t('auto.views.ai.model.chatRole.ChatRoleForm.k8343a95c')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.ka0a62a00')"
        prop="description"
      >
        <el-input
          type="textarea"
          v-model="formData.description"
          :placeholder="t('auto.views.ai.model.chatRole.ChatRoleForm.k0c205e28')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.k338affe3')"
        prop="systemMessage"
      >
        <el-input
          type="textarea"
          v-model="formData.systemMessage"
          :placeholder="t('auto.views.ai.model.chatRole.ChatRoleForm.kbb91e9c0')"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.k5857cbaf')"
        prop="publicStatus"
        v-if="!isUser"
      >
        <el-radio-group v-model="formData.publicStatus">
          <el-radio
            v-for="dict in getBoolDictOptions(DICT_TYPE.INFRA_BOOLEAN_STRING)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.kf86bd91f')"
        prop="sort"
        v-if="!isUser"
      >
        <el-input-number
          v-model="formData.sort"
          :placeholder="t('auto.views.ai.model.chatRole.ChatRoleForm.k426a9339')"
          class="!w-1/1"
        />
      </el-form-item>
      <el-form-item
        :label="t('auto.views.ai.model.chatRole.ChatRoleForm.k6bbda1b1')"
        prop="status"
        v-if="!isUser"
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
        t('auto.views.ai.model.chatRole.ChatRoleForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.ai.model.chatRole.ChatRoleForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
// @ts-nocheck
import { getIntDictOptions, getBoolDictOptions, DICT_TYPE } from '@/utils/dict'
import { ChatRoleApi, ChatRoleVO } from '@/api/ai/model/chatRole'
import { CommonStatusEnum } from '@/utils/constants'
import { ChatModelApi, ChatModelVO } from '@/api/ai/model/chatModel'
import { FormRules } from 'element-plus'

/** AI 聊天角色 表单 */
const { t } = useI18n()
defineOptions({ name: 'ChatRoleForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  modelId: undefined,
  name: undefined,
  avatar: undefined,
  category: undefined,
  sort: undefined,
  description: undefined,
  systemMessage: undefined,
  publicStatus: true,
  status: CommonStatusEnum.ENABLE
})
const formRef = ref() // 表单 Ref
const chatModelList = ref([] as ChatModelVO[]) // 聊天模型列表

/** 是否【我】自己创建，私有角色 */
const isUser = computed(() => {
  return formType.value === 'my-create' || formType.value === 'my-update'
})

const formRules = reactive<FormRules>({
  name: [
    {
      required: true,
      message: t('auto.views.ai.model.chatRole.ChatRoleForm.k299e56ae'),
      trigger: 'blur'
    }
  ],
  avatar: [
    {
      required: true,
      message: t('auto.views.ai.model.chatRole.ChatRoleForm.k72392da9'),
      trigger: 'blur'
    }
  ],
  category: [
    {
      required: true,
      message: t('auto.views.ai.model.chatRole.ChatRoleForm.k2d8be6ff'),
      trigger: 'blur'
    }
  ],
  sort: [
    {
      required: true,
      message: t('auto.views.ai.model.chatRole.ChatRoleForm.k1b15dfd0'),
      trigger: 'blur'
    }
  ],
  description: [
    {
      required: true,
      message: t('auto.views.ai.model.chatRole.ChatRoleForm.kd0e486fd'),
      trigger: 'blur'
    }
  ],
  systemMessage: [
    {
      required: true,
      message: t('auto.views.ai.model.chatRole.ChatRoleForm.kba6f4eaf'),
      trigger: 'blur'
    }
  ],
  publicStatus: [
    {
      required: true,
      message: t('auto.views.ai.model.chatRole.ChatRoleForm.k3fbc0a2e'),
      trigger: 'blur'
    }
  ]
})

/** 打开弹窗 */
// TODO @fan：title 是不是收敛到 type 判断生成 title，会更合理
const open = async (type: string, id?: string, title?: string) => {
  dialogVisible.value = true
  dialogTitle.value = title || t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ChatRoleApi.getChatRole(id)
    } finally {
      formLoading.value = false
    }
  }
  // 获得下拉数据
  chatModelList.value = await ChatModelApi.getChatModelSimpleList(CommonStatusEnum.ENABLE)
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
    const data = formData.value as unknown as ChatRoleVO
    // tip: my-create、my-update 是 chat 角色仓库调用
    // tip: create、else 是后台管理调用
    if (formType.value === 'my-create') {
      await ChatRoleApi.createMy(data)
      message.success(t('common.createSuccess'))
    } else if (formType.value === 'my-update') {
      await ChatRoleApi.updateMy(data)
      message.success(t('common.updateSuccess'))
    } else if (formType.value === 'create') {
      await ChatRoleApi.createChatRole(data)
      message.success(t('common.createSuccess'))
    } else {
      await ChatRoleApi.updateChatRole(data)
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
    modelId: undefined,
    name: undefined,
    avatar: undefined,
    category: undefined,
    sort: undefined,
    description: undefined,
    systemMessage: undefined,
    publicStatus: true,
    status: CommonStatusEnum.ENABLE
  }
  formRef.value?.resetFields()
}
</script>
