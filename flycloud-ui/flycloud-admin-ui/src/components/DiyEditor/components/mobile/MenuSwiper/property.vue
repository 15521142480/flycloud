<template>
  <ComponentContainerProperty v-model="formData.style">
    <!-- 表单 -->
    <el-form label-width="80px" :model="formData" class="m-t-8px">
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.kb0961e17')"
        prop="layout"
      >
        <el-radio-group v-model="formData.layout">
          <el-radio value="iconText">{{
            t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.kaaa7ad7e')
          }}</el-radio>
          <el-radio value="icon">{{
            t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k94d888ec')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k579f280b')"
        prop="row"
      >
        <el-radio-group v-model="formData.row">
          <el-radio :value="1">{{
            t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k6bb47749')
          }}</el-radio>
          <el-radio :value="2">{{
            t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.kbfb8ca14')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k067f80c3')"
        prop="column"
      >
        <el-radio-group v-model="formData.column">
          <el-radio :value="3">{{
            t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.ka9909c08')
          }}</el-radio>
          <el-radio :value="4">{{
            t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k904f39d1')
          }}</el-radio>
          <el-radio :value="5">{{
            t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k1d38b523')
          }}</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.ka76df3b0')"
        class="property-group"
        shadow="never"
      >
        <Draggable v-model="formData.list" :empty-item="cloneDeep(EMPTY_MENU_SWIPER_ITEM_PROPERTY)">
          <template #default="{ element }">
            <el-form-item
              :label="
                t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k1f24c1e5')
              "
              prop="iconUrl"
            >
              <UploadImg v-model="element.iconUrl" height="80px" width="80px">
                <template #tip>
                  {{
                    t('auto.components.DiyEditor.components.mobile.MenuSwiper.property.k5026223e')
                  }}
                </template>
              </UploadImg>
            </el-form-item>
            <el-form-item label="标题" prop="title">
              <InputWithColor v-model="element.title" v-model:color="element.titleColor" />
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
  EMPTY_MENU_SWIPER_ITEM_PROPERTY,
  MenuSwiperProperty
} from '@/components/DiyEditor/components/mobile/MenuSwiper/config'
import { cloneDeep } from 'lodash-es'
/** 菜单导航属性面板 */
const { t } = useI18n()
defineOptions({ name: 'MenuSwiperProperty' })

const props = defineProps<{ modelValue: MenuSwiperProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
