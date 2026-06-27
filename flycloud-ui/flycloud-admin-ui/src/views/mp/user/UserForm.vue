<template>
  <Dialog v-model="dialogVisible" :title="t('auto.views.mp.user.UserForm.kc9c77517')">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="80px"
    >
      <el-form-item :label="t('auto.views.mp.user.UserForm.k25124ed7')" prop="name">
        <el-input
          v-model="formData.name"
          :placeholder="t('auto.views.mp.user.UserForm.k5dbe6b07')"
        />
      </el-form-item>
      <el-form-item :label="t('common.remark')" prop="remark">
        <el-input
          v-model="formData.remark"
          :placeholder="t('auto.views.mp.user.UserForm.k57e709d9')"
        />
      </el-form-item>
      <el-form-item :label="t('auto.views.mp.user.UserForm.kae0a7afe')" prop="tagIds">
        <el-select
          v-model="formData.tagIds"
          clearable
          multiple
          :placeholder="t('auto.views.mp.user.UserForm.kcd71ef8f')"
        >
          <el-option
            v-for="item in tagList"
            :key="item.tagId"
            :label="item.name"
            :value="item.tagId"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{
        t('auto.views.mp.user.UserForm.k31f9d856')
      }}</el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mp.user.UserForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as MpTagApi from '@/api/mp/tag'
import * as MpUserApi from '@/api/mp/user'
const { t } = useI18n()
defineOptions({ name: 'MpUserForm' })
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const formData = ref({
  id: undefined,
  name: undefined,
  remark: undefined,
  tagIds: []
})
const formRules = reactive({}) // 表单的校验
const formRef = ref() // 表单 Ref
const tagList = ref([]) // 公众号标签列表

/** 打开弹窗 */
const open = async (id: number) => {
  dialogVisible.value = true
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await MpUserApi.getUser(id)
    } finally {
      formLoading.value = false
    }
  }
  // 加载标签
  tagList.value = await MpTagApi.getSimpleTagList()
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
    await MpUserApi.updateUser(formData.value)
    message.success(t('common.updateSuccess'))
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
    remark: undefined,
    tagIds: []
  }
  formRef.value?.resetFields()
}
</script>
