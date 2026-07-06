"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const SocialApi = {
  // 获得社交用户
  getSocialUser: (type) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/social-user/get",
      method: "GET",
      params: {
        type
      },
      custom: {
        showLoading: false
      }
    });
  },
  // 社交绑定
  socialBind: (type, code, state) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/social-user/bind",
      method: "POST",
      data: {
        type,
        code,
        state
      },
      custom: {
        custom: {
          showSuccess: true,
          loadingMsg: "绑定中",
          successMsg: "绑定成功"
        }
      }
    });
  },
  // 社交绑定
  socialUnbind: (type, openid) => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/social-user/unbind",
      method: "DELETE",
      data: {
        type,
        openid
      },
      custom: {
        showLoading: false,
        loadingMsg: "解除绑定",
        successMsg: "解绑成功"
      }
    });
  },
  // 获取订阅消息模板列表
  getSubscribeTemplateList: () => sheep_request_index.request({
    url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/social-user/get-subscribe-template-list",
    method: "GET",
    custom: {
      showError: false,
      showLoading: false
    }
  }),
  // 获取微信小程序码
  getWxaQrcode: async (path, query) => {
    return await sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/social-user/wxa-qrcode",
      method: "POST",
      data: {
        scene: query,
        path,
        checkPath: false
        // TODO 开发环境暂不检查 path 是否存在
      }
    });
  }
};
const __vite_glob_0_7 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: SocialApi
}, Symbol.toStringTag, { value: "Module" }));
exports.SocialApi = SocialApi;
exports.__vite_glob_0_7 = __vite_glob_0_7;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/member/social.js.map
