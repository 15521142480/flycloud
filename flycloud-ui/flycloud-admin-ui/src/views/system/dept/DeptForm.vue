<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item :label="t('auto.views.system.dept.DeptForm.k7cd8969b')" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="deptTree"
          :props="defaultProps"
          check-strictly
          default-expand-all
          :placeholder="t('auto.views.system.dept.DeptForm.k968b1fe0')"
          value-key="deptId"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.dept.DeptForm.ka35c1d2e')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.system.dept.DeptForm.k95e96ffe')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.dept.DeptForm.k424aaf17')" prop="sort">
        <el-input-number v-model="formData.sort" :min="0" controls-position="right" />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.dept.DeptForm.k974d383f')" prop="leaderUserId">
        <el-select
          v-model="formData.leaderUserId"
          clearable
          :placeholder="t('auto.views.system.dept.DeptForm.k38ed47bf')"
        >
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('auto.views.system.dept.DeptForm.ke02f6e57')" prop="phone">
        <el-input
          v-model="formData.phone"
          maxlength="11"
          :placeholder="t('auto.views.system.dept.DeptForm.k4597687d')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.system.dept.DeptForm.k9ed627bc')" prop="email">
        <el-input
          v-model="formData.email"
          maxlength="50"
          :placeholder="t('auto.views.system.dept.DeptForm.kf2dc24de')"
        />
      </el-form-item>
      <el-form-item :label="t('common.status')" prop="status">
        <el-select
          v-model="formData.status"
          clearable
          :placeholder="t('auto.views.system.dept.DeptForm.kdba277df')"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitForm">{{
        t('auto.views.system.dept.DeptForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.system.dept.DeptForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
// @ts-nocheck
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { defaultProps, handleTree } from '@/utils/tree'
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { CommonStatusEnum } from '@/utils/constants'
import { FormRules } from 'element-plus'
const { t } = useI18n()
defineOptions({ name: 'SystemDeptForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  title: '',
  parentId: undefined,
  name: undefined,
  sort: undefined,
  leaderUserId: undefined,
  phone: undefined,
  email: undefined,
  status: CommonStatusEnum.ENABLE
})
const formRules = reactive<FormRules>({
  parentId: [
    { required: true, message: t('auto.views.system.dept.DeptForm.kdb79ef98'), trigger: 'blur' }
  ],
  name: [
    { required: true, message: t('auto.views.system.dept.DeptForm.k34a04b8d'), trigger: 'blur' }
  ],
  sort: [
    { required: true, message: t('auto.views.system.dept.DeptForm.k31039a91'), trigger: 'blur' }
  ],
  email: [
    {
      type: 'email',
      message: t('auto.views.system.dept.DeptForm.k60d23edd'),
      trigger: ['blur', 'change']
    }
  ],
  phone: [
    {
      pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
      message: t('auto.views.system.dept.DeptForm.k36d2ca22'),
      trigger: 'blur'
    }
  ],
  status: [
    { required: true, message: t('auto.views.system.dept.DeptForm.k1318b551'), trigger: 'blur' }
  ]
})
const formRef = ref() // 表单 Ref
const deptTree = ref() // 树形结构
const userList = ref<UserApi.UserVO[]>([]) // 用户列表

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
      formData.value = await DeptApi.getDept(id)
    } finally {
      formLoading.value = false
    }
  }
  // 获得用户列表
  userList.value = await UserApi.getSimpleUserList()
  // 获得部门树
  await getTree()
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
    const data = formData.value as unknown as DeptApi.DeptVO
    if (formType.value === 'create') {
      await DeptApi.saveOrUpdate(data)
      message.success(t('common.createSuccess'))
    } else {
      await DeptApi.saveOrUpdate(data)
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
    title: '',
    parentId: undefined,
    name: undefined,
    sort: undefined,
    leaderUserId: undefined,
    phone: undefined,
    email: undefined,
    status: CommonStatusEnum.ENABLE
  }
  formRef.value?.resetFields()
}

/** 获得部门树 */
const getTree = async () => {
  deptTree.value = []
  const data = await DeptApi.getSimpleDeptList()
  let dept: Tree = { id: '0', name: t('auto.views.system.dept.DeptForm.kc01998c0'), children: [] }
  dept.children = handleTree(data)
  deptTree.value.push(dept)
}
</script>
