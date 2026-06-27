<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-text tag="p">
      {{ t('auto.components.DiyEditor.components.mobile.MenuList.property.ka76df3b0') }}
    </el-text>
    <el-text type="info" size="small">
      {{ t('auto.components.DiyEditor.components.mobile.MenuList.property.k2e972ad7') }}
    </el-text>

    <!-- 表单 -->
    <el-form label-width="60px" :model="formData" class="m-t-8px">
      <Draggable v-model="formData.list" :empty-item="EMPTY_MENU_LIST_ITEM_PROPERTY">
        <template #default="{ element }">
          <el-form-item
            :label="t('auto.components.DiyEditor.components.mobile.MenuList.property.k1f24c1e5')"
            prop="iconUrl"
          >
            <UploadImg v-model="element.iconUrl" height="80px" width="80px">
              <template #tip>
                {{ t('auto.components.DiyEditor.components.mobile.MenuList.property.kc9250c21') }}
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
        </template>
      </Draggable>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { usePropertyForm } from '@/components/DiyEditor/util'
import {
  EMPTY_MENU_LIST_ITEM_PROPERTY,
  MenuListProperty
} from '@/components/DiyEditor/components/mobile/MenuList/config'

/** 列表导航属性面板 */
const { t } = useI18n()
defineOptions({ name: 'MenuListProperty' })

const props = defineProps<{ modelValue: MenuListProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
