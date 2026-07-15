<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="620px">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item v-if="formType === 'update'" label="会员编号">
        <el-input v-model="formData.id" disabled />
      </el-form-item>
      <el-form-item v-if="formType === 'create'" label="手机号" prop="mobile">
        <el-input v-model="formData.mobile" clearable placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item v-if="formType === 'create'" label="邮箱" prop="email">
        <el-input v-model="formData.email" clearable placeholder="请输入邮箱（选填）" />
      </el-form-item>
      <el-form-item label="会员昵称" prop="nickname">
        <el-input v-model="formData.nickname" clearable placeholder="请输入会员昵称" />
      </el-form-item>
      <el-form-item label="真实姓名" prop="name">
        <el-input v-model="formData.name" clearable placeholder="请输入真实姓名（选填）" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
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
      <el-form-item label="岗位编号" prop="postIdsText">
        <el-input
          v-model="formData.postIdsText"
          clearable
          placeholder="多个岗位编号使用英文逗号分隔，例如：1,2"
        />
      </el-form-item>
      <el-form-item label="会员备注" prop="mark">
        <el-input v-model="formData.mark" :rows="3" maxlength="512" show-word-limit type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">保存</el-button>
      <el-button :disabled="formLoading" @click="dialogVisible = false">取消</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as MemberUserSearchApi from '@/api/dis/member-user'

type FormType = 'create' | 'update'

/** 会员 ES 演示页使用的新增、修改共用表单。 */
defineOptions({ name: 'MemberUserSearchForm' })

const message = useMessage()
const emit = defineEmits<{
  success: []
}>()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref<FormType>('create')
const formRef = ref()
const formData = reactive({
  id: undefined as string | undefined,
  mobile: '',
  email: '',
  nickname: '',
  name: '',
  status: 0,
  mark: '',
  postIdsText: ''
})

const formRules = reactive<Record<string, any[]>>({
  mobile: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { max: 32, message: '手机号长度不能超过 32 个字符', trigger: 'blur' }
  ],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }],
  nickname: [
    { required: true, message: '请输入会员昵称', trigger: 'blur' },
    { max: 64, message: '会员昵称长度不能超过 64 个字符', trigger: 'blur' }
  ],
  name: [{ max: 64, message: '真实姓名长度不能超过 64 个字符', trigger: 'blur' }],
  mark: [{ max: 512, message: '会员备注长度不能超过 512 个字符', trigger: 'blur' }]
})

/** 打开新增或修改会员弹窗。 */
const open = (type: FormType, row?: MemberUserSearchApi.MemberUserSearchVO) => {
  formType.value = type
  dialogTitle.value = type === 'create' ? '新增会员并异步同步 ES' : '修改会员并异步同步 ES'
  resetForm()
  if (row) {
    formData.id = row.id
    formData.nickname = row.nickname || ''
    formData.name = row.name || ''
    formData.status = row.status
    formData.mark = row.mark || ''
    formData.postIdsText = row.postIds?.join(',') || ''
  }
  dialogVisible.value = true
}

/** 重置表单数据。 */
const resetForm = () => {
  formData.id = undefined
  formData.mobile = ''
  formData.email = ''
  formData.nickname = ''
  formData.name = ''
  formData.status = 0
  formData.mark = ''
  formData.postIdsText = ''
  formRef.value?.resetFields()
}

/** 将岗位编号文本转换为接口所需的数组。 */
const parsePostIds = (): string[] | undefined => {
  const values = formData.postIdsText
    .split(',')
    .map((value) => value.trim())
    .filter(Boolean)
  return values.length > 0 ? values : undefined
}

/** 校验并提交新增或修改请求。 */
const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) {
    return
  }
  formLoading.value = true
  try {
    const postIds = parsePostIds()
    if (formType.value === 'create') {
      await MemberUserSearchApi.insertMemberUser({
        mobile: formData.mobile,
        email: formData.email || undefined,
        nickname: formData.nickname,
        name: formData.name || undefined,
        status: formData.status,
        mark: formData.mark || undefined,
        postIds
      })
      message.success('会员已新增，已写入本地消息表，等待 RocketMQ 异步同步 ES')
    } else {
      await MemberUserSearchApi.updateMemberUser({
        id: formData.id!,
        nickname: formData.nickname || undefined,
        name: formData.name || undefined,
        status: formData.status,
        mark: formData.mark || undefined,
        postIds
      })
      message.success('会员已修改，已写入本地消息表，等待 RocketMQ 异步同步 ES')
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

defineExpose({ open })
</script>
