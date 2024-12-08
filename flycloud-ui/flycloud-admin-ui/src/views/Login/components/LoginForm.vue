<template>
  <el-form
    v-show="getShow"
    ref="formLogin"
    :model="loginData.loginForm"
    :rules="LoginRules"
    class="login-form"
    label-position="top"
    label-width="120px"
    size="large"
  >
    <el-row style="margin-right: -10px; margin-left: -10px">

      <!-- 登录字段 -->
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item>
          <LoginFormTitle style="width: 100%" />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item prop="username">
          <el-input
            v-model="loginData.loginForm.username"
            :placeholder="t('login.usernamePlaceholder')"
            :prefix-icon="iconAvatar"
          />
        </el-form-item>
      </el-col>

      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item prop="password">
          <el-input
            v-model="loginData.loginForm.password"
            :placeholder="t('login.passwordPlaceholder')"
            :prefix-icon="iconLock"
            show-password
            type="password"
            @keyup.enter="handleLogin()"
          />
        </el-form-item>
      </el-col>

      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item prop="code" >
          <el-col :span="16">
            <el-input v-model="loginData.loginForm.code" :placeholder="t('login.codePlaceholder')"/>
          </el-col>
          <el-col :span="1" style="text-align: center">&nbsp;</el-col>
          <el-col :span="7">
              <img v-if="loginData.loginForm.codeKey" :src="codeUrl" style="cursor: pointer;" @click="getCode" alt=""/>
              <XButton v-else @click="getCode()">{{t('login.getCode')}}</XButton>
          </el-col>
        </el-form-item>
      </el-col>

      <!-- 记住我 || 忘记密码 -->
      <el-col
        :span="24"
        style="padding-right: 10px; padding-left: 10px; margin-top: -20px; margin-bottom: -20px"
      >
        <el-form-item>
          <el-row justify="space-between" style="width: 100%">
            <el-col :span="6">
              <el-checkbox v-model="loginData.loginForm.rememberMe">
                {{ t('login.remember') }}
              </el-checkbox>
            </el-col>
            <el-col :offset="6" :span="12">
              <el-link style="float: right" type="primary">{{ t('login.forgetPassword') }}</el-link>
            </el-col>
          </el-row>
        </el-form-item>
      </el-col>

      <!-- 登录 -->
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item>
          <XButton
            :loading="loginLoading"
            :title="t('login.login')"
            class="w-[100%]"
            type="primary"
            @click="handleLogin()"
          />
        </el-form-item>
      </el-col>

      <!-- 验证码 -->
<!--      <Verify-->
<!--        ref="verify"-->
<!--        :captchaType="captchaType"-->
<!--        :imgSize="{ width: '400px', height: '200px' }"-->
<!--        mode="pop"-->
<!--        @success="handleLogin"-->
<!--      />-->

      <!-- 注册 || 其他登录 -->
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item>
          <el-row :gutter="5" justify="space-between" style="width: 100%">
            <el-col :span="8">
<!--              <XButton-->
<!--                :title="t('login.btnMobile')"-->
<!--                class="w-[100%]"-->
<!--                @click="setLoginState(LoginStateEnum.MOBILE)"-->
<!--              />-->
            </el-col>
            <el-col :span="8">
<!--              <XButton-->
<!--                :title="t('login.btnQRCode')"-->
<!--                class="w-[100%]"-->
<!--                @click="setLoginState(LoginStateEnum.QR_CODE)"-->
<!--              />-->
            </el-col>
            <el-col :span="8">
              <XButton
                :title="t('login.btnRegister')"
                class="w-[100%]"
                @click="setLoginState(LoginStateEnum.REGISTER)"
              />
            </el-col>
          </el-row>
        </el-form-item>
      </el-col>

<!--      <el-divider content-position="center">{{ t('login.otherLogin') }}</el-divider>-->
<!--      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">-->
<!--        <el-form-item>-->
<!--          <div class="w-[100%] flex justify-between">-->
<!--            <Icon-->
<!--              v-for="(item, key) in socialList"-->
<!--              :key="key"-->
<!--              :icon="item.icon"-->
<!--              :size="30"-->
<!--              class="anticon cursor-pointer"-->
<!--              color="#999"-->
<!--              @click="doSocialLogin(item.type)"-->
<!--            />-->
<!--          </div>-->
<!--        </el-form-item>-->
<!--      </el-col>-->

    </el-row>
  </el-form>
</template>
<script lang="ts" setup>
import { ElLoading } from 'element-plus'
import LoginFormTitle from './LoginFormTitle.vue'
import type { RouteLocationNormalizedLoaded } from 'vue-router'

import { useIcon } from '@/hooks/web/useIcon'

import * as authUtil from '@/utils/auth'
import { usePermissionStore } from '@/store/modules/permission'
import * as LoginApi from '@/api/login'
import { LoginStateEnum, useFormValid, useLoginState } from './useLogin'

defineOptions({ name: 'LoginForm' })

const { t } = useI18n()
// const message = useMessage()
// const iconHouse = useIcon({ icon: 'ep:house' })
const iconAvatar = useIcon({ icon: 'ep:avatar' })
const iconLock = useIcon({ icon: 'ep:lock' })
const formLogin = ref()
const { validForm } = useFormValid(formLogin)
const { setLoginState, getLoginState } = useLoginState()
const { currentRoute, push } = useRouter()
const permissionStore = usePermissionStore()
const redirect = ref<string>('')
const loginLoading = ref(false)
// const verify = ref()
// const captchaType = ref('blockPuzzle') // blockPuzzle 滑块 clickWord 点击文字
const getShow = computed(() => unref(getLoginState) === LoginStateEnum.LOGIN)
const codeUrl = ref()


