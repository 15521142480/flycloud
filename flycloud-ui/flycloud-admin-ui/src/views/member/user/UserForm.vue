<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item :label="t('auto.views.member.user.UserForm.k5a9cc5e8')" prop="mobile">
        <el-input
          v-model="formData.mobile"
          :placeholder="t('auto.views.member.user.UserForm.k5ecce333')"
        />
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
      <el-form-item :label="t('auto.views.member.user.UserForm.k90542e0a')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.member.user.UserForm.k359da8d3')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.UserForm.k4ceeeb31')" prop="avatar">
        <UploadImg v-model="formData.avatar" :limit="1" :is-show-tip="false" />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.UserForm.kf593a9a9')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.member.user.UserForm.ka3d7a309')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.UserForm.k9227ccfa')" prop="sex">
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
      <el-form-item :label="t('auto.views.member.user.UserForm.ka1cd13bc')" prop="birthday">
        <el-date-picker
          v-model="formData.birthday"
          type="date"
          value-format="x"
          :placeholder="t('auto.views.member.user.UserForm.ka38f2f3a')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.UserForm.kfe837ba6')" prop="areaId">
        <el-tree-select
          v-model="formData.areaId"
          :data="areaList"
          :props="defaultProps"
          :render-after-expand="true"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.UserForm.k53ea20a0')" prop="tagIds">
        <MemberTagSelect v-model="formData.tagIds" show-add />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.UserForm.k137bc66f')" prop="groupId">
        <MemberGroupSelect v-model="formData.groupId" />
      </el-form-item>
      <el-form-item :label="t('auto.views.member.user.UserForm.kb1f5e04c')" prop="mark">
        <el-input
          type="textarea"
          v-model="formData.mark"
          :placeholder="t('auto.views.member.user.UserForm.k42b623a2')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">{{
        t('auto.views.member.user.UserForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.member.user.UserForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as UserApi from '@/api/member/user'
import * as AreaApi from '@/api/system/area'
import { defaultProps } from '@/utils/tree'
import MemberTagSelect from '@/views/member/tag/components/MemberTagSelect.vue'
import MemberGroupSelect from '@/views/member/group/components/MemberGroupSelect.vue'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  mobile: undefined,
  password: undefined,
  status: undefined,
  name: undefined,
  avatar: undefined,
  name: undefined,
  sex: undefined,
  areaId: undefined,
  birthday: undefined,
  mark: undefined,
  tagIds: [],
  groupId: undefined
})
const formRules = reactive({
  mobile: [
    { required: true, message: t('auto.views.member.user.UserForm.ka40faf76'), trigger: 'blur' }
  ],
  status: [
    { required: true, message: t('auto.views.member.user.UserForm.k1318b551'), trigger: 'blur' }
  ]
})
const formRef = ref() // 表单 Ref
const areaList = ref([]) // 地区列表

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
      formData.value = await UserApi.getUser(id)
    } finally {
      formLoading.value = false
    }
  }
  // 获得地区列表
  areaList.value = await AreaApi.getAreaTree()
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
      // 说明：目前暂时没有新增操作。如果自己业务需要，可以进行扩展
      // await UserApi.createUser(data)
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
    id: undefined,
    mobile: undefined,
    password: undefined,
    status: undefined,
    name: undefined,
    avatar: undefined,
    name: undefined,
    sex: undefined,
    areaId: undefined,
    birthday: undefined,
    mark: undefined,
    tagIds: [],
    groupId: undefined
  }
  formRef.value?.resetFields()
}
</script>
