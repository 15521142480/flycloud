"use strict";
const sheep_request_index = require("../../request/index.js");
const sheep_config_server = require("../../config/server.js");
const sheep_config_index = require("../../config/index.js");
const SignInApi = {
  // 获得签到规则列表
  getSignInConfigList: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/sign-in/config/list",
      method: "GET"
    });
  },
  // 获得个人签到统计
  getSignInRecordSummary: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/sign-in/record/get-summary",
      method: "GET"
    });
  },
  // 签到
  createSignInRecord: () => {
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + "/member/sign-in/record/create",
      method: "POST"
    });
  },
  // 获得签到记录分页
  getSignRecordPage: (params) => {
    const queryString = Object.keys(params).map((key) => encodeURIComponent(key) + "=" + params[key]).join("&");
    return sheep_request_index.request({
      url: sheep_config_server.getSystemBaseUrl() + sheep_config_index.apiPath + `/member/sign-in/record/page?${queryString}`,
      method: "GET"
    });
  }
};
const __vite_glob_0_6 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: SignInApi
}, Symbol.toStringTag, { value: "Module" }));
exports.SignInApi = SignInApi;
exports.__vite_glob_0_6 = __vite_glob_0_6;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/member/signin.js.map
