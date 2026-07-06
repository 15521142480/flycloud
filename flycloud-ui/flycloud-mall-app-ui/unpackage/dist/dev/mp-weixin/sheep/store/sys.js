"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_store_app = require("./app.js");
const sys = common_vendor.defineStore("sys", {
  state: () => ({
    theme: "",
    // 主题,
    mode: "light",
    // 明亮模式、暗黑模式（暂未支持）
    modeAuto: false,
    // 跟随系统
    fontSize: 1
    // 设置默认字号等级(0-4)
  }),
  getters: {},
  actions: {
    setTheme(theme = "") {
      var _a;
      if (theme === "") {
        this.theme = ((_a = sheep_store_app.app().template) == null ? void 0 : _a.basic.theme) || "orange";
      } else {
        this.theme = theme;
      }
    }
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: "sys-store"
      }
    ]
  }
});
const __vite_glob_0_3 = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
  __proto__: null,
  default: sys
}, Symbol.toStringTag, { value: "Module" }));
exports.__vite_glob_0_3 = __vite_glob_0_3;
exports.sys = sys;
//# sourceMappingURL=../../../.sourcemap/mp-weixin/sheep/store/sys.js.map
