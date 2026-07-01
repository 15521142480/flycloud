<template>
  <el-form label-width="80px" :model="formData" :rules="rules">
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.PageConfig.property.k5d4f9172')"
      prop="description"
    >
      <el-input
        type="textarea"
        v-model="formData!.description"
        :placeholder="
          t('auto.components.DiyEditor.components.mobile.PageConfig.property.kb105a0df')
        "
      />
    </el-form-item>
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.PageConfig.property.k4a2d8a28')"
      prop="backgroundColor"
    >
      <ColorInput v-model="formData!.backgroundColor" />
    </el-form-item>
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.PageConfig.property.k8c841866')"
      prop="backgroundImage"
    >
      <UploadImg v-model="formData!.backgroundImage" directory="diy" :limit="1">
        <template #tip>{{
          t('auto.components.DiyEditor.components.mobile.PageConfig.property.k54614dc3')
        }}</template>
      </UploadImg>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { PageConfigProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'PageConfigProperty' })
// 表单校验
const rules = {}

const props = defineProps<{ modelValue: PageConfigProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
