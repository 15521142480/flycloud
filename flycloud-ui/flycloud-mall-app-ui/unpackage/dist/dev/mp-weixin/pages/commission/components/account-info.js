"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_trade_brokerage = require("../../../sheep/api/trade/brokerage.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_uni_icons2 = common_vendor.resolveComponent("uni-icons");
  _easycom_uni_icons2();
}
const _easycom_uni_icons = () => "../../../uni_modules/uni-icons/components/uni-icons/uni-icons.js";
if (!Math) {
  _easycom_uni_icons();
}
const _sfc_main = {
  __name: "account-info",
  setup(__props) {
    common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const state = common_vendor.reactive({
      showMoney: false,
      summary: {}
    });
    common_vendor.onMounted(async () => {
      let { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUserSummary();
      if (code === 0) {
        state.summary = data || {};
      }
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.p({
          type: state.showMoney ? "eye-filled" : "eye-slash-filled",
          color: "#A57A55",
          size: "20"
        }),
        b: common_vendor.o(($event) => state.showMoney = !state.showMoney, "ac"),
        c: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/commission/wallet"), "a1"),
        d: common_vendor.t(state.showMoney ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.brokeragePrice || 0) : "***"),
        e: common_vendor.t(state.showMoney ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.yesterdayPrice || 0) : "***"),
        f: common_vendor.t(state.showMoney ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.withdrawPrice || 0) : "***")
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-12734ec3"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/commission/components/account-info.js.map
