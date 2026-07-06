"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
if (!Array) {
  const _easycom_su_notice_bar2 = common_vendor.resolveComponent("su-notice-bar");
  _easycom_su_notice_bar2();
}
const _easycom_su_notice_bar = () => "../../ui/su-notice-bar/su-notice-bar.js";
if (!Math) {
  _easycom_su_notice_bar();
}
const _sfc_main = {
  __name: "s-notice-block",
  props: {
    data: {
      type: Object,
      default() {
      }
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(sheep_index.sheep).$url.cdn(__props.data.iconUrl),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go(__props.data.contents[0].url), "7f"),
        c: common_vendor.p({
          showIcon: false,
          scrollable: true,
          single: true,
          text: __props.data.contents[0].text,
          speed: 50,
          color: __props.data.textColor
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-587c9010"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-notice-block/s-notice-block.js.map
