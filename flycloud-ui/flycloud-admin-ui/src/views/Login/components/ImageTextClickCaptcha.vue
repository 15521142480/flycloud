<template>
  <div class="">
    <!--  图文点选验证码‌  -->
    <el-dialog
      v-model="captchaDialogVisible"
      :title="t('textCaptcha.title')"
      width="420px"
      align-center
      :close-on-click-modal="false"
      @closed="handleCaptchaClosed"
    >
      <div class="captcha-block">
        <div class="captcha-header">
          <div class="captcha-prompt">
            <span>{{ t('textCaptcha.prompt') }}：</span>
            <b v-for="word in targetWords" :key="word">{{ word }}</b>
          </div>
          <el-button
            :icon="Refresh"
            text
            circle
            :loading="captchaLoading"
            @click="loadCaptcha"
            style="font-size: 20px"
          />
          <div style="width: 5px"></div>
        </div>
        <div
          class="captcha-image-wrap"
          :class="{ passed: Boolean(props.textCaptchaData.captchaCode) }"
        >
          <img
            v-if="captchaChallenge"
            ref="captchaImageRef"
            :src="captchaChallenge.imageBase64"
            :alt="t('textCaptcha.imageAlt')"
            @click="handleCaptchaClick"
          />
          <div v-else class="captcha-placeholder">
            {{ captchaLoading ? t('common.loading') : t('textCaptcha.loadFailed') }}
          </div>
          <span
            v-for="(point, index) in clickPoints"
            :key="`${point.x}-${point.y}-${index}`"
            class="captcha-marker"
            :style="{ left: `${point.displayX}%`, top: `${point.displayY}%` }"
          >
            {{ index + 1 }}
          </span>
          <div v-if="props.textCaptchaData.captchaCode" class="captcha-passed">{{
            t('textCaptcha.passed')
          }}</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from '@/hooks/web/useI18n'
import { Refresh } from '@element-plus/icons-vue'
import type {
  ClickCaptchaChallengeVo,
  ClickCaptchaPointDto,
  TextCaptchaDataVo
} from '@/entity/auth'
import { getImageTextClickCaptchaApi, checkImageTextClickCaptchaApi } from '@/api/login'
import type { PropType } from 'vue'
const { t } = useI18n()
const props = defineProps({
  textCaptchaData: {
    type: Object as PropType<TextCaptchaDataVo>,
    default: () => ({
      imageTextClickCaptchaKey: '',
      imageTextClickCaptchaValue: '',
      captchaCode: ''
    })
  }
})

// const emit = defineEmits([
//   'update:imageTextClickCaptchaKey',
//   'success'
// ])
const emit = defineEmits<{
  (e: 'update:textCaptchaData', value: TextCaptchaDataVo): void
  (e: 'success', value: TextCaptchaDataVo): void
}>()

const captchaImageRef = ref<HTMLImageElement>()
const captchaLoading = ref(false)
const captchaDialogVisible = ref(false)
const captchaChallenge = ref<ClickCaptchaChallengeVo>()
// 图片点击的最多3个光标的x和y坐标信息
const clickPoints = ref<Array<ClickCaptchaPointDto & { displayX: number; displayY: number }>>([])
const targetWords = computed(() => captchaChallenge.value?.targetText.split('') || [])

/**
 * 父组件回调
 */
const openImageTextClickCaptchaProp = async () => {
  captchaDialogVisible.value = true
  await loadCaptcha()
}

defineExpose({ openImageTextClickCaptchaProp })

/**
 * 加载图文验证码，等待期间隐藏旧题目并合并重复刷新。
 */
const loadCaptcha = async () => {
  if (captchaLoading.value) {
    return
  }
  captchaLoading.value = true
  captchaChallenge.value = undefined
  clickPoints.value = []
  resetTextCaptchaData()

  try {
    const challenge = await getImageTextClickCaptchaApi()
    if (!captchaDialogVisible.value) {
      return
    }
    captchaChallenge.value = challenge
    emit('update:textCaptchaData', {
      ...props.textCaptchaData,
      imageTextClickCaptchaKey: captchaChallenge.value?.captchaId ?? ''
    })
  } catch {
    // 请求错误由公共拦截器提示，图片区域展示加载失败并允许重新刷新。
    captchaChallenge.value = undefined
  } finally {
    captchaLoading.value = false
  }
}

/**
 * 收集原图坐标；加载和校验期间忽略点击，避免提交正在被替换的旧题目。
 *
 * @param event 验证码图片上的鼠标点击事件
 */
const handleCaptchaClick = async (event: MouseEvent) => {
  if (
    captchaLoading.value ||
    !captchaChallenge.value ||
    props.textCaptchaData.captchaCode ||
    clickPoints.value.length >= 3
  ) {
    return
  }
  const image = captchaImageRef.value
  if (!image) {
    return
  }

  const rect = image.getBoundingClientRect()
  const displayX = ((event.clientX - rect.left) / rect.width) * 100
  const displayY = ((event.clientY - rect.top) / rect.height) * 100
  const x = Math.round(((event.clientX - rect.left) / rect.width) * image.naturalWidth)
  const y = Math.round(((event.clientY - rect.top) / rect.height) * image.naturalHeight)
  clickPoints.value.push({ x, y, displayX, displayY })

  const pointList = clickPoints.value.map(({ x, y }) => ({ x, y }))
  const pointListStr = pointList.map((item) => `${item.x},${item.y}`).join('; ')
  emit('update:textCaptchaData', {
    ...props.textCaptchaData,
    imageTextClickCaptchaValue: pointListStr
  })

  if (clickPoints.value.length === 3) {
    await checkCaptcha()
  }
}

