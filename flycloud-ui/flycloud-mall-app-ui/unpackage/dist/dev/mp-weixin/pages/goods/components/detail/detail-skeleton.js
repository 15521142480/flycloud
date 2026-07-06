"use strict";
const common_vendor = require("../../../../common/vendor.js");
const sheep_index = require("../../../../sheep/index.js");
if (!Array) {
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  _easycom_su_fixed2();
}
const _easycom_su_fixed = () => "../../../../sheep/ui/su-fixed/su-fixed.js";
if (!Math) {
  _easycom_su_fixed();
}
const _sfc_main = {
  __name: "detail-skeleton",
  setup(__props) {
    const sys = common_vendor.computed(() => sheep_index.sheep.$store("sys"));
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          bottom: true,
          placeholder: true,
          bg: "bg-white"
        }),
        b: common_vendor.n("theme-" + sys.value.mode),
        c: common_vendor.n("main-" + sys.value.theme),
        d: common_vendor.n("font-" + sys.value.fontSize)
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-929a32c8"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-skeleton.js.map
