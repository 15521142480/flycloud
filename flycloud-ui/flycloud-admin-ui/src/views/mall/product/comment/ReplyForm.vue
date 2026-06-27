<template>
  <Dialog :title="t('auto.views.mall.product.comment.ReplyForm.kffc78509')" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item
        :label="t('auto.views.mall.product.comment.ReplyForm.ka36c787d')"
        prop="replyContent"
      >
        <el-input type="textarea" v-model="formData.replyContent" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitReplyForm" type="primary" :disabled="formLoading"
        >{{ t('auto.views.mall.product.comment.ReplyForm.k31f9d856') }}
      </el-button>
      <el-button @click="dialogVisible = false">{{
        t('auto.views.mall.product.comment.ReplyForm.kd54aeadc')
      }}</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as CommentApi from '@/api/mall/product/comment'
import { ElInput } from 'element-plus'
const { t } = useI18n()
defineOptions({ name: 'ProductComment' })

const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formData = ref({
  id: undefined,
  replyContent: undefined
})
const formRules = reactive({
  replyContent: [
    {
      required: true,
      message: t('auto.views.mall.product.comment.ReplyForm.kf5da0888'),
      trigger: 'blur'
    }
  ]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (id?: number) => {
  resetForm()
  formData.value.id = id
  dialogVisible.value = true
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitReplyForm = async () => {
  // 校验表单
  const valid = await formRef?.value?.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    await CommentApi.replyComment(formData.value)
    message.success(t('common.createSuccess'))
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
    replyContent: undefined
  }
  formRef.value?.resetFields()
}
</script>
