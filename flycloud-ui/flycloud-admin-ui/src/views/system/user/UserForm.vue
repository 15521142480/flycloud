<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="90px"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item
            v-if="formData.id === undefined"
            :label="t('system.user.account')"
            prop="account"
          >
            <el-input
              v-model="formData.account"
              :placeholder="t('system.user.accountPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            v-if="formData.id === undefined"
            :label="t('system.user.password')"
            prop="password"
          >
            <el-input
              v-model="formData.password"
              :placeholder="t('system.user.passwordInputPlaceholder')"
              show-password
              type="password"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('system.user.nickname')" prop="name">
            <el-input v-model="formData.name" :placeholder="t('system.user.nicknamePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('system.user.dept')" prop="deptId">
            <el-tree-select
              v-model="formData.deptId"
              :data="deptList"
              :props="defaultProps"
              check-strictly
              node-key="id"
              :placeholder="t('system.user.deptPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('common.status')" prop="status">
            <el-select
              v-model="formData.status"
              clearable
              :placeholder="t('auto.views.system.post.PostForm.kdba277df')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('system.user.phone')" prop="telephone">
            <el-input
              v-model="formData.telephone"
              maxlength="11"
              :placeholder="t('system.user.phonePlaceholder')"
            />
          </el-form-item>

        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('system.user.email')" prop="email">
            <el-input
              v-model="formData.email"
              maxlength="50"
              :placeholder="t('system.user.emailPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">

          <el-form-item :label="t('system.user.sex')">
            <el-select v-model="formData.sex" :placeholder="t('common.selectText')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.SYSTEM_USER_SEX)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('extra.k06ba448d')">
            <el-select v-model="formData.postIds" multiple :placeholder="t('extra.k15cfe2ff')">
              <el-option
                v-for="item in postList"
                :key="item.id"
                :label="item.name"
                :value="item.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('system.user.avatar')" prop="picUrl">
            <UploadImg v-model="formData.avatar" directory="user" :disabled="false" height="80px" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('common.remark')">
            <el-input
              v-model="formData.remark"
              :placeholder="t('common.inputText')"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('common.ok')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { CommonStatusEnum } from '@/utils/constants'
import { defaultProps, handleTree } from '@/utils/tree'
import * as PostApi from '@/api/system/post'
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { FormRules } from 'element-plus'
import {TransferReqVO} from "@/api/crm/permission";
const { t } = useI18n()
defineOptions({ name: 'SystemUserForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
// const formData = ref({
//   name: '',
//   deptId: '',
//   telephone: '',
//   email: '',
//   id: undefined,
//   account: '',
//   password: '',
//   sex: undefined,
//   postIds: [],
//   remark: '',
//   status: CommonStatusEnum.ENABLE
//   // roleIds: []
// })
const formData = ref<UserApi.UserVO>({} as UserApi.UserVO)
const formRules = reactive<FormRules>({
  account: [{ required: true, message: t('system.user.accountRequired'), trigger: 'blur' }],
  name: [{ required: true, message: t('system.user.nicknameRequired'), trigger: 'blur' }],
  password: [{ required: true, message: t('system.user.passwordRequired'), trigger: 'blur' }],
  email: [
    {
      type: 'email',
      message: t('profile.rules.truemail'),
      trigger: ['blur', 'change']
    }
  ],
  telephone: [
    {
      pattern: /^(?:(?:\+|00)86)?1(?:3[\d]|4[5-79]|5[0-35-9]|6[5-7]|7[0-8]|8[\d]|9[189])\d{8}$/,
      message: t('profile.rules.truephone'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref
const deptList = ref<Tree[]>([]) // 树形结构
const postList = ref([] as PostApi.PostVO[]) // 岗位列表

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
      let userInfo = await UserApi.getUser(id)
      let userData = userInfo.user
      formData.value = userData
    } finally {
      formLoading.value = false
    }
  }
  // 加载部门树
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  // 加载岗位列表
  postList.value = await PostApi.getSimplePostList()
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
    const data = formData.value as unknown as UserApi.UserVO
    if (formType.value === 'create') {
      await UserApi.createUser(data)
      message.success(t('common.createSuccess'))
    } else {
      await UserApi.updateUser(data)
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
    name: '',
    deptId: '',
    telephone: '',
    email: '',
    id: undefined,
    account: '',
    password: '',
    sex: undefined,
    postIds: [],
    remark: '',
    status: CommonStatusEnum.ENABLE
    // roleIds: []
  }
  formRef.value?.resetFields()
}
</script>
