<template>
  <el-upload
    :action="UPLOAD_URL"
    :headers="HEADERS"
    multiple
    :limit="1"
    :file-list="fileList"
    :data="uploadData"
    :on-error="onUploadError"
    :before-upload="onBeforeUpload"
    :on-success="onUploadSuccess"
  >
    <el-button type="primary" plain>
      {{ t('auto.views.mp.material.components.UploadFile.k69aaf1d5') }}
    </el-button>
    <template #tip>
      <span class="el-upload__tip" style="margin-left: 5px">
        <slot></slot>
      </span>
    </template>
  </el-upload>
</template>
<script lang="ts" setup>
import type { UploadProps, UploadUserFile } from 'element-plus'
import {
  HEADERS,
  UPLOAD_URL,
  UploadData,
  UploadType,
  beforeImageUpload,
  beforeVoiceUpload
} from './upload'
const { t } = useI18n()
const message = useMessage()

const props = defineProps<{ type: UploadType }>()

const accountId = inject<number>('accountId')

const fileList = ref<UploadUserFile[]>([])
const emit = defineEmits<{
  (e: 'uploaded', v: void)
}>()

const uploadData: UploadData = reactive({
  type: UploadType.Image,
  title: '',
  introduction: '',
  accountId: accountId!
})

/** 上传前检查 */
const onBeforeUpload = props.type === UploadType.Image ? beforeImageUpload : beforeVoiceUpload

/** 上传成功处理 */
const onUploadSuccess: UploadProps['onSuccess'] = (res: any) => {
  if (res.code !== 0) {
    message.alertError(t('auto.views.mp.material.components.UploadFile.kea0f7067') + res.msg)
    return false
  }

  // 清空上传时的各种数据
  fileList.value = []
  uploadData.title = ''
  uploadData.introduction = ''

  message.notifySuccess(t('auto.views.mp.material.components.UploadFile.kea9f9179'))
  emit('uploaded')
}

/** 上传失败处理 */
const onUploadError = (err: Error) =>
  message.error(t('auto.views.mp.material.components.UploadFile.kb6017477') + err.message)
</script>

<style lang="scss" scoped>
.el-upload__tip {
  margin-left: 5px;
}
</style>
