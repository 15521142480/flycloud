<template>
  <ComponentContainerProperty v-model="formData.style">
    <el-form label-width="80px" :model="formData">
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.Carousel.property.k8b52bd0a')"
        class="property-group"
        shadow="never"
      >
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.Carousel.property.k393a6c91')"
          prop="type"
        >
          <el-radio-group v-model="formData.type">
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.Carousel.property.kc8d09cf9')
              "
              placement="bottom"
            >
              <el-radio-button value="default">
                <Icon icon="system-uicons:carousel" />
              </el-radio-button>
            </el-tooltip>
            <el-tooltip
              class="item"
              :content="
                t('auto.components.DiyEditor.components.mobile.Carousel.property.k46b64b21')
              "
              placement="bottom"
            >
              <el-radio-button value="card">
                <Icon icon="ic:round-view-carousel" />
              </el-radio-button>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.Carousel.property.k2a51a4f6')"
          prop="indicator"
        >
          <el-radio-group v-model="formData.indicator">
            <el-radio value="dot">{{
              t('auto.components.DiyEditor.components.mobile.Carousel.property.k172e4496')
            }}</el-radio>
            <el-radio value="number">{{
              t('auto.components.DiyEditor.components.mobile.Carousel.property.k7a4dc825')
            }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.Carousel.property.k891d585b')"
          prop="autoplay"
        >
          <el-switch v-model="formData.autoplay" />
        </el-form-item>
        <el-form-item
          :label="t('auto.components.DiyEditor.components.mobile.Carousel.property.k1bb2c36f')"
          prop="interval"
          v-if="formData.autoplay"
        >
          <el-slider
            v-model="formData.interval"
            :max="10"
            :min="0.5"
            :step="0.5"
            show-input
            input-size="small"
            :show-input-controls="false"
          />
          <el-text type="info">{{
            t('auto.components.DiyEditor.components.mobile.Carousel.property.ka8be7968')
          }}</el-text>
        </el-form-item>
      </el-card>
      <el-card
        :header="t('auto.components.DiyEditor.components.mobile.Carousel.property.kffc00e74')"
        class="property-group"
        shadow="never"
      >
        <Draggable v-model="formData.items" :empty-item="{ type: 'img' }">
          <template #default="{ element }">
            <el-form-item
              :label="t('auto.components.DiyEditor.components.mobile.Carousel.property.ke4e46c72')"
              prop="type"
              class="m-b-8px!"
              label-width="40px"
            >
              <el-radio-group v-model="element.type">
                <el-radio value="img">{{
                  t('auto.components.DiyEditor.components.mobile.Carousel.property.kbe8da62e')
                }}</el-radio>
                <el-radio value="video">{{
                  t('auto.components.DiyEditor.components.mobile.Carousel.property.kfa4e33b6')
                }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item
              :label="t('auto.components.DiyEditor.components.mobile.Carousel.property.kbe8da62e')"
              class="m-b-8px!"
              label-width="40px"
              v-if="element.type === 'img'"
            >
              <UploadImg
                v-model="element.imgUrl"
                draggable="false"
                height="80px"
                width="100%"
                class="min-w-80px"
              />
            </el-form-item>
            <template v-else>
              <el-form-item
                :label="
                  t('auto.components.DiyEditor.components.mobile.Carousel.property.kb8bb31e1')
                "
                class="m-b-8px!"
                label-width="40px"
              >
                <UploadImg
                  v-model="element.imgUrl"
                  draggable="false"
                  height="80px"
                  width="100%"
                  class="min-w-80px"
                />
              </el-form-item>
              <el-form-item
                :label="
                  t('auto.components.DiyEditor.components.mobile.Carousel.property.kfa4e33b6')
                "
                class="m-b-8px!"
                label-width="40px"
              >
                <UploadFile
                  v-model="element.videoUrl"
                  :file-type="['mp4']"
                  :limit="1"
                  :file-size="100"
                  class="min-w-80px"
                />
              </el-form-item>
            </template>
            <el-form-item label="链接" class="m-b-8px!" label-width="40px">
              <AppLinkInput v-model="element.url" />
            </el-form-item>
          </template>
        </Draggable>
      </el-card>
    </el-form>
  </ComponentContainerProperty>
</template>

<script setup lang="ts">
import { CarouselProperty } from './config'
import { usePropertyForm } from '@/components/DiyEditor/util'
const { t } = useI18n()
defineOptions({ name: 'CarouselProperty' })

const props = defineProps<{ modelValue: CarouselProperty }>()
const emit = defineEmits(['update:modelValue'])
const { formData } = usePropertyForm(props.modelValue, emit)
</script>

<style scoped lang="scss"></style>
