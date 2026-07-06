"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_trade_brokerage = require("../../../sheep/api/trade/brokerage.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  _easycom_uni_load_more2();
}
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
if (!Math) {
  _easycom_uni_load_more();
}
const _sfc_main = {
  __name: "commission-log",
  setup(__props) {
    const state = common_vendor.reactive({
      loadStatus: "",
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 8
      }
    });
    async function getLog() {
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageRecordPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    getLog();
    function loadmore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getLog();
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/commission/title2.png"),
        b: state.pagination.list
      }, state.pagination.list ? {
        c: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.title),
            b: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price)),
            c: common_vendor.t(common_vendor.unref(common_vendor.dayjs)(item.createTime).fromNow()),
            d: item.id
          };
        }),
        d: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/avatar/notice.png")
      } : {}, {
        e: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        f: common_vendor.o(loadmore, "8e"),
        g: common_vendor.p({
          status: state.loadStatus,
          color: "#333333"
        })
      } : {}, {
        h: common_vendor.o(loadmore, "7c")
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8c54e688"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/commission/components/commission-log.js.map
