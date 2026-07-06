"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const _sfc_main = {
  __name: "commission-info",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "7417b452": common_vendor.unref(headerBg)
    }));
    const userInfo = common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/commission/background.png");
    common_vendor.reactive({
      showMoney: false
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(userInfo.value.avatar),
        b: common_vendor.t(userInfo.value.nickname),
        c: common_vendor.s(_ctx.__cssVars())
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-2ef4f675"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/commission/components/commission-info.js.map
