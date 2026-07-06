import request from '@/sheep/request';
import { getAuthBaseUrl } from '@/sheep/config/server';
import { rsaEncrypt } from '@/utils/crypto';
// import { apiPath } from '@/sheep/config';

const BASIC_AUTHORIZATION = 'Basic Zmx5OmZseV9zZWNyZXQ=';

const AuthUtil = {
  // 使用帐号登录
  loginByAccount: async (data) => {
    const newData = {
      mobile: data.mobile || data.username,
      password: data.password,
      // grant_type: 'captcha',
      // scope: 'all',
      captchaCode: data.captchaCode,
    };
    newData.password = await rsaEncrypt(
      newData.password,
      import.meta.env.SHOPRO_FLY_CLOUD_LOGIN_PASSWORD_PUBLIC_KEY,
    );
    return request({
      // baseURL: getAuthBaseUrl(),
      // url: '/app/auth/token',
      url: getAuthBaseUrl() + '/app/auth/token',
      method: 'POST',
      data: newData,
      header: {
        'Content-Type': 'application/json',
        Authorization: BASIC_AUTHORIZATION,
      },
      custom: {
        isToken: false,
        showSuccess: true,
        loadingMsg: '登录中',
        successMsg: '登录成功',
      },
    });
  },

  // 使用手机 + 密码登录
  login: (data) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/login',
      method: 'POST',
      data,
      custom: {
        showSuccess: true,
        loadingMsg: '登录中',
        successMsg: '登录成功',
      },
    });
  },
  // 使用手机 + 验证码登录
  smsLogin: (data) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/sms-login',
      method: 'POST',
      data,
      custom: {
        showSuccess: true,
        loadingMsg: '登录中',
        successMsg: '登录成功',
      },
    });
  },
  // 发送手机验证码
  sendSmsCode: (mobile, scene) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/send-sms-code',
      method: 'POST',
      data: {
        mobile,
        scene,
      },
      custom: {
        loadingMsg: '发送中',
        showSuccess: true,
        successMsg: '发送成功',
      },
    });
  },
  // 登出系统
  logout: () => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/logOut',
      method: 'POST',
    });
  },
  // 刷新令牌
  refreshToken: (refreshToken) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/token',
      method: 'POST',
      data: {
        grant_type: 'refresh_token',
        refresh_token: refreshToken,
      },
      header: {
        Authorization: BASIC_AUTHORIZATION,
      },
      custom: {
        isToken: false,
        isRefreshToken: true,
        showLoading: false, // 不用加载中
        showError: false, // 不展示错误提示
      },
    });
  },
  // 社交授权的跳转
  socialAuthRedirect: (type, redirectUri) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/social-auth-redirect',
      method: 'GET',
      params: {
        type,
        redirectUri,
      },
      custom: {
        showSuccess: true,
        loadingMsg: '登录中',
      },
    });
  },
  // 社交快捷登录
  socialLogin: (type, code, state) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/social-login',
      method: 'POST',
      data: {
        type,
        code,
        state,
      },
      custom: {
        showSuccess: true,
        loadingMsg: '登录中',
      },
    });
  },
  // 微信小程序的一键登录
  weixinMiniAppLogin: (phoneCode, loginCode, state) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/weixin-mini-app-login',
      method: 'POST',
      data: {
        phoneCode,
        loginCode,
        state,
      },
      custom: {
        showSuccess: true,
        loadingMsg: '登录中',
        successMsg: '登录成功',
      },
    });
  },
  // 创建微信 JS SDK 初始化所需的签名
  createWeixinMpJsapiSignature: (url) => {
    return request({
      url: getAuthBaseUrl() + '/app/auth/create-weixin-jsapi-signature',
      method: 'POST',
      params: {
        url,
      },
      custom: {
        showError: false,
        showLoading: false,
      },
    });
  },
  //
};

export default AuthUtil;
