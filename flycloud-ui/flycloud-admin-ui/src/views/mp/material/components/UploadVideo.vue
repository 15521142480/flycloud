<template>
  <el-dialog
    :title="t('auto.views.mp.material.components.UploadVideo.kcb725a4b')"
    v-model="showDialog"
    width="600px"
  >
    <el-upload
      :action="UPLOAD_URL"
      :headers="HEADERS"
      multiple
      :limit="1"
      :file-list="fileList"
      :data="uploadData"
      :before-upload="beforeVideoUpload"
      :on-error="onUploadError"
      :on-success="onUploadSuccess"
      ref="uploadVideoRef"
      :auto-upload="false"
      class="mb-5"
    >
      <template #trigger>
        <el-button type="primary" plain>{{
          t('auto.views.mp.material.components.UploadVideo.kf6bc7748')
        }}</el-button>
      </template>
      <template #tip>
        <span class="el-upload__tip" style="margin-left: 10px">{{ t('extra.k95d5df7f') }}</span>
      </template>
    </el-upload>
    <el-divider />
    <el-form :model="uploadData" :rules="uploadRules" ref="uploadFormRef">
      <el-form-item :label="t('table.title')" prop="title">
        <el-input v-model="uploadData.title" :placeholder="t('extra.k6f5b83b0')" />
      </el-form-item>
      <el-form-item :label="t('auto.views.bpm.group.UserGroupForm.k412f54dc')" prop="introduction">
        <el-input
          :rows="3"
          type="textarea"
          v-model="uploadData.introduction"
          :placeholder="t('extra.k4feea8fe')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="showDialog = false">{{
        t('auto.components.AppLinkInput.AppLinkSelectDialog.kd54aeadc')
      }}</el-button>
      <el-button type="primary" @click="submitVideo">{{
        t('auto.views.mp.draft.index.k729bc2ba')
      }}</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
// @ts-nocheck
import type {
  FormInstance,
  FormRules,
  UploadInstance,
  UploadProps,
  UploadUserFile
} from 'element-plus'
import { HEADERS, UploadData, UPLOAD_URL, UploadType, beforeVideoUpload } from './upload'
const { t } = useI18n()
const message = useMessage()

const accountId = inject<number>('accountId')

const uploadRules: FormRules = {
  title: [
    {
      required: true,
      message: t('auto.views.mp.material.components.UploadVideo.k901722e5'),
      trigger: 'blur'
    }
  ],
  introduction: [
    {
      required: true,
      message: t('auto.views.mp.material.components.UploadVideo.kcb869887'),
      trigger: 'blur'
    }
  ]
}

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits<{
  (e: 'update:modelValue', v: boolean)
  (e: 'uploaded', v: void)
}>()

const showDialog = computed<boolean>({
  get() {
    return props.modelValue
  },
  set(val) {
    emit('update:modelValue', val)
  }
})

const fileList = ref<UploadUserFile[]>([])

const uploadData: UploadData = reactive({
  type: UploadType.Video,
  title: '',
  introduction: '',
  accountId: accountId!
})

const uploadFormRef = ref<FormInstance | null>(null)
const uploadVideoRef = ref<UploadInstance | null>(null)

const submitVideo = () => {
  uploadFormRef.value?.validate((valid) => {
    if (!valid) {
      return false
    }
    uploadVideoRef.value?.submit()
  })
}

/** 上传成功处理 */
const onUploadSuccess: UploadProps['onSuccess'] = (res: any) => {
  if (res.code !== 0) {
    message.error(t('auto.views.mp.material.components.UploadVideo.kea0f7067') + res.msg)
    return false
  }

  // 清空上传时的各种数据
  fileList.value = []
  uploadData.title = ''
  uploadData.introduction = ''

  showDialog.value = false
  message.notifySuccess(t('auto.views.mp.material.components.UploadVideo.kea9f9179'))
  emit('uploaded')
}

/** 上传失败处理 */
const onUploadError = (err: Error) => message.error(t('extra.k37ed3670', { p0: err.message }))
</script>
