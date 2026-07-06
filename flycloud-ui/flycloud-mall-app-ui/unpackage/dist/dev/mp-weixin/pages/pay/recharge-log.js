"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_api_pay_wallet = require("../../sheep/api/pay/wallet.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_empty2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_empty + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "recharge-log",
  setup(__props) {
    const state = common_vendor.reactive({
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 5
      },
      loadStatus: ""
    });
    async function getLogList(page = 1, list_rows = 5) {
      const { code, data } = await sheep_api_pay_wallet.PayWalletApi.getWalletRechargePage({
        pageNum: page,
        pageSize: list_rows
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getLogList();
    }
    common_vendor.onLoad(() => {
      getLogList();
    });
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.payPrice)),
            b: item.bonusPrice > 0
          }, item.bonusPrice > 0 ? {
            c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.bonusPrice))
          } : {}, {
            d: common_vendor.n(item.refundStatus === 10 ? "danger-color" : "success-color"),
            e: common_vendor.t(item.refundStatus === 10 ? "已退款" : "已支付"),
            f: common_vendor.n(item.refundStatus === 10 ? "danger-color" : "success-color"),
            g: common_vendor.t(item.payChannelName),
            h: common_vendor.t(item.payOrderChannelOrderNo),
            i: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.payTime, "yyyy-mm-dd hh:MM:ss")),
            j: item
          });
        }),
        b: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        c: common_vendor.p({
          icon: "/static/comment-empty.png",
          text: "暂无充值记录"
        })
      } : {}, {
        d: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        e: common_vendor.o(loadMore, "ba"),
        f: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        g: common_vendor.p({
          title: "充值记录"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-fcfe0364"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/pay/recharge-log.js.map
