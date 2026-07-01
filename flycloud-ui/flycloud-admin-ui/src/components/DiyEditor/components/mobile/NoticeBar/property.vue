<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form label-width="80px" :model="formData" :rules="rules">
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.NoticeBar.property.k2e6872ea')"
        prop="iconUrl"
      >
        <UploadImg v-model="formData.iconUrl" directory="diy" height="48px">
          <template #tip>{{
            t('auto.components.DiyEditor.components.mobile.NoticeBar.property.kf03b0c2d')
          }}</template>
        </UploadImg>
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.NavigationBar.property.k4a2d8a28')"
        prop="backgroundColor"
      >
        <ColorInput v-model="formData.backgroundColor" />
      </el-form-item>
      <el-form-item
        :label="t('auto.components.DiyEditor.components.mobile.CouponCard.property.k07f568da')"
        prop="文字颜色"
      >
        <ColorInput v-model="formData.textColor" />
      </el-form-item>
      <el-card
        :header="t('auto.views.system.notice.NoticeForm.k7d23960d')"
        class="property-group"
        shadow="never"
      >
        <Draggable v-model="formData.contents">
          <template #default="{ element }">
            <el-form-item
              :label="t('auto.views.home.Index.k3f956953')"
              prop="text"
              label-width="40px"
            >
              <el-input
                v-model="element.text"
                :placeholder="
                  t('auto.components.DiyEditor.components.mobile.NoticeBar.property.kd553302a')
                "
              />
            </el-form-item>
            <el-form-item
              :label="t('auto.components.DiyEditor.components.mobile.MagicCube.property.k71502205')"
              prop="url"
              label-width="40px"
            >
              <AppLinkInput v-model="element.url" />
            </el-form-item>
          </template>
        </Draggable>
      </el-card>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { NoticeBarProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'NoticeBarProperty' })
// 表单校验
const rules = {
  content: [
    {
      required: true,
      message: t('auto.components.DiyEditor.components.mobile.NoticeBar.property.kd553302a'),
      trigger: 'blur'
    }
  ]
}

const props = defineProps<{ modelValue: NoticeBarProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
