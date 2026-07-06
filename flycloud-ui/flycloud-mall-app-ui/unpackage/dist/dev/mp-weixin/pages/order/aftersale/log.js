"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_api_trade_afterSale = require("../../../sheep/api/trade/afterSale.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (logItem + _easycom_s_layout)();
}
const logItem = () => "./log-item.js";
const _sfc_main = {
  __name: "log",
  setup(__props) {
    const state = common_vendor.reactive({
      list: []
    });
    async function getDetail(id) {
      const { data } = await sheep_api_trade_afterSale.AfterSaleApi.getAfterSaleLogList(id);
      state.list = data;
    }
    common_vendor.onLoad((options) => {
      state.aftersaleId = options.id;
      getDetail(options.id);
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(state.list, (item, index, i0) => {
          return {
            a: "2fedae8f-1-" + i0 + ",2fedae8f-0",
            b: common_vendor.p({
              item,
              index,
              data: state.list
            }),
            c: item.id
          };
        }),
        b: common_vendor.p({
          title: "售后进度"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-2fedae8f"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/order/aftersale/log.js.map
