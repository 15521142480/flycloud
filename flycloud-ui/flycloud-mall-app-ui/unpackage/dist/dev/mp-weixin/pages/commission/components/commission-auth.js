"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_trade_brokerage = require("../../../sheep/api/trade/brokerage.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../../sheep/ui/su-popup/su-popup.js";
if (!Math) {
  _easycom_su_popup();
}
const _sfc_main = {
  __name: "commission-auth",
  setup(__props) {
    const state = common_vendor.reactive({
      show: false
    });
    common_vendor.onShow(async () => {
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUser();
      if (code === 0 && !(data == null ? void 0 : data.brokerageEnabled)) {
        state.show = true;
      }
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/commission/forbidden.png"),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.back(), "b6"),
        c: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.back(), "66"),
        d: common_vendor.o(($event) => state.show = false, "1d"),
        e: common_vendor.p({
          show: state.show,
          type: "center",
          round: "10",
          isMaskClick: false,
          maskBackgroundColor: "rgba(0, 0, 0, 0.7)"
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ec55d95e"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/commission/components/commission-auth.js.map
