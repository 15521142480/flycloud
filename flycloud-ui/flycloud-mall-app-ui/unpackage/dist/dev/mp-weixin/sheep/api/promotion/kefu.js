"use strict";
const sheep_request_index = require("../../request/index.js");
const KeFuApi = {
  sendKefuMessage: (data) => {
    return sheep_request_index.request({
      url: "/promotion/kefu-message/send",
      method: "POST",
      data,
      custom: {
        auth: true,
        showLoading: true,
        loadingMsg: "发送中",
        showSuccess: true,
        successMsg: "发送成功"
      }
    });
  },
  getKefuMessageList: (params) => {
    return sheep_request_index.request({
      url: "/promotion/kefu-message/list",
      method: "GET",
      params,
      custom: {
        auth: true,
        showLoading: false
      }
    });
  }
};
const __vite_glob_0_25 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: KeFuApi
}, Symbol.toStringTag, { value: "Module" }));
exports.KeFuApi = KeFuApi;
exports.__vite_glob_0_25 = __vite_glob_0_25;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/kefu.js.map
