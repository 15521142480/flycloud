"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
const sheep_api_trade_brokerage = require("../../sheep/api/trade/brokerage.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_empty2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_su_tabs = () => "../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_su_tabs + _easycom_su_sticky + _easycom_s_empty + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "order",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "83224c2e": common_vendor.unref(headerBg)
    }));
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/user/withdraw_bg.png");
    common_vendor.onPageScroll((e) => {
      state.scrollTop = e.scrollTop <= 100;
    });
    const state = common_vendor.reactive({
      totals: 0,
      // 累计推广订单（单）
      scrollTop: false,
      currentTab: 0,
      loadStatus: "",
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 8
      }
    });
    const tabMaps = [
      {
        name: "全部",
        value: -1
      },
      {
        name: "待结算",
        value: 0
        // 待结算
      },
      {
        name: "已结算",
        value: 1
        // 已结算
      }
    ];
    function onTabsChange(e) {
      sheep_helper_utils.resetPagination(state.pagination);
      state.currentTab = e.index;
      getOrderList();
    }
    async function getOrderList() {
      state.loadStatus = "loading";
      const tab = tabMaps[state.currentTab];
      const queryParams = {
        pageSize: state.pagination.pageSize,
        pageNum: state.pagination.pageNum,
        bizType: 1,
        // 获得推广佣金
        status: tab.value
      };
      if (tab.value < 0) {
        delete queryParams.status;
      }
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageRecordPage(queryParams);
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
      if (state.currentTab === 0) {
        state.totals = data.total;
      }
    }
    common_vendor.onLoad(() => {
      getOrderList();
    });
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getOrderList();
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(state.totals),
        b: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 108) + "rpx"
        }),
        c: common_vendor.o(onTabsChange, "c4"),
        d: common_vendor.p({
          list: tabMaps,
          scrollable: false,
          current: state.currentTab
        }),
        e: common_vendor.p({
          bgColor: "#fff"
        }),
        f: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.bizId),
            b: common_vendor.t(item.status === 0 ? "待结算" : item.status === 1 ? "已结算" : "已取消"),
            c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price)),
            d: common_vendor.t(item.title),
            e: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.createTime, "yyyy-mm-dd hh:MM:ss")),
            f: item
          };
        }),
        g: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        h: common_vendor.p({
          icon: "/static/order-empty.png",
          text: "暂无订单"
        })
      } : {}, {
        i: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        j: common_vendor.o(loadMore, "27"),
        k: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        l: common_vendor.n(state.scrollTop ? "order-warp" : ""),
        m: common_vendor.s(_ctx.__cssVars()),
        n: common_vendor.p({
          title: "分销订单",
          navbar: "inner"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-3971f30d"]]);
_sfc_main.__runtimeHooks = 1;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/order.js.map
