<template>
  <el-form label-width="80px" :model="formData">
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.Divider.property.keea51aa4')"
      prop="height"
    >
      <el-slider v-model="formData.height" :min="1" :max="100" show-input input-size="small" />
    </el-form-item>
    <el-form-item
      :label="t('auto.components.DiyEditor.components.mobile.Divider.property.k73d46404')"
      prop="borderType"
    >
      <el-radio-group v-model="formData!.borderType">
        <el-tooltip
          placement="top"
          v-for="(item, index) in BORDER_TYPES"
          :key="index"
          :content="item.text"
        >
          <el-radio-button :value="item.type">
            <Icon :icon="item.icon" />
          </el-radio-button>
        </el-tooltip>
      </el-radio-group>
    </el-form-item>
    <template v-if="formData.borderType !== 'none'">
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.Divider.property.k2db49230')"
        prop="lineWidth"
      >
        <el-slider v-model="formData.lineWidth" :min="1" :max="30" show-input input-size="small" />
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.Divider.property.kd5a5526b')"
        prop="paddingType"
      >
        <el-radio-group v-model="formData!.paddingType">
          <el-tooltip
            :content="t('auto.components.DiyEditor.components.mobile.Divider.property.kd8a481cc')"
            placement="top"
          >
            <el-radio-button value="none">
              <Icon icon="tabler:box-padding" />
            </el-radio-button>
          </el-tooltip>
          <el-tooltip
            :content="t('auto.components.DiyEditor.components.mobile.Divider.property.k987418f8')"
            placement="top"
          >
            <el-radio-button value="horizontal">
              <Icon icon="vaadin:padding" />
            </el-radio-button>
          </el-tooltip>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.Divider.property.k8ef48860')"
      >
        <!-- 分割线颜色 -->
        <ColorInput v-model="formData.lineColor" />
      </el-form-item>
    </template>
  </el-form>
</template>

<script setup lang="ts">
import { DividerProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'DividerProperty' })
const props = defineProps<{ modelValue: DividerProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)

//线类型
const BORDER_TYPES = [
  {
    icon: 'vaadin:line-h',
    text: t('auto.components.DiyEditor.components.mobile.Divider.property.k165a7c44'),
    type: 'solid'
  },
  {
    icon: 'tabler:line-dashed',
    text: t('auto.components.DiyEditor.components.mobile.Divider.property.k12c09d03'),
    type: 'dashed'
  },
  {
    icon: 'tabler:line-dotted',
    text: t('auto.components.DiyEditor.components.mobile.Divider.property.k1510a5c5'),
    type: 'dotted'
  },
  {
    icon: 'entypo:progress-empty',
    text: t('auto.components.DiyEditor.components.mobile.Divider.property.k72077749'),
    type: 'none'
  }
]
</script>

<style scoped lang="scss"></style>
