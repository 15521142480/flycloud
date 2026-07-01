<template>
  <div class="tab-bar">
    <!-- 表单 -->
    <el-form :model="formData" label-width="80px">
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.TabBar.property.ke848ddd4')"
        prop="theme"
      >
        <el-select v-model="formData!.theme" @change="handleThemeChange">
          <el-option
            v-for="(theme, index) in THEME_LIST"
            :key="index"
            :label="theme.name"
            :value="theme.id"
          >
            <template #default>
              <div class="flex items-center justify-between">
                <Icon :icon="theme.icon" :color="theme.color" />
                <span>{{ theme.name }}</span>
              </div>
            </template>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="t('extra.k8d231704')">
        <ColorInput v-model="formData!.style.color" />
      </el-form-item>
      <el-form-item :label="t('extra.k076d83a2')">
        <ColorInput v-model="formData!.style.activeColor" />
      </el-form-item>
      <el-form-item :label="t('extra.k4767a4ae')">
        <el-radio-group v-model="formData!.style.bgType">
          <el-radio-button value="color">{{
            t('auto.components.DiyEditor.components.ComponentContainerProperty.kc502e4a0')
          }}</el-radio-button>
          <el-radio-button value="img">{{ t('extra.k3b9e2563') }}</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k91732e25')"
        v-if="formData!.style.bgType === 'color'"
      >
        <ColorInput v-model="formData!.style.bgColor" />
      </el-form-item>
      <el-form-item :label="t('cropper.selectImage')" v-if="formData!.style.bgType === 'img'">
        <UploadImg v-model="formData!.style.bgImg" directory="diy" width="100%" height="50px" class="min-w-200px">
          <template #tip> {{ t('extra.k509da066') }} </template>
        </UploadImg>
      </el-form-item>

      <el-text tag="p">{{ t('extra.k97e1a5d6') }}</el-text>
      <el-text type="info" size="small"> {{ t('extra.kd398e770') }} </el-text>
      <Draggable v-model="formData.items" :limit="5">
        <template #default="{ element }">
          <div class="m-b-8px flex items-center justify-around">
            <div class="flex flex-col items-center justify-between">
              <UploadImg
                v-model="element.iconUrl"
                width="40px"
                height="40px"
                :show-delete="false"
                :show-btn-text="false"
              />
              <el-text size="small">{{ t('extra.k7d32bb78') }}</el-text>
            </div>
            <div>
              <UploadImg
                v-model="element.activeIconUrl"
                width="40px"
                height="40px"
                :show-delete="false"
                :show-btn-text="false"
              />
              <el-text>{{ t('extra.k543b044d') }}</el-text>
            </div>
          </div>
          <el-form-item
            prop="text"
            :label="t('extra.k99e832f9')"
            label-width="48px"
            class="m-b-8px!"
          >
            <el-input v-model="element.text" :placeholder="t('extra.k4f6a3c19')" />
          </el-form-item>
          <el-form-item
            prop="url"
            :label="t('auto.components.DiyEditor.components.mobile.MagicCube.property.k71502205')"
            label-width="48px"
            class="m-b-0!"
          >
            <AppLinkInput v-model="element.url" />
          </el-form-item>
        </template>
      </Draggable>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { TabBarProperty, component, THEME_LIST } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'TabBarProperty' })

const props = defineProps<{ modelValue: TabBarProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)

// 将数据库的值更新到右侧属性栏
component.property.items = formData.value.items

// 要的主题
const handleThemeChange = () => {
  const theme = THEME_LIST.find((theme) => theme.id === formData.value.theme)
  if (theme?.color) {
    formData.value.style.activeColor = theme.color
  }
}
</script>

<style lang="scss" scoped></style>
