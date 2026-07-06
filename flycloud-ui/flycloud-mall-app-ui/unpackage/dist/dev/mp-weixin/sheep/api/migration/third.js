"use strict";
const sheep_request_index = require("../../request/index.js");
const third = {
  // 苹果相关
  apple: {
    // 第三方登录
    login: (data) => sheep_request_index.request({
      url: "third/apple/login",
      method: "POST",
      data,
      custom: {
        showSuccess: true,
        loadingMsg: "登录中"
      }
    })
  }
};
const __vite_glob_0_10 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: third
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_10 = __vite_glob_0_10;
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/api/migration/third.js.map
