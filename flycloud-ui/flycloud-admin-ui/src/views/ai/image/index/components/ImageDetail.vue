<template>
  <el-drawer
    v-model="showDrawer"
    :title="t('auto.views.ai.image.index.components.ImageDetail.kd7cf3ec3')"
    @close="handleDrawerClose"
    custom-class="drawer-class"
  >
    <!-- 图片 -->
    <div class="item">
      <div class="body">
        <el-image
          class="image"
          :src="getFilePreviewUrl(detail?.picUrl)"
          :preview-src-list="[getFilePreviewUrl(detail.picUrl)]"
          preview-teleported
        />
      </div>
    </div>
    <!-- 时间 -->
    <div class="item">
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k89b4aa63') }}</div>
      <div class="body">
        <div>{{
          t('extra.k9685d376', { p0: formatTime(detail.createTime, 'yyyy-MM-dd HH:mm:ss') })
        }}</div>
        <div>{{
          t('extra.k8ca253c4', { p0: formatTime(detail.finishTime, 'yyyy-MM-dd HH:mm:ss') })
        }}</div>
      </div>
    </div>
    <!-- 模型 -->
    <div class="item">
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k98fd0cbd') }}</div>
      <div class="body"> {{ detail.model }}({{ detail.height }}x{{ detail.width }}) </div>
    </div>
    <!-- 提示词 -->
    <div class="item">
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k2f0bb123') }}</div>
      <div class="body">
        {{ detail.prompt }}
      </div>
    </div>
    <!-- 地址 -->
    <div class="item">
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k72582dc3') }}</div>
      <div class="body">
        {{ detail.picUrl }}
      </div>
    </div>
    <!-- StableDiffusion 专属区域 -->
    <div
      class="item"
      v-if="detail.platform === AiPlatformEnum.STABLE_DIFFUSION && detail?.options?.sampler"
    >
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k08d226e8') }}</div>
      <div class="body">
        {{
          StableDiffusionSamplers.find(
            (item: ImageModelVO) => item.key === detail?.options?.sampler
          )?.name
        }}
      </div>
    </div>
    <div
      class="item"
      v-if="
        detail.platform === AiPlatformEnum.STABLE_DIFFUSION && detail?.options?.clipGuidancePreset
      "
    >
      <div class="tip">CLIP</div>
      <div class="body">
        {{
          StableDiffusionClipGuidancePresets.find(
            (item: ImageModelVO) => item.key === detail?.options?.clipGuidancePreset
          )?.name
        }}
      </div>
    </div>
    <div
      class="item"
      v-if="detail.platform === AiPlatformEnum.STABLE_DIFFUSION && detail?.options?.stylePreset"
    >
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.ka4ce420c') }}</div>
      <div class="body">
        {{
          StableDiffusionStylePresets.find(
            (item: ImageModelVO) => item.key === detail?.options?.stylePreset
          )?.name
        }}
      </div>
    </div>
    <div
      class="item"
      v-if="detail.platform === AiPlatformEnum.STABLE_DIFFUSION && detail?.options?.steps"
    >
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k74dc89c5') }}</div>
      <div class="body">
        {{ detail?.options?.steps }}
      </div>
    </div>
    <div
      class="item"
      v-if="detail.platform === AiPlatformEnum.STABLE_DIFFUSION && detail?.options?.scale"
    >
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.kc86788d7') }}</div>
      <div class="body">
        {{ detail?.options?.scale }}
      </div>
    </div>
    <div
      class="item"
      v-if="detail.platform === AiPlatformEnum.STABLE_DIFFUSION && detail?.options?.seed"
    >
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k6387e53f') }}</div>
      <div class="body">
        {{ detail?.options?.seed }}
      </div>
    </div>
    <!-- Dall3 专属区域 -->
    <div class="item" v-if="detail.platform === AiPlatformEnum.OPENAI && detail?.options?.style">
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k6fe75ac9') }}</div>
      <div class="body">
        {{ Dall3StyleList.find((item: ImageModelVO) => item.key === detail?.options?.style)?.name }}
      </div>
    </div>
    <!-- Midjourney 专属区域 -->
    <div
      class="item"
      v-if="detail.platform === AiPlatformEnum.MIDJOURNEY && detail?.options?.version"
    >
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.kb4d3de59') }}</div>
      <div class="body">
        {{ detail?.options?.version }}
      </div>
    </div>
    <div
      class="item"
      v-if="detail.platform === AiPlatformEnum.MIDJOURNEY && detail?.options?.referImageUrl"
    >
      <div class="tip">{{ t('auto.views.ai.image.index.components.ImageDetail.k5149ae9c') }}</div>
      <div class="body">
        <el-image :src="getFilePreviewUrl(detail.options.referImageUrl)" />
      </div>
    </div>
  </el-drawer>
</template>

<script setup lang="ts">
import { getFilePreviewUrl } from '@/components/UploadFile/src/useUpload'
import { ImageApi, ImageVO } from '@/api/ai/image'
import {
  AiPlatformEnum,
  Dall3StyleList,
  ImageModelVO,
  StableDiffusionClipGuidancePresets,
  StableDiffusionSamplers,
  StableDiffusionStylePresets
} from '@/views/ai/utils/constants'
import { formatTime } from '@/utils'
const { t } = useI18n()
const showDrawer = ref<boolean>(false) // 是否显示
const detail = ref<ImageVO>({} as ImageVO) // 图片详细信息

const props = defineProps({
  show: {
    type: Boolean,
    require: true,
    default: false
  },
  id: {
    type: Number,
    required: true
  }
})

/** 关闭抽屉  */
const handleDrawerClose = async () => {
  emits('handleDrawerClose')
}

/** 监听 drawer 是否打开 */
const { show } = toRefs(props)
watch(show, async (newValue, oldValue) => {
  showDrawer.value = newValue as boolean
})

/**  获取图片详情  */
const getImageDetail = async (id: string) => {
  detail.value = await ImageApi.getImageMy(id)
}

/** 监听 id 变化，加载最新图片详情 */
const { id } = toRefs(props)
watch(id, async (newVal, oldVal) => {
  if (newVal) {
    await getImageDetail(newVal)
  }
})

const emits = defineEmits(['handleDrawerClose'])
</script>
<style scoped lang="scss">
.item {
  margin-bottom: 20px;
  width: 100%;
  overflow: hidden;
  word-wrap: break-word;

  .header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  .tip {
    font-weight: bold;
    font-size: 16px;
  }

  .body {
    margin-top: 10px;
    color: #616161;

    .taskImage {
      border-radius: 10px;
    }
  }
}
</style>
