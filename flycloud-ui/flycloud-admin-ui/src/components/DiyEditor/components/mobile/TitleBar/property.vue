<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form label-width="85px" :model="formData" :rules="rules">
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.TitleBar.property.ka4ce420c')"
        class="property-group"
        shadow="never"
      >
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.TitleBar.property.k8c841866')"
          prop="bgImgUrl"
        >
          <UploadImg v-model="formData.bgImgUrl" directory="diy" width="100%" height="40px">
            <template #tip>{{
              t('auto.components.DiyEditor.components.mobile.TitleBar.property.ka13ca032')
            }}</template>
          </UploadImg>
        </el-form-item>
        <el-form-item :label="t('extra.k3a864730')" prop="textAlign">
          <el-radio-group v-model="formData!.textAlign">
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
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.TitleBar.config.ke6dc2df4')"
        class="property-group"
        shadow="never"
      >
        <el-form-item :label="t('extra.k99e832f9')" prop="title" label-width="40px">
          <InputWithColor
            v-model="formData.title"
            v-model:color="formData.titleColor"
            show-word-limit
            maxlength="20"
          />
        </el-form-item>
        <el-form-item :label="t('extra.k58f96667')" prop="titleSize" label-width="40px">
          <el-slider
            v-model="formData.titleSize"
            :max="60"
            :min="10"
            show-input
            input-size="small"
          />
        </el-form-item>
        <el-form-item :label="t('extra.kfb0af810')" prop="titleWeight" label-width="40px">
          <el-slider
            v-model="formData.titleWeight"
            :min="100"
            :max="900"
            :step="100"
            show-input
            input-size="small"
          />
        </el-form-item>
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.MenuGrid.config.k8344831e')"
        class="property-group"
        shadow="never"
      >
        <el-form-item :label="t('extra.k99e832f9')" prop="description" label-width="40px">
          <InputWithColor
            v-model="formData.description"
            v-model:color="formData.descriptionColor"
            show-word-limit
            maxlength="50"
          />
        </el-form-item>
        <el-form-item :label="t('extra.k58f96667')" prop="descriptionSize" label-width="40px">
          <el-slider
            v-model="formData.descriptionSize"
            :max="60"
            :min="10"
            show-input
            input-size="small"
          />
        </el-form-item>
        <el-form-item :label="t('extra.kfb0af810')" prop="descriptionWeight" label-width="40px">
          <el-slider
            v-model="formData.descriptionWeight"
            :min="100"
            :max="900"
            :step="100"
            show-input
            input-size="small"
          />
        </el-form-item>
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.TitleBar.config.k120b8697')"
        class="property-group"
        shadow="never"
      >
        <el-form-item :label="t('extra.kae57b19f')" prop="more.show">
          <el-checkbox v-model="formData.more.show" />
        </el-form-item>
        <!-- 更多按钮的 样式选择 -->
        <template v-if="formData.more.show">
          <el-form-item
            :label="t('auto.components.DiyEditor.components.ComponentContainerProperty.k393a6c91')"
            prop="more.type"
          >
            <el-radio-group v-model="formData.more.type">
              <el-radio value="text">{{ t('extra.k99e832f9') }}</el-radio>
              <el-radio value="icon">{{ t('form.icon') }}</el-radio>
              <el-radio value="all">{{ t('extra.k6251cd1d') }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            :label="t('extra.k87aae677')"
            prop="more.text"
            v-show="formData.more.type !== 'icon'"
          >
            <el-input v-model="formData.more.text" />
          </el-form-item>
          <el-form-item
            :label="
              t(
                'auto.components.DiyEditor.components.mobile.FloatingActionButton.property.kb0fb9e44'
              )
            "
            prop="more.url"
          >
            <AppLinkInput v-model="formData.more.url" />
          </el-form-item>
        </template>
      </el-card>
    </el-form>
  </ComponentContainerProperty>
</template>
<script setup lang="ts">
import { TitleBarProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'TitleBarProperty' })

const props = defineProps<{ modelValue: TitleBarProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)

// 表单校验
const rules = {}
</script>

<style scoped lang="scss"></style>
