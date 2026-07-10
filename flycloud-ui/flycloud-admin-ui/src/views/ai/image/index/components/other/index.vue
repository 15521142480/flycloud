<!-- dall3 -->
<template>
  <div class="prompt">
    <el-text tag="b">{{ t('auto.views.ai.image.index.components.other.k1813eae1') }}</el-text>
    <el-text tag="p">{{ t('auto.views.ai.image.index.components.other.kcf07e703') }}</el-text>
    <el-input
      v-model="prompt"
      maxlength="1024"
      rows="5"
      class="w-100% mt-15px"
      input-style="border-radius: 7px;"
      :placeholder="t('auto.views.ai.image.index.components.other.k379873d7')"
      show-word-limit
      type="textarea"
    />
  </div>
  <div class="hot-words">
    <div>
      <el-text tag="b">{{ t('auto.views.ai.image.index.components.other.k21b37b6a') }}</el-text>
    </div>
    <el-space wrap class="word-list">
      <el-button
        round
        class="btn"
        :type="selectHotWord === hotWord ? 'primary' : 'default'"
        v-for="hotWord in ImageHotWords"
        :key="hotWord"
        @click="handleHotWordClick(hotWord)"
      >
        {{ hotWord }}
      </el-button>
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">{{ t('auto.views.ai.image.index.components.other.ke4b9d694') }}</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-select
        v-model="otherPlatform"
        placeholder="Select"
        size="large"
        class="!w-350px"
        @change="handlerPlatformChange"
      >
        <el-option
          v-for="item in OtherPlatformEnum"
          :key="item.key"
          :label="item.name"
          :value="item.key"
        />
      </el-select>
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">{{ t('auto.views.ai.image.index.components.other.k98fd0cbd') }}</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-select v-model="model" placeholder="Select" size="large" class="!w-350px">
        <el-option v-for="item in models" :key="item.key" :label="item.name" :value="item.key" />
      </el-select>
    </el-space>
  </div>
  <div class="group-item">
    <div>
      <el-text tag="b">{{ t('auto.views.ai.image.index.components.other.k85626028') }}</el-text>
    </div>
    <el-space wrap class="group-item-body">
      <el-input
        v-model="width"
        type="number"
        class="w-170px"
        :placeholder="t('auto.views.ai.image.index.components.other.k5da942a5')"
      />
      <el-input
        v-model="height"
        type="number"
        class="w-170px"
        :placeholder="t('auto.views.ai.image.index.components.other.kf3f33764')"
      />
    </el-space>
  </div>
  <div class="btns">
    <el-button type="primary" size="large" round :loading="drawIn" @click="handleGenerateImage">{{
      drawIn ? t('extra.k80a72054') : t('extra.k7ecb6362')
    }}</el-button>
  </div>
</template>
<script setup lang="ts">
// @ts-nocheck
import { ImageApi, ImageDrawReqVO, ImageVO } from '@/api/ai/image'
import {
  AiPlatformEnum,
  ChatGlmModels,
  ImageHotWords,
  ImageModelVO,
  OtherPlatformEnum,
  QianFanModels,
  TongYiWanXiangModels
} from '@/views/ai/utils/constants'
const { t } = useI18n()
const message = useMessage() // 消息弹窗

// 定义属性
const drawIn = ref<boolean>(false) // 生成中
const selectHotWord = ref<string>('') // 选中的热词
// 表单
const prompt = ref<string>('') // 提示词
const width = ref<number>(512) // 图片宽度
const height = ref<number>(512) // 图片高度
const otherPlatform = ref<string>(AiPlatformEnum.TONG_YI) // 平台
const models = ref<ImageModelVO[]>(TongYiWanXiangModels) // 模型  TongYiWanXiangModels、QianFanModels
const model = ref<string>(models.value[0].key) // 模型

const emits = defineEmits(['onDrawStart', 'onDrawComplete']) // 定义 emits

/** 选择热词 */
const handleHotWordClick = async (hotWord: string) => {
  // 情况一：取消选中
  if (selectHotWord.value == hotWord) {
    selectHotWord.value = ''
    return
  }

  // 情况二：选中
  selectHotWord.value = hotWord // 选中
  prompt.value = hotWord // 替换提示词
}

/** 图片生成 */
const handleGenerateImage = async () => {
  // 二次确认
  await message.confirm(t('auto.views.ai.image.index.components.other.k47808f71'))
  try {
    // 加载中
    drawIn.value = true
    // 回调
    emits('onDrawStart', AiPlatformEnum.STABLE_DIFFUSION)
    // 发送请求
    const form = {
      platform: otherPlatform.value,
      model: model.value, // 模型
      prompt: prompt.value, // 提示词
      width: width.value, // 图片宽度
      height: height.value, // 图片高度
      options: {}
    } as unknown as ImageDrawReqVO
    await ImageApi.drawImage(form)
  } finally {
    // 回调
    emits('onDrawComplete', AiPlatformEnum.STABLE_DIFFUSION)
    // 加载结束
    drawIn.value = false
  }
}

/** 填充值 */
const settingValues = async (detail: ImageVO) => {
  prompt.value = detail.prompt
  width.value = detail.width
  height.value = detail.height
}

/** 平台切换 */
const handlerPlatformChange = async (platform: string) => {
  // 切换平台，切换模型、风格
  if (AiPlatformEnum.TONG_YI === platform) {
    models.value = TongYiWanXiangModels
  } else if (AiPlatformEnum.YI_YAN === platform) {
    models.value = QianFanModels
  } else if (AiPlatformEnum.ZHI_PU === platform) {
    models.value = ChatGlmModels
  } else {
    models.value = []
  }
  // 切换平台，默认选择一个风格
  if (models.value.length > 0) {
    model.value = models.value[0].key
  } else {
    model.value = ''
  }
}

/** 暴露组件方法 */
defineExpose({ settingValues })
</script>
<style scoped lang="scss">
// 提示词
.prompt {
}

// 热词
.hot-words {
  display: flex;
  flex-direction: column;
  margin-top: 30px;

  .word-list {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: start;
    margin-top: 15px;

    .btn {
      margin: 0;
    }
  }
}

// 模型
.group-item {
  margin-top: 30px;

  .group-item-body {
    margin-top: 15px;
    width: 100%;
  }
}

.btns {
  display: flex;
  justify-content: center;
  margin-top: 50px;
}
</style>
