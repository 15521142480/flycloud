<template>
  <ComponentContainerProperty v-model="formData.style">
    <!-- 表单 -->
    <el-form label-width="80px" :model="formData" class="m-t-8px">
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.SearchBar.property.k4b08247d')"
        class="property-group"
        shadow="never"
      >
        <Draggable v-model="formData.hotKeywords" :empty-item="''">
          <template #default="{ index }">
            <el-input
              v-model="formData.hotKeywords[index]"
              :placeholder="
                t('auto.components.DiyEditor.components.mobile.SearchBar.property.k0b34c52b')
              "
            />
          </template>
        </Draggable>
      </el-card>
      <el-card :header="t('extra.k4c419a4f')" class="property-group" shadow="never">
        <el-form-item :label="t('extra.k9aac3609')">
          <el-radio-group v-model="formData!.borderRadius">
            <el-tooltip :content="t('extra.k35908bb0')" placement="top">
              <el-radio-button :value="0">
                <Icon icon="tabler:input-search" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip :content="t('extra.k2db0fcd5')" placement="top">
              <el-radio-button :value="10">
                <Icon icon="iconoir:input-search" />
              </el-radio-button>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="t('extra.k9b14c905')" prop="placeholder">
          <el-input v-model="formData.placeholder" />
        </el-form-item>
        <el-form-item :label="t('extra.k93bd6052')" prop="placeholderPosition">
          <el-radio-group v-model="formData!.placeholderPosition">
            <el-tooltip :content="t('extra.k316a6396')" placement="top">
              <el-radio-button value="left">
                <Icon icon="ant-design:align-left-outlined" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip :content="t('extra.k0bbc2ea4')" placement="top">
              <el-radio-button value="center">
                <Icon icon="ant-design:align-center-outlined" />
              </el-radio-button>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="t('extra.k402b79ad')" prop="showScan">
          <el-switch v-model="formData!.showScan" />
        </el-form-item>
        <el-form-item :label="t('extra.k70e84c03')" prop="height">
          <el-slider v-model="formData!.height" :max="50" :min="28" show-input input-size="small" />
        </el-form-item>
        <el-form-item :label="t('extra.k4a1e658c')" prop="backgroundColor">
          <ColorInput v-model="formData.backgroundColor" />
        </el-form-item>
        <el-form-item class="lef" :label="t('extra.k3b33ac4d')" prop="textColor">
          <ColorInput v-model="formData.textColor" />
        </el-form-item>
      </el-card>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { usePropertyForm } from '@/components/DiyEditor/util'
import { SearchProperty } from '@/components/DiyEditor/components/mobile/SearchBar/config'
/** 搜索框属性面板 */
const { t } = useI18n()
defineOptions({ name: 'SearchProperty' })

const props = defineProps<{ modelValue: SearchProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
