"use strict";
const sheep_request_index = require("../../request/index.js");
const DiyApi = {
  getUsedDiyTemplate: () => {
    return sheep_request_index.request({
      url: "/promotion/diy-template/used",
      method: "GET",
      custom: {
        showError: false,
        showLoading: false
      }
    });
  },
  getDiyTemplate: (id) => {
    return sheep_request_index.request({
      url: "/promotion/diy-template/get",
      method: "GET",
      params: {
        id
      },
      custom: {
        showError: false,
        showLoading: false
      }
    });
  },
  getDiyPage: (id) => {
    return sheep_request_index.request({
      url: "/promotion/diy-page/get",
      method: "GET",
      params: {
        id
      }
    });
  }
};
const __vite_glob_0_24 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: DiyApi
}, Symbol.toStringTag, { value: "Module" }));
exports.DiyApi = DiyApi;
exports.__vite_glob_0_24 = __vite_glob_0_24;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/promotion/diy.js.map
