<template>
  <div
    :class="prefixCls"
    class="relative h-[100%] lt-md:px-10px lt-sm:px-10px lt-xl:px-10px lt-xl:px-10px"
  >
    <div class="relative mx-auto h-full flex">
      <div
        :class="`${prefixCls}__left w-[55%] relative p-30px lt-xl:hidden overflow-x-hidden overflow-y-auto`"
      >
        <!-- 左上角的 logo + 系统标题 -->
        <div class="login-logo-box relative flex items-center text-white">
          <img alt="" class="mr-10px h-48px w-48px" src="@/assets/imgs/logo.png" />
          <span class="text-20px font-bold" style="font-size: 24px; font-weight: 700">{{
            t('app.title')
          }}</span>
        </div>

        <!-- 左边的背景图 + 提示语 -->
        <div class="relative z-1 h-[calc(100%-60px)] flex items-center justify-center">
          <TransitionGroup
            appear
            enter-active-class="animate__animated animate__bounceInLeft"
            tag="div"
            class="login-left-content"
          >
            <img key="1" alt="" class="login-left-img" src="@/assets/svgs/login_left.svg" />
            <div key="2" class="login-left-title">
              {{ t('app.homeTitle') }}
            </div>
            <div key="3" class="login-left-desc">{{ t('app.homeTitleDesc') }}</div>
          </TransitionGroup>
        </div>
      </div>

      <!--   右边区域   -->
      <div
        class="relative w-[45%] p-30px dark:bg-[var(--login-bg-color)] lt-sm:p-10px overflow-x-hidden overflow-y-auto"
      >
        <!-- 右上角的主题、语言选择 -->
        <div
          class="flex items-center justify-between at-2xl:justify-end at-xl:justify-end"
          style="color: var(--el-text-color-primary)"
        >
          <div class="flex items-center at-2xl:hidden at-xl:hidden">
            <img alt="" class="mr-10px h-48px w-48px" src="@/assets/imgs/logo.png" />
            <span class="text-20px font-bold">{{ t('app.title') }}</span>
          </div>
          <div class="login-preference flex items-center justify-end h-48px">
            <PreferenceDropdown :show-size="false" />
          </div>
        </div>
        <!-- 右边的登录界面 -->
        <Transition appear enter-active-class="animate__animated animate__bounceInRight">
          <div
            class="m-auto h-[calc(100%-60px)] w-[100%] flex items-center at-2xl:max-w-500px at-lg:max-w-500px at-md:max-w-500px at-xl:max-w-500px"
          >
            <!-- 账号登录 -->
            <LoginForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />

            <!-- 注册 -->
            <RegisterForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />

            <!-- 手机登录 -->
            <!--            <MobileForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />-->

            <!-- 二维码登录 -->
            <!--            <QrCodeForm class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />-->

            <!-- 三方登录 -->
            <!--            <SSOLoginVue class="m-auto h-auto p-20px lt-xl:(rounded-3xl light:bg-white)" />-->
          </div>
        </Transition>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { useDesign } from '@/hooks/web/useDesign'
import { PreferenceDropdown } from '@/layout/components/PreferenceDropdown'

// import { LoginForm, MobileForm, QrCodeForm, RegisterForm, SSOLoginVue } from './components'
import { LoginForm, RegisterForm } from './components'
const { t } = useI18n()
defineOptions({ name: 'Login' })
const { getPrefixCls } = useDesign()
const prefixCls = getPrefixCls('login')
</script>

<style lang="scss" scoped>
$prefix-cls: #{$namespace}-login;

.#{$prefix-cls} {
  overflow: auto;

  &__left {
    background-color: #fff;

    &::before {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      //margin-left: -8%;
      content: '';
      background-image: url('@/assets/svgs/login-bg.svg');
      background-repeat: no-repeat;
      background-position: 100%;
      background-size: auto 100%;
    }
  }
}

.dark .#{$prefix-cls} {
  &__left {
    background: linear-gradient(160deg, #1f2f4b 0%, #17243a 100%);

    &::before {
      background-image: none;
      background: radial-gradient(circle at 22% 28%, rgb(64 158 255 / 16%) 0, transparent 30%),
        linear-gradient(160deg, #1f2f4b 0%, #17243a 100%);
    }

    &::after {
      position: absolute;
      top: -15%;
      right: -360px;
      width: 520px;
      height: 130%;
      content: '';
      background: var(--login-bg-color);
      border-radius: 50%;
      box-shadow: -18px 0 0 rgb(41 49 70 / 35%);
    }
  }
}
</style>

<style lang="scss">
.dark .login-form {
  .el-divider__text {
    background-color: var(--login-bg-color);
  }

  .el-card {
    background-color: var(--login-bg-color);
  }
}

.login-logo-box {
  justify-content: center;
  transform: translate(-150px, 40px);
}

.login-preference {
  --app-chip-bg: var(--el-bg-color);
  --app-border: var(--el-border-color);
  --app-text: var(--el-text-color-primary);
}

.login-left-content {
  width: 520px;
  color: #fff;
  padding-bottom: 70px;
}

.login-left-img {
  display: block;
  width: 350px;
  margin: 2px 1px 50px 10px;
}

.login-left-title {
  margin-top: 20px;
  font-size: 1.775rem;
  font-weight: 700;
  line-height: 1.4;
}

.login-left-desc {
  margin-top: 18px;
  margin-left: 2px;
  font-size: 16px;
  font-weight: 500;
}
</style>