/**
 * 验证
 */
const LoginRules = {
  username: [required],
  password: [required],
  code: [required],
}

/**
 * 登录数据
 */
const loginData = reactive({
  isShowPassword: false,
  loginForm: {
    username: import.meta.env.VITE_APP_DEFAULT_LOGIN_USERNAME || '',
    password: import.meta.env.VITE_APP_DEFAULT_LOGIN_PASSWORD || '',
    grant_type: 'captcha',
    // grant_type: 'password',
    scope: 'all',
    codeKey: '', // 验证码key
    code: '', // 验证码
    rememberMe: true // 默认记录我。如果不需要，可手动修改
  }
})

const socialList = [
  { icon: 'ant-design:wechat-filled', type: 30 },
  { icon: 'ant-design:dingtalk-circle-filled', type: 20 },
  { icon: 'ant-design:github-filled', type: 0 },
  { icon: 'ant-design:alipay-circle-filled', type: 0 }
]

/**
 * 获取验证码
 */
const getCode = async () => {

  // // 情况一，未开启：则直接登录
  // if (loginData.captchaEnable === 'false') {
  //   await handleLogin({})
  // } else {
  //   // 情况二，已开启：则展示验证码；只有完成验证码的情况，才进行登录
  //   // 弹出验证码
  //   verify.value.show()
  // }

  const res = await LoginApi.getCodeApi()
  if (!res) {
    return
  }
  loginData.loginForm.codeKey = res.key
  codeUrl.value = res.codeUrl
}

/**
 * 记住我
 */
const getLoginFormCache = () => {
  const loginForm = authUtil.getLoginForm()
  if (loginForm) {
    loginData.loginForm = {
      ...loginData.loginForm,
      username: loginForm.username ? loginForm.username : loginData.loginForm.username,
      password: loginForm.password ? loginForm.password : loginData.loginForm.password,
      rememberMe: loginForm.rememberMe
    }
  }
}

const loading = ref() // ElLoading.service 返回的实例

/**
 * 登录
 */
const handleLogin = async () => {
  loginLoading.value = true
  try {
    // await getTenantId()
    const data = await validForm()
    if (!data) {
      return
    }
    const loginDataLoginForm = { ...loginData.loginForm }
    const res = await LoginApi.login(loginDataLoginForm)
    if (!res) {
      return
    }
    loading.value = ElLoading.service({
      lock: true,
      text: '正在加载系统中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    if (loginDataLoginForm.rememberMe) {
      authUtil.setLoginForm(loginDataLoginForm)
    } else {
      authUtil.removeLoginForm()
    }
    authUtil.setToken(res.data)
    if (!redirect.value) {
      redirect.value = '/'
    }
    // 判断是否为SSO登录
    if (redirect.value.indexOf('sso') !== -1) {
      window.location.href = window.location.href.replace('/login?redirect=', '')
    } else {
      push({ path: redirect.value || permissionStore.addRouters[0].path })
    }
  } finally {
    loginLoading.value = false
    loading.value.close()
  }
}

/**
 * 社交登录
 */
// const doSocialLogin = async (type: number) => {
//   if (type === 0) {
//     message.error('此方式未配置')
//   } else {
//     loginLoading.value = true
//     if (loginData.tenantEnable === 'true') {
//       // 尝试先通过 tenantName 获取租户
//       await getTenantId()
//       // 如果获取不到，则需要弹出提示，进行处理
//       if (!authUtil.getTenantId()) {
//         try {
//           const data = await message.prompt('请输入租户名称', t('common.reminder'))
//           if (data?.action !== 'confirm') throw 'cancel'
//           const res = await LoginApi.getTenantIdByName(data.value)
//           authUtil.setTenantId(res)
//         } catch (error) {
//           if (error === 'cancel') return
//         } finally {
//           loginLoading.value = false
//         }
//       }
//     }
//     // 计算 redirectUri
//     // tricky: type、redirect需要先encode一次，否则钉钉回调会丢失。
//     // 配合 Login/SocialLogin.vue#getUrlValue() 使用
//     const redirectUri =
//       location.origin +
//       '/social-login?' +
//       encodeURIComponent(`type=${type}&redirect=${redirect.value || '/'}`)
//
//     // 进行跳转
//     const res = await LoginApi.socialAuthRedirect(type, encodeURIComponent(redirectUri))
//     window.location.href = res
//   }
// }
watch(
  () => currentRoute.value,
  (route: RouteLocationNormalizedLoaded) => {
    redirect.value = route?.query?.redirect as string
  },
  {
    immediate: true
  }
)
onMounted(() => {
  getCode()
  getLoginFormCache()
  // getTenantByWebsite()
})
</script>

<style lang="scss" scoped>
:deep(.anticon) {
  &:hover {
    color: var(--el-color-primary) !important;
  }
}

.login-code {
  float: right;
  width: 100%;
  height: 38px;

  img {
    width: 100%;
    height: auto;
    max-width: 100px;
    vertical-align: middle;
    cursor: pointer;
  }
}
</style>
