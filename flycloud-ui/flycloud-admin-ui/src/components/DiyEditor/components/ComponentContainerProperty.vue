<template>
  <el-tabs stretch>
    <!-- 每个组件的自定义内容 -->
    <el-tab-pane
      :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k163aec91')"
      v-if="$slots.default"
    >
      <slot></slot>
    </el-tab-pane>

    <!-- 每个组件的通用内容 -->
    <el-tab-pane
      :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k393a6c91')"
      lazy
    >
      <el-card
        :header="t('auto.components.DiyEditor.components.ComponentContainerProperty.k63d3704f')"
        class="property-group"
      >
        <el-form :model="formData" label-width="80px">
          <el-form-item
            :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k2f849f33')"
            prop="bgType"
          >
            <el-radio-group v-model="formData.bgType">
              <el-radio value="color">{{
                t('auto.components.DiyEditor.components.ComponentContainerProperty.kc502e4a0')
              }}</el-radio>
              <el-radio value="img">{{
                t('auto.components.DiyEditor.components.ComponentContainerProperty.kbe8da62e')
              }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k91732e25')"
            prop="bgColor"
            v-if="formData.bgType === 'color'"
          >
            <ColorInput v-model="formData.bgColor" />
          </el-form-item>
          <el-form-item
            :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k59b308c8')"
            prop="bgImg"
            v-else
          >
            <UploadImg v-model="formData.bgImg" :limit="1">
              <template #tip>{{
                t('auto.components.DiyEditor.components.ComponentContainerProperty.k54614dc3')
              }}</template>
            </UploadImg>
          </el-form-item>
          <el-tree :data="treeData" :expand-on-click-node="false" default-expand-all>
            <template #default="{ node, data }">
              <el-form-item
                :label="data.label"
                :prop="data.prop"
                :label-width="node.level === 1 ? '80px' : '62px'"
                class="w-full m-b-0!"
              >
                <el-slider
                  v-model="formData[data.prop]"
                  :max="100"
                  :min="0"
                  show-input
                  input-size="small"
                  :show-input-controls="false"
                  @input="handleSliderChange(data.prop)"
                />
              </el-form-item>
            </template>
          </el-tree>
          <slot name="style" :style="formData"></slot>
        </el-form>
      </el-card>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import { ComponentStyle, usePropertyForm } from '@/components/DiyEditor/util'
/**
 * 组件容器属性：目前右边部分
 * 用于包裹组件，为组件提供 背景、外边距、内边距、边框等样式
 */
const { t } = useI18n()
defineOptions({ name: 'ComponentContainer' })

const props = defineProps<{ modelValue: ComponentStyle }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)

const treeData = [
  {
    label: t('auto.components.DiyEditor.components.ComponentContainerProperty.ka0a8c031'),
    prop: 'margin',
    children: [
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.kb967ce84'),
        prop: 'marginTop'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.k851cdd60'),
        prop: 'marginRight'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.ka6caf2ef'),
        prop: 'marginBottom'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.kdf3ee667'),
        prop: 'marginLeft'
      }
    ]
  },
  {
    label: t('auto.components.DiyEditor.components.ComponentContainerProperty.k8292daed'),
    prop: 'padding',
    children: [
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.kb967ce84'),
        prop: 'paddingTop'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.k851cdd60'),
        prop: 'paddingRight'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.ka6caf2ef'),
        prop: 'paddingBottom'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.kdf3ee667'),
        prop: 'paddingLeft'
      }
    ]
  },
  {
    label: t('auto.components.DiyEditor.components.ComponentContainerProperty.kf3486e92'),
    prop: 'borderRadius',
    children: [
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.k91ddc16d'),
        prop: 'borderTopLeftRadius'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.kcc2c87ba'),
        prop: 'borderTopRightRadius'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.kab1eb0c0'),
        prop: 'borderBottomRightRadius'
      },
      {
        label: t('auto.components.DiyEditor.components.ComponentContainerProperty.k801f2acb'),
        prop: 'borderBottomLeftRadius'
      }
    ]
  }
]

const handleSliderChange = (prop: string) => {
  switch (prop) {
    case 'margin':
      formData.value.marginTop = formData.value.margin
      formData.value.marginRight = formData.value.margin
      formData.value.marginBottom = formData.value.margin
      formData.value.marginLeft = formData.value.margin
      break
    case 'padding':
      formData.value.paddingTop = formData.value.padding
      formData.value.paddingRight = formData.value.padding
      formData.value.paddingBottom = formData.value.padding
      formData.value.paddingLeft = formData.value.padding
      break
    case 'borderRadius':
      formData.value.borderTopLeftRadius = formData.value.borderRadius
      formData.value.borderTopRightRadius = formData.value.borderRadius
      formData.value.borderBottomRightRadius = formData.value.borderRadius
      formData.value.borderBottomLeftRadius = formData.value.borderRadius
      break
  }
}
</script>

<style scoped lang="scss">
:deep(.el-slider__runway) {
  margin-right: 16px;
}

:deep(.el-input-number) {
  width: 50px;
}
</style>
