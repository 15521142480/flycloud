"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const utils_crypto_rsa = require("../../../utils/crypto/rsa.js");
const BASIC_AUTHORIZATION = "Basic Zmx5OmZseV9zZWNyZXQ=";
const AuthUtil = {
  // 使用帐号登录
  loginByAccount: async (data) => {
    const newData = {
      mobile: data.mobile || data.username,
      password: data.password,
      // grant_type: 'captcha',
      // scope: 'all',
      captchaCode: data.captchaCode
    };
    newData.password = await utils_crypto_rsa.rsaEncrypt(
      newData.password,
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqG/pmVfnO8KrCL1zHzsnx2hlwmbvFqSGna89QU9a5q0JO+pdeppBhHbHI8Lao/Hsk4jSQbA1C+vSKi018A6a4WILlbRMYZaStouc2abYCZsejL/SMccsCFz+c06WPzFJ5VkxAoTTmVJ0pCViVJEZdQuXMc7GtQRP7VME+DhrVDThII6s/2oxJLNhPxCejGZz84jhekHIedF38/SRz2gj5QQH3v+zYC2SWjH+WAoyLckFQ7A3FBYt9qaFTLA1ObpSmKEeZZvxWadovMW2gw6tQPWbzxalY00Dhei7hGi8yU29bhF032SQaMIgyuIiYW4wLdk2wdjOY1JtJDGgjhmjXwIDAQAB"
    );
    return sheep_request_index.request({
      // baseURL: getAuthBaseUrl(),
      // url: '/app/auth/token',
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/token",
      method: "POST",
      data: newData,
      header: {
        "Content-Type": "application/json",
        Authorization: BASIC_AUTHORIZATION
      },
      custom: {
        isToken: false,
        showSuccess: true,
        loadingMsg: "登录中",
        successMsg: "登录成功"
      }
    });
  },
  // 使用手机 + 密码登录
  login: (data) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/login",
      method: "POST",
      data,
      custom: {
        showSuccess: true,
        loadingMsg: "登录中",
        successMsg: "登录成功"
      }
    });
  },
  // 使用手机 + 验证码登录
  smsLogin: (data) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/sms-login",
      method: "POST",
      data,
      custom: {
        showSuccess: true,
        loadingMsg: "登录中",
        successMsg: "登录成功"
      }
    });
  },
  // 发送手机验证码
  sendSmsCode: (mobile, scene) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/send-sms-code",
      method: "POST",
      data: {
        mobile,
        scene
      },
      custom: {
        loadingMsg: "发送中",
        showSuccess: true,
        successMsg: "发送成功"
      }
    });
  },
  // 登出系统
  logout: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/logOut",
      method: "POST"
    });
  },
  // 刷新令牌
  refreshToken: (refreshToken) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/token",
      method: "POST",
      data: {
        grant_type: "refresh_token",
        refresh_token: refreshToken
      },
      header: {
        Authorization: BASIC_AUTHORIZATION
      },
      custom: {
        isToken: false,
        isRefreshToken: true,
        showLoading: false,
        // 不用加载中
        showError: false
        // 不展示错误提示
      }
    });
  },
  // 社交授权的跳转
  socialAuthRedirect: (type, redirectUri) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/social-auth-redirect",
      method: "GET",
      params: {
        type,
        redirectUri
      },
      custom: {
        showSuccess: true,
        loadingMsg: "登录中"
      }
    });
  },
  // 社交快捷登录
  socialLogin: (type, code, state) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/social-login",
      method: "POST",
      data: {
        type,
        code,
        state
      },
      custom: {
        showSuccess: true,
        loadingMsg: "登录中"
      }
    });
  },
  // 微信小程序的一键登录
  weixinMiniAppLogin: (phoneCode, loginCode, state) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/weixin-mini-app-login",
      method: "POST",
      data: {
        phoneCode,
        loginCode,
        state
      },
      custom: {
        showSuccess: true,
        loadingMsg: "登录中",
        successMsg: "登录成功"
      }
    });
  },
  // 创建微信 JS SDK 初始化所需的签名
  createWeixinMpJsapiSignature: (url) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/app/auth/create-weixin-jsapi-signature",
      method: "POST",
      params: {
        url
      },
      custom: {
        showError: false,
        showLoading: false
      }
    });
  }
  //
};
const __vite_glob_0_4 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: AuthUtil
}, Symbol.toStringTag, { value: "Module" }));
exports.AuthUtil = AuthUtil;
exports.__vite_glob_0_4 = __vite_glob_0_4;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/member/auth.js.map
