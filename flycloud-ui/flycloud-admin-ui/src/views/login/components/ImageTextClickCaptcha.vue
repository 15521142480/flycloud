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
          <el-button :icon="Refresh" text circle :loading="captchaLoading" @click="loadCaptcha" />
        </div>
        <div class="captcha-image-wrap" :class="{ passed: Boolean(props.imageTextClickCaptchaValue) }">
          <img
            v-if="captchaChallenge"
            ref="captchaImageRef"
            :src="captchaChallenge.imageBase64"
            :alt="t('captcha.imageAlt')"
            @click="handleCaptchaClick"
          />
          <div v-else class="captcha-placeholder">{{ t('common.loading') }}</div>
          <span
            v-for="(point, index) in clickPoints"
            :key="`${point.x}-${point.y}-${index}`"
            class="captcha-marker"
            :style="{ left: `${point.displayX}%`, top: `${point.displayY}%` }"
          >
          {{ index + 1 }}
        </span>
          <div v-if="props.imageTextClickCaptchaValue" class="captcha-passed">{{ t('textCaptcha.passed') }}</div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from '@/hooks/web/useI18n'
import { Refresh } from '@element-plus/icons-vue'
const { t } = useI18n()
import type { ClickCaptchaChallengeVo, ClickCaptchaPointDto } from '@/entity/auth'
import { getImageTextClickCaptchaApi, checkImageTextClickCaptchaApi } from '@/api/login'


const props = defineProps({
  imageTextClickCaptchaKey: {
    type: String,
    default: ''
  },
  imageTextClickCaptchaValue: {
    type: String,
    default: ''
  }
})


const emit = defineEmits([
  'update:imageTextClickCaptchaKey',
  'update:imageTextClickCaptchaValue',
  'success'
])


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


const loginAfterCaptcha = async () => {

  // loading.value = true
  try {
    // await authStore.login(form)
    ElMessage.success(t('login.success'))

  } catch (error) {
    const message = error instanceof Error ? error.message : t('')
    ElMessage.error(message || t('login.accountPasswordError'))
    emit('update:imageTextClickCaptchaValue', '')
  } finally {
    // loading.value = false
  }
}

/**
 * 加载图文点选验证码‌
 */
const loadCaptcha = async () => {

  captchaLoading.value = true
  emit('update:imageTextClickCaptchaKey', '')
  emit('update:imageTextClickCaptchaValue', '')
  clickPoints.value = []
  try {
    captchaChallenge.value = await getImageTextClickCaptchaApi()
  } catch (error) {
    const message = error instanceof Error ? error.message : '图文点选验证码‌出现未知错误'
    ElMessage.error(message)
  } finally {
    captchaLoading.value = false
  }
}

/**
 * 点击图片事件
 * @param event
 */
const handleCaptchaClick = async (event: MouseEvent) => {

  if (!captchaChallenge.value || props.imageTextClickCaptchaValue || clickPoints.value.length >= 3) {
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
  const pointListStr = pointList.map(item => `${item.x},${item.y}`).join(';')
  emit('update:imageTextClickCaptchaValue', pointListStr)


  if (clickPoints.value.length === 3) {
    await checkCaptcha()
  }
}

/**
 * 验证图文点选验证码‌
 */
const checkCaptcha = async () => {

  if (!captchaChallenge.value) {
    return
  }
  captchaLoading.value = true
  try {
    const result = await checkImageTextClickCaptchaApi({
      captchaId: captchaChallenge.value.captchaId,
      points: clickPoints.value.map(({ x, y }) => ({ x, y }))
    })
    emit('update:imageTextClickCaptchaValue', result.captchaVerification)
    ElMessage.success(t('captcha.passed'))
    captchaDialogVisible.value = false
    await loginAfterCaptcha()
  } catch (error) {
    const message = error instanceof Error ? error.message : t('captcha.invalid')
    ElMessage.error(message)
    await loadCaptcha()
  } finally {
    captchaLoading.value = false
  }
}

const handleCaptchaClosed = () => {
  if (!props.imageTextClickCaptchaValue) {
    captchaChallenge.value = undefined
    clickPoints.value = []
  }
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
