"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_trade_brokerage = require("../../sheep/api/trade/brokerage.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
if (!Array) {
  const _component_emptyPage = common_vendor.resolveComponent("emptyPage");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_component_emptyPage + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "commission-ranking",
  setup(__props) {
    const tabMaps = ["周排行", "月排行"];
    const state = common_vendor.reactive({
      currentTab: 0,
      position: 0,
      // 排名
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 10
      },
      loadStatus: ""
    });
    async function switchTap(index) {
      state.currentTab = index;
      sheep_helper_utils.resetPagination(state.pagination);
      calculateTimes();
      getBrokerageRankList();
    }
    async function getBrokerageRankList() {
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUserChildSummaryPageByPrice({
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
      if (state.pagination.pageNum === 1) {
        getBrokerageRankNumber();
      }
    }
    async function getBrokerageRankNumber() {
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getRankByPrice({
        times: state.times
      });
      if (code !== 0) {
        return;
      }
      state.position = data;
    }
    function formatDate(date) {
      return sheep_index.sheep.$helper.timeFormat(date, "yyyy-mm-dd hh:MM:ss");
    }
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
      getBrokerageRankList();
    });
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getBrokerageRankList();
    }
    common_vendor.onReachBottom(() => loadMore());
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.position
      }, state.position ? {
        b: common_vendor.t(state.position)
      } : {}, {
        c: common_vendor.f(tabMaps, (item, index, i0) => {
          return {
            a: common_vendor.t(item),
            b: common_vendor.n(state.currentTab === index ? "font-color" : ""),
            c: index,
            d: common_vendor.o(($event) => switchTap(index), index)
          };
        }),
        d: common_vendor.f(state.pagination.list, (item, index, i0) => {
          return common_vendor.e({
            a: index <= 2
          }, index <= 2 ? {
            b: "/static/images/medal0" + (index + 1) + ".png"
          } : {
            c: common_vendor.t(index + 1)
          }, {
            d: common_vendor.unref(sheep_index.sheep).$url.cdn(item.avatar),
            e: common_vendor.t(item.nickname),
            f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.brokeragePrice)),
            g: index
          });
        }),
        e: state.pagination.list.length === 0
      }, state.pagination.list.length === 0 ? {
        f: common_vendor.p({
          title: "暂无排行～"
        })
      } : {}, {
        g: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        h: common_vendor.o(loadMore, "5b"),
        i: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        j: common_vendor.p({
          title: "佣金排行榜"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-4b4e4229"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/commission-ranking.js.map
