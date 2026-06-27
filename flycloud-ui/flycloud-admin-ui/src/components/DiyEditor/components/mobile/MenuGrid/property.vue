<template>
  <ComponentContainerProperty v-model="formData.style">
    <!-- 表单 -->
    <el-form label-width="80px" :model="formData" class="m-t-8px">
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.MenuGrid.property.k6a553efa')"
        prop="column"
      >
        <el-radio-group v-model="formData.column">
          <el-radio :value="3">{{
            t('auto.components.DiyEditor.components.mobile.MenuGrid.property.kf1ffe416')
          }}</el-radio>
          <el-radio :value="4">{{
            t('auto.components.DiyEditor.components.mobile.MenuGrid.property.k758a56d5')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.MenuGrid.property.ka76df3b0')"
        class="property-group"
        shadow="never"
      >
        <Draggable v-model="formData.list" :empty-item="EMPTY_MENU_GRID_ITEM_PROPERTY">
          <template #default="{ element }">
            <el-form-item
              :label="t('auto.components.DiyEditor.components.mobile.MenuGrid.property.k1f24c1e5')"
              prop="iconUrl"
            >
              <UploadImg v-model="element.iconUrl" height="80px" width="80px">
                <template #tip>
                  {{ t('auto.components.DiyEditor.components.mobile.MenuGrid.property.kc9250c21') }}
                </template>
              </UploadImg>
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <InputWithColor v-model="element.title" v-model:color="element.titleColor" />
            </el-form-item>
            <el-form-item label="副标题" prop="subtitle">
              <InputWithColor v-model="element.subtitle" v-model:color="element.subtitleColor" />
            </el-form-item>
            <el-form-item label="链接" prop="url">
              <AppLinkInput v-model="element.url" />
            </el-form-item>
            <el-form-item label="显示角标" prop="badge.show">
              <el-switch v-model="element.badge.show" />
            </el-form-item>
            <template v-if="element.badge.show">
              <el-form-item label="角标内容" prop="badge.text">
                <InputWithColor
                  v-model="element.badge.text"
                  v-model:color="element.badge.textColor"
                />
              </el-form-item>
              <el-form-item label="背景颜色" prop="badge.bgColor">
                <ColorInput v-model="element.badge.bgColor" />
              </el-form-item>
            </template>
          </template>
        </Draggable>
      </el-card>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { usePropertyForm } from '@/components/DiyEditor/util'
import {
  EMPTY_MENU_GRID_ITEM_PROPERTY,
  MenuGridProperty
} from '@/components/DiyEditor/components/mobile/MenuGrid/config'

/** 宫格导航属性面板 */
const { t } = useI18n()
defineOptions({ name: 'MenuGridProperty' })

const props = defineProps<{ modelValue: MenuGridProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
