"use strict";
const common_vendor = require("../../common/vendor.js");
const modal = common_vendor.defineStore("modal", {
  state: () => ({
    auth: "",
    // 授权弹框 accountLogin|smsLogin|resetPassword|changeMobile|changePassword|changeUsername
    share: false,
    // 分享弹框
    menu: false,
    // 快捷菜单弹框
    advHistory: [],
    // 广告弹框记录
    lastTimer: {
      // 短信验证码计时器，为了防止刷新请求做了持久化
      smsLogin: 0,
      changeMobile: 0,
      resetPassword: 0,
      changePassword: 0
    }
  }),
  persist: {
    enabled: true,
    strategies: [
      {
        key: "modal-store",
        paths: ["lastTimer", "advHistory"]
      }
    ]
  }
});
const __vite_glob_0_2 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: modal
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_2 = __vite_glob_0_2;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/store/modal.js.map
