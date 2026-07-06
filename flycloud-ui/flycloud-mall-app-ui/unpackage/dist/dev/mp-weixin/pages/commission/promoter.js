"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_trade_brokerage = require("../../sheep/api/trade/brokerage.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "promoter",
  setup(__props) {
    const tabMaps = ["周排行", "月排行"];
    const state = common_vendor.reactive({
      currentTab: 0,
      times: [],
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      loadStatus: ""
    });
    function switchTap(index) {
      if (state.currentTab === index) {
        return;
      }
      state.currentTab = index;
      sheep_helper_utils.resetPagination(state.pagination);
      calculateTimes();
      getRankList();
    }
    async function getRankList() {
      state.loadStatus = "loading";
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUserRankPageByUserCount({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        "times[0]": state.times[0],
        "times[1]": state.times[1]
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
      getRankList();
    }
    common_vendor.onReachBottom(() => loadMore());
    function calculateTimes() {
      let times;
      if (state.currentTab === 0) {
        times = sheep_helper_utils.getWeekTimes();
      } else {
        times = sheep_helper_utils.getMonthTimes();
      }
      state.times = [formatDate(times[0]), formatDate(times[1])];
    }
    common_vendor.onLoad(function() {
      calculateTimes();
      getRankList();
    });
    function formatDate(date) {
      return sheep_index.sheep.$helper.timeFormat(date, "yyyy-mm-dd hh:MM:ss");
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(tabMaps, (item, index, i0) => {
          return {
            a: common_vendor.t(item),
            b: common_vendor.n(state.currentTab === index ? "active" : ""),
            c: index,
            d: common_vendor.o(($event) => switchTap(index), index)
          };
        }),
        b: common_vendor.f(state.pagination.list, (item, index, i0) => {
          return common_vendor.e({
            a: index < 3
          }, index < 3 ? {
            b: common_vendor.t(index + 1),
            c: common_vendor.unref(sheep_index.sheep).$url.cdn(item.avatar),
            d: common_vendor.t(item.brokerageUserCount),
            e: common_vendor.t(item.nickname)
          } : {}, {
            f: index
          });
        }),
        c: common_vendor.f(state.pagination.list, (item, index, i0) => {
          return common_vendor.e({
            a: index > 2
          }, index > 2 ? {
            b: common_vendor.t(index + 1),
            c: common_vendor.unref(sheep_index.sheep).$url.cdn(item.avatar),
            d: common_vendor.t(item.nickname),
            e: common_vendor.t(item.brokerageUserCount)
          } : {}, {
            f: index
          });
        }),
        d: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        e: common_vendor.o(loadMore, "f6"),
        f: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        g: common_vendor.p({
          title: "推广人排行榜"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-fe20a205"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/promoter.js.map
