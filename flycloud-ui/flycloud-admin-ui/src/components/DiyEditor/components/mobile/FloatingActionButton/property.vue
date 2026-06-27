<template>
  <el-form label-width="80px" :model="formData">
    <el-card
      :header="
        t('auto.components.DiyEditor.components.mobile.FloatingActionButton.property.k44367477')
      "
      class="property-group"
      shadow="never"
    >
      <el-form-item
        :label="
          t('auto.components.DiyEditor.components.mobile.FloatingActionButton.property.k56e40cc4')
        "
        prop="direction"
      >
        <el-radio-group v-model="formData.direction">
          <el-radio value="vertical">{{
            t('auto.components.DiyEditor.components.mobile.FloatingActionButton.property.k6ae4e00a')
          }}</el-radio>
          <el-radio value="horizontal">{{
            t('auto.components.DiyEditor.components.mobile.FloatingActionButton.property.k4461a8e6')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="
          t('auto.components.DiyEditor.components.mobile.FloatingActionButton.property.k6f95d477')
        "
        prop="showText"
      >
        <el-switch v-model="formData.showText" />
      </el-form-item>
    </el-card>
    <el-card
      :header="
        t('auto.components.DiyEditor.components.mobile.FloatingActionButton.property.k3c404e87')
      "
      class="property-group"
      shadow="never"
    >
      <Draggable v-model="formData.list" :empty-item="{ textColor: '#fff' }">
        <template #default="{ element, index }">
          <el-form-item
            :label="
              t(
                'auto.components.DiyEditor.components.mobile.FloatingActionButton.property.k1f24c1e5'
              )
            "
            :prop="`list[${index}].imgUrl`"
          >
            <UploadImg v-model="element.imgUrl" height="56px" width="56px" />
          </el-form-item>
          <el-form-item
            :label="
              t(
                'auto.components.DiyEditor.components.mobile.FloatingActionButton.property.kf4d3dab8'
              )
            "
            :prop="`list[${index}].text`"
          >
            <InputWithColor v-model="element.text" v-model:color="element.textColor" />
          </el-form-item>
          <el-form-item
            :label="
              t(
                'auto.components.DiyEditor.components.mobile.FloatingActionButton.property.kb0fb9e44'
              )
            "
            :prop="`list[${index}].url`"
          >
            <AppLinkInput v-model="element.url" />
          </el-form-item>
        </template>
      </Draggable>
    </el-card>
  </el-form>
</template>

<script setup lang="ts">
import { FloatingActionButtonProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'FloatingActionButtonProperty' })

const props = defineProps<{ modelValue: FloatingActionButtonProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
