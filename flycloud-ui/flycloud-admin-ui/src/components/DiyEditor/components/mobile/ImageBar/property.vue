<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form label-width="80px" :model="formData">
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.ImageBar.property.k59b308c8')"
        prop="imgUrl"
      >
        <UploadImg
          v-model="formData.imgUrl"
          draggable="false"
          height="80px"
          width="100%"
          class="min-w-80px"
        >
          <template #tip>
            {{ t('auto.components.DiyEditor.components.mobile.ImageBar.property.k2c75987f') }}
          </template>
        </UploadImg>
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.MagicCube.property.k71502205')"
        prop="url"
      >
        <AppLinkInput v-model="formData.url" />
      </el-form-item>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { ImageBarProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'ImageBarProperty' })

const props = defineProps<{ modelValue: ImageBarProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
