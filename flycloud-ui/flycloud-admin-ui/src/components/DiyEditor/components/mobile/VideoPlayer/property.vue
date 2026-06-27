<template>
  <ComponentContainerProperty v-model="formData.style">
    <template #style>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.VideoPlayer.property.keea51aa4')"
        prop="height"
      >
        <el-slider
          v-model="formData.style.height"
          :max="500"
          :min="100"
          show-input
          input-size="small"
          :show-input-controls="false"
        />
      </el-form-item>
    </template>
    <el-form label-width="80px" :model="formData">
      <el-form-item label="上传视频" prop="videoUrl">
        <UploadFile
          v-model="formData.videoUrl"
          :file-type="['mp4']"
          :limit="1"
          :file-size="100"
          class="min-w-80px"
        />
      </el-form-item>
      <el-form-item label="上传封面" prop="posterUrl">
        <UploadImg
          v-model="formData.posterUrl"
          draggable="false"
          height="80px"
          width="100%"
          class="min-w-80px"
        >
          <template #tip> 建议宽度750 </template>
        </UploadImg>
      </el-form-item>
      <el-form-item label="自动播放" prop="autoplay">
        <el-switch v-model="formData.autoplay" />
      </el-form-item>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { VideoPlayerProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'VideoPlayerProperty' })

const props = defineProps<{ modelValue: VideoPlayerProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
