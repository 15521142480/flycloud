"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
require("../../config/index.js");
const CAPTCHA_IMAGE_WIDTH = Number("360");
const CAPTCHA_IMAGE_HEIGHT = Number("240");
const CaptchaApi = {
  imageWidth: CAPTCHA_IMAGE_WIDTH,
  imageHeight: CAPTCHA_IMAGE_HEIGHT,
  // 获取图文点选验证码题目。
  getImageTextClickCaptcha: () => {
    return sheep_request_index.request({
      // baseURL: getAuthBaseUrl(),
      // url: '/captcha/getImageTextClickCaptcha',
      url: sheep_config_server.getAuthBaseUrl() + "/captcha/getImageTextClickCaptcha",
      method: "POST",
      custom: {
        isToken: false,
        showLoading: false
      }
    });
  },
  // 校验图文点选验证码坐标。
  checkImageTextClickCaptcha: (data) => {
    return sheep_request_index.request({
      url: sheep_config_server.getAuthBaseUrl() + "/captcha/checkGetImageTextClickCaptcha",
      method: "POST",
      data,
      custom: {
        isToken: false,
        showLoading: false
      }
    });
  }
};
const __vite_glob_0_0 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: CaptchaApi
}, Symbol.toStringTag, { value: "Module" }));
exports.CaptchaApi = CaptchaApi;
exports.__vite_glob_0_0 = __vite_glob_0_0;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/auth/captcha.js.map
