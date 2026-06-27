<template>
  <el-form label-width="80px" :model="formData">
    <Draggable v-model="formData.list" :empty-item="{ showType: 'once' }">
      <template #default="{ element, index }">
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.Popover.property.kbe8da62e')"
          :prop="`list[${index}].imgUrl`"
        >
          <UploadImg v-model="element.imgUrl" height="56px" width="56px" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.Popover.property.kb0fb9e44')"
          :prop="`list[${index}].url`"
        >
          <AppLinkInput v-model="element.url" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.Popover.property.ked7e5368')"
          :prop="`list[${index}].showType`"
        >
          <el-radio-group v-model="element.showType">
            <el-tooltip
              :content="t('auto.components.DiyEditor.components.mobile.Popover.property.kc3975e53')"
              placement="bottom"
            >
              <el-radio value="once">{{
                t('auto.components.DiyEditor.components.mobile.Popover.property.k26d5a2aa')
              }}</el-radio>
            </el-tooltip>
            <el-tooltip
              :content="t('auto.components.DiyEditor.components.mobile.Popover.property.kc6853403')"
              placement="bottom"
            >
              <el-radio value="always">{{
                t('auto.components.DiyEditor.components.mobile.Popover.property.k09c4fc25')
              }}</el-radio>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>
      </template>
    </Draggable>
  </el-form>
</template>

<script setup lang="ts">
import { PopoverProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'PopoverProperty' })

const props = defineProps<{ modelValue: PopoverProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
