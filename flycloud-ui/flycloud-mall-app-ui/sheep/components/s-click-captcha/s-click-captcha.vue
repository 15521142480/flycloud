<!-- 图文点选验证码公共弹窗 -->
<template>
  <su-popup :show="show" type="center" round="10" :isMaskClick="false" @close="handleClose">
    <view class="captcha-wrap">
      <view class="captcha-head ss-flex ss-row-between ss-col-center">
        <view class="captcha-title">安全验证</view>
        <button class="ss-reset-button captcha-close" @tap="handleClose">×</button>
      </view>

      <view class="captcha-prompt-row ss-flex ss-col-center">
        <view class="captcha-prompt ss-flex ss-col-center">
          <text>请依次点击</text>
          <text v-for="(word, index) in targetWords" :key="`${word}-${index}`" class="captcha-word">
            {{ word }}
          </text>
        </view>
        <button
          class="ss-reset-button captcha-refresh"
          :disabled="state.loading"
          @tap="loadCaptcha"
        >
          换一张
        </button>
      </view>

      <view class="captcha-image-wrap" :class="{ loading: state.loading }">
        <image
          v-if="state.challenge"
          class="captcha-image"
          :src="state.challenge.imageBase64"
          mode="scaleToFill"
          @tap="handleImageTap"
        />
        <view v-else class="captcha-placeholder ss-flex ss-row-center ss-col-center"> 加载中 </view>
        <view
          v-for="(point, index) in state.points"
          :key="`${point.x}-${point.y}-${index}`"
          class="captcha-marker ss-flex ss-row-center ss-col-center"
          :style="{ left: `${point.displayX}%`, top: `${point.displayY}%` }"
        >
          {{ index + 1 }}
        </view>
      </view>
    </view>
  </su-popup>
</template>

<script setup>
  import { computed, getCurrentInstance, nextTick, reactive, watch } from 'vue';
  import sheep from '@/sheep';
  import CaptchaApi from '@/sheep/api/auth/captcha';

  const props = defineProps({
    show: {
      type: Boolean,
      default: false,
    },
  });

  const emits = defineEmits(['update:show', 'success', 'close']);
  const instance = getCurrentInstance();

  const state = reactive({
    loading: false,
    challenge: null,
    points: [],
  });

  const targetWords = computed(() => state.challenge?.targetText?.split('') || []);

  watch(
    () => props.show,
    async (value) => {
      if (value) {
        await loadCaptcha();
      }
    },
  );

  // 加载图文点选验证码题目。
  async function loadCaptcha() {
    state.loading = true;
    state.points = [];
    try {
      const res = await CaptchaApi.getImageTextClickCaptcha();
      if (res?.code === 0) {
        state.challenge = res.data;
        return;
      }
      sheep.$helper.toast(res?.msg || '验证码加载失败');
      state.challenge = null;
    } finally {
      state.loading = false;
    }
  }

  // 处理图片点选，并把展示坐标换算成后端原图坐标。
  async function handleImageTap(event) {
    if (!state.challenge || state.loading || state.points.length >= 3) {
      return;
    }

    const rect = await getImageRect();
    const clientPoint = getClientPoint(event);
    if (!rect || !clientPoint) {
      sheep.$helper.toast('验证码定位失败，请重试');
      await loadCaptcha();
      return;
    }

    const offsetX = Math.max(0, Math.min(clientPoint.clientX - rect.left, rect.width));
    const offsetY = Math.max(0, Math.min(clientPoint.clientY - rect.top, rect.height));
    const x = Math.round((offsetX / rect.width) * CaptchaApi.imageWidth);
    const y = Math.round((offsetY / rect.height) * CaptchaApi.imageHeight);

    state.points.push({
      x,
      y,
      displayX: (offsetX / rect.width) * 100,
      displayY: (offsetY / rect.height) * 100,
    });

    if (state.points.length === 3) {
      await verifyCaptcha();
    }
  }

  // 校验图文点选验证码，成功后把一次性凭证交给业务组件。
  async function verifyCaptcha() {
    state.loading = true;
    try {
      const res = await CaptchaApi.checkImageTextClickCaptcha({
        captchaId: state.challenge.captchaId,
        points: state.points.map(({ x, y }) => ({ x, y })),
      });
      if (res?.code === 0 && res.data?.captchaVerification) {
        emits('success', res.data.captchaVerification);
        emits('update:show', false);
        resetCaptcha();
        return;
      }
      sheep.$helper.toast(res?.msg || '验证码错误，请重新验证');
      await loadCaptcha();
    } finally {
      state.loading = false;
    }
  }

  // 获取验证码图片在当前页面上的实际位置。
  function getImageRect() {
    return new Promise((resolve) => {
      uni
        .createSelectorQuery()
        .in(instance?.proxy)
        .select('.captcha-image')
        .boundingClientRect((rect) => resolve(rect))
        .exec();
    });
  }

  // 兼容 H5 和移动端 tap 事件的坐标来源。
  function getClientPoint(event) {
    const touch = event.changedTouches?.[0] || event.touches?.[0];
    if (touch) {
      return { clientX: touch.clientX, clientY: touch.clientY };
    }
    if (event.detail?.x !== undefined && event.detail?.y !== undefined) {
      return { clientX: event.detail.x, clientY: event.detail.y };
    }
    if (event.clientX !== undefined && event.clientY !== undefined) {
      return { clientX: event.clientX, clientY: event.clientY };
    }
    return null;
  }

  function handleClose() {
    emits('update:show', false);
    emits('close');
    nextTick(resetCaptcha);
  }

  function resetCaptcha() {
    state.challenge = null;
    state.points = [];
  }
</script>

<style lang="scss" scoped>
  .captcha-wrap {
    width: 660rpx;
    padding: 34rpx;
    background: #fff;
    border-radius: 20rpx;
  }

  .captcha-head {
    margin-bottom: 24rpx;
  }

  .captcha-title {
    font-size: 34rpx;
    font-weight: 600;
    color: #333;
  }

  .captcha-close {
    width: 56rpx;
    height: 56rpx;
    line-height: 50rpx;
    font-size: 44rpx;
    color: #999;
  }

  .captcha-prompt-row {
    margin-bottom: 20rpx;
  }

  .captcha-prompt {
    font-size: 28rpx;
    color: #666;
  }

  .captcha-word {
    margin-left: 12rpx;
    font-size: 34rpx;
    font-weight: 700;
    color: #1f3568;
  }

  .captcha-image-wrap {
    position: relative;
    width: 100%;
    height: 394rpx;
    overflow: hidden;
    border-radius: 12rpx;
    background: #f6f7f9;
  }

  .captcha-image {
    width: 100%;
    height: 100%;
  }

  .captcha-placeholder {
    width: 100%;
    height: 100%;
    font-size: 28rpx;
    color: #999;
  }

  .captcha-marker {
    position: absolute;
    width: 44rpx;
    height: 44rpx;
    margin-left: -22rpx;
    margin-top: -22rpx;
    border-radius: 50%;
    background: var(--ui-BG-Main);
    color: #fff;
    font-size: 26rpx;
    font-weight: 700;
    box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.18);
  }

  .captcha-refresh {
    margin-left: 88rpx;
    width: 140rpx;
    height: 56rpx;
    line-height: 56rpx;
    border-radius: 28rpx;
    font-size: 26rpx;
    color: var(--ui-BG-Main);
    border: 2rpx solid var(--ui-BG-Main);
  }
</style>