/**
 * 校验当前题目，通过后通知父组件自动登录，失效或失败时重新加载题目。
 */
const checkCaptcha = async () => {
  const captchaId = captchaChallenge.value?.captchaId
  if (!captchaId || captchaLoading.value) {
    return
  }
  captchaLoading.value = true
  let reloadCaptcha = false
  try {
    const result = await checkImageTextClickCaptchaApi({
      captchaId,
      points: clickPoints.value.map(({ x, y }) => ({ x, y }))
    })
    // 用户关闭弹窗后到达的响应不能触发自动登录。
    if (!captchaDialogVisible.value || captchaChallenge.value?.captchaId !== captchaId) {
      return
    }
    if (!result?.captchaVerification) {
      ElMessage.error(t('textCaptcha.invalid'))
      reloadCaptcha = true
    } else {
      const verifiedData: TextCaptchaDataVo = {
        ...props.textCaptchaData,
        captchaCode: result.captchaVerification
      }
      emit('update:textCaptchaData', verifiedData)
      // 等待父组件同步验证码凭证，再通知其提交登录。
      await nextTick()
      ElMessage.success(t('textCaptcha.passed'))
      captchaDialogVisible.value = false
      emit('success', verifiedData)
    }
  } catch {
    // 公共拦截器已经提示错误，释放忙碌状态后再刷新，避免刷新被自身的并发保护阻止。
    reloadCaptcha = captchaDialogVisible.value && captchaChallenge.value?.captchaId === captchaId
  } finally {
    captchaLoading.value = false
  }
  if (reloadCaptcha) {
    await loadCaptcha()
  }
}

/**
 * 关闭弹窗
 */
const handleCaptchaClosed = () => {
  if (!props.textCaptchaData.captchaCode) {
    captchaChallenge.value = undefined
    clickPoints.value = []
    resetTextCaptchaData()
  }
}

const resetTextCaptchaData = () => {
  emit('update:textCaptchaData', {
    ...props.textCaptchaData,
    imageTextClickCaptchaKey: '',
    imageTextClickCaptchaValue: '',
    captchaCode: ''
  })
}
</script>

<style scoped lang="scss">
.login-page {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--app-login-bg);
}

.login-actions {
  position: absolute;
  top: 22px;
  right: 24px;
  z-index: 2;
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.login-panel {
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px;
}

.login-box {
  width: min(100%, 420px);
  padding: 36px 34px;
  border: 1px solid var(--app-border);
  border-radius: 8px;
  background: var(--app-surface);
  box-shadow: var(--app-shadow-lg);
  backdrop-filter: blur(20px);

  h1 {
    margin: 4px 0 26px;
    text-align: center;
    color: var(--app-text);
    font-size: 30px;
  }
}

.login-logo {
  width: 86px;
  height: 86px;
  display: block;
  object-fit: contain;
  margin: 0 auto 18px;
}

.eyebrow {
  margin: 0;
  text-align: center;
  color: var(--app-text-muted);
  font-size: 13px;
}

.login-button {
  width: 100%;
  height: 44px;
  border-radius: 8px;
}

.captcha-block {
  margin: 2px 0 4px;
}

.captcha-header {
  min-height: 42px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  color: var(--app-text-secondary);
  font-size: 15px;
}

.captcha-prompt {
  min-width: 0;
  display: inline-flex;
  align-items: center;
  justify-content: flex-start;
  gap: 8px;
  min-height: 30px;
  color: var(--app-text-secondary);
  white-space: nowrap;

  span {
    color: var(--app-text);
    font-size: 18px;
  }

  b {
    min-width: 30px;
    height: 30px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border-radius: 6px;
    background: var(--app-control-bg);
    color: var(--app-text);
    font-size: 18px;
  }
}

.captcha-image-wrap {
  position: relative;
  width: 100%;
  aspect-ratio: 3 / 2;
  overflow: hidden;
  border: 1px solid var(--app-border-strong);
  border-radius: 8px;
  background: var(--app-control-bg);
  cursor: crosshair;

  &.passed {
    cursor: default;
  }

  img {
    width: 100%;
    height: 100%;
    display: block;
    object-fit: cover;
    user-select: none;
  }
}

.captcha-placeholder,
.captcha-passed {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--app-text-muted);
  font-size: 14px;
}

.captcha-passed {
  background: var(--app-overlay-bg);
  color: #16833a;
  font-weight: 700;
  backdrop-filter: blur(3px);
}

.captcha-marker {
  position: absolute;
  width: 24px;
  height: 24px;
  transform: translate(-50%, -50%);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 2px solid #fff;
  border-radius: 50%;
  background: #0071e3;
  color: #fff;
  font-size: 12px;
  font-weight: 700;
  box-shadow: 0 6px 16px rgba(0, 113, 227, 0.32);
  pointer-events: none;
}

@media (max-width: 920px) {
  .login-panel {
    padding: 18px;
  }
}
</style>
