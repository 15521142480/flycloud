<template>
  <el-form label-width="80px" :model="formData" :rules="rules">
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k393a6c91')"
      prop="styleType"
    >
      <el-radio-group v-model="formData!.styleType">
        <el-radio value="normal">{{
          t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k8beedd9b')
        }}</el-radio>
        <el-tooltip
          :content="
            t('auto.components.DiyEditor.components.mobile.NavigationBar.property.kfb026431')
          "
          placement="top"
        >
          <el-radio value="inner">{{
            t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k4482d00a')
          }}</el-radio>
        </el-tooltip>
      </el-radio-group>
    </el-form-item>
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k09b030bf')"
      prop="alwaysShow"
      v-if="formData.styleType === 'inner'"
    >
      <el-radio-group v-model="formData!.alwaysShow">
        <el-radio :value="false">{{ t('common.close') }}</el-radio>
        <el-tooltip
          :content="
            t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k50794371')
          "
          placement="top"
        >
          <el-radio :value="true">{{
            t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k256783b7')
          }}</el-radio>
        </el-tooltip>
      </el-radio-group>
    </el-form-item>
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k99b01ada')"
      prop="bgType"
    >
      <el-radio-group v-model="formData.bgType">
        <el-radio value="color">{{
          t('auto.components.DiyEditor.components.mobile.NavigationBar.property.kc502e4a0')
        }}</el-radio>
        <el-radio value="img">{{
          t('auto.components.DiyEditor.components.mobile.NavigationBar.property.kbe8da62e')
        }}</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k4a2d8a28')"
      prop="bgColor"
      v-if="formData.bgType === 'color'"
    >
      <ColorInput v-model="formData.bgColor" />
    </el-form-item>
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k8c841866')"
      prop="bgImg"
      v-else
    >
      <UploadImg v-model="formData.bgImg" directory="diy" :limit="1" width="56px" height="56px" />
    </el-form-item>
    <el-card class="property-group" shadow="never">
      <template #header>
        <div class="flex items-center justify-between">
          <span>{{
            t('auto.components.DiyEditor.components.mobile.NavigationBar.property.kaf543980')
          }}</span>
          <el-form-item prop="_local.previewMp" class="m-b-0!">
            <el-checkbox
              v-model="formData._local.previewMp"
              @change="formData._local.previewOther = !formData._local.previewMp"
              >{{ t('action.preview') }}</el-checkbox
            >
          </el-form-item>
        </div>
      </template>
      <NavigationBarCellProperty v-model="formData.mpCells" is-mp />
    </el-card>
    <el-card class="property-group" shadow="never">
      <template #header>
        <div class="flex items-center justify-between">
          <span>{{ t('extra.k29213155') }}</span>
          <el-form-item prop="_local.previewOther" class="m-b-0!">
            <el-checkbox
              v-model="formData._local.previewOther"
              @change="formData._local.previewMp = !formData._local.previewOther"
              >{{ t('action.preview') }}</el-checkbox
            >
          </el-form-item>
        </div>
      </template>
      <NavigationBarCellProperty v-model="formData.otherCells" :is-mp="false" />
    </el-card>
  </el-form>
</template>

<script setup lang="ts">
import { NavigationBarProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
import NavigationBarCellProperty from '@/components/DiyEditor/components/mobile/NavigationBar/components/CellProperty.vue'
const { t } = useI18n()
defineOptions({ name: 'NavigationBarProperty' })
// 表单校验
const rules = {
  name: [
    {
      required: true,
      message: t('auto.components.DiyEditor.components.mobile.NavigationBar.property.ke41e27e6'),
      trigger: 'blur'
    }
  ]
}

const props = defineProps<{ modelValue: NavigationBarProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
if (!formData.value._local) {
  formData.value._local = { previewMp: true, previewOther: false }
}
</script>

<style scoped lang="scss"></style>
