"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_trade_afterSale = require("../../../sheep/api/trade/afterSale.js");
const sheep_api_trade_delivery = require("../../../sheep/api/trade/delivery.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  _easycom_s_layout();
}
const _sfc_main = {
  __name: "return-delivery",
  setup(__props) {
    const state = common_vendor.reactive({
      id: 0,
      // 售后编号
      expressIndex: 0,
      // 选中的 expresses 下标
      expresses: []
      // 可选的快递列表
    });
    function bindPickerChange(e) {
      state.expressIndex = e.detail.value;
    }
    async function subRefund(e) {
      let data = {
        id: state.id,
        logisticsId: state.expresses[state.expressIndex].id,
        logisticsNo: e.detail.value.logisticsNo
      };
      const { code } = await sheep_api_trade_afterSale.AfterSaleApi.deliveryAfterSale(data);
      if (code !== 0) {
        return;
      }
      common_vendor.index.showToast({
        title: "填写退货成功"
      });
      sheep_index.sheep.$router.go("/pages/order/aftersale/detail", { id: state.id });
    }
    async function getExpressList() {
      const { code, data } = await sheep_api_trade_delivery.DeliveryApi.getDeliveryExpressList();
      if (code !== 0) {
        return;
      }
      state.expresses = data;
    }
    common_vendor.onLoad((options) => {
      if (!options.id) {
        sheep_index.sheep.$helper.toast(`缺少订单信息，请检查`);
        return;
      }
      state.id = options.id;
      getExpressList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.expresses.length > 0
      }, state.expresses.length > 0 ? {
        b: common_vendor.t(state.expresses[state.expressIndex].name),
        c: common_vendor.o(bindPickerChange, "c9"),
        d: state.expressIndex,
        e: state.expresses
      } : {}, {
        f: common_vendor.o(subRefund, "91"),
        g: common_vendor.p({
          title: "退货物流"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e2252b3c"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/order/aftersale/return-delivery.js.map
