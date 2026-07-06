"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_trade_brokerage = require("../../sheep/api/trade/brokerage.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  _easycom_s_layout();
}
const _sfc_main = {
  __name: "team",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "768616fe": common_vendor.unref(headerBg)
    }));
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    common_vendor.computed(() => sheep_index.sheep.$store("user").userInfo);
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/user/withdraw_bg.png");
    common_vendor.onPageScroll((e) => {
      state.scrollTop = e.scrollTop <= 100;
    });
    let sort = common_vendor.ref();
    const state = common_vendor.reactive({
      summary: {},
      pagination: {
        pageNum: 1,
        pageSize: 8,
        list: [],
        total: 0
      },
      loadStatus: "",
      // ↓ 新 ui 逻辑
      level: 1,
      nickname: common_vendor.ref(""),
      sortKey: "",
      isAsc: ""
    });
    function submitForm() {
      state.pagination.list = [];
      getTeamList();
    }
    async function getTeamList() {
      state.loadStatus = "loading";
      let { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUserChildSummaryPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        level: state.level,
        "sortingField.order": state.isAsc,
        "sortingField.field": state.sortKey,
        nickname: state.nickname
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    function setType(e) {
      state.pagination.list = [];
      state.pagination.pageNum = 1;
      state.level = e + "";
      getTeamList();
    }
    function setSort(sortKey, isAsc) {
      state.pagination.list = [];
      sort = sortKey + isAsc.toUpperCase();
      state.isAsc = isAsc;
      state.sortKey = sortKey;
      getTeamList();
    }
    common_vendor.onLoad(async () => {
      await getTeamList();
      let { data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUserSummary();
      state.summary = data;
    });
    function loadMore() {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getTeamList();
    }
    common_vendor.onReachBottom(() => {
      loadMore();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(state.summary.firstBrokerageUserCount + state.summary.secondBrokerageUserCount || 0),
        b: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 108) + "rpx"
        }),
        c: common_vendor.t(state.summary.firstBrokerageUserCount || 0),
        d: common_vendor.n(state.level == 1 ? "item on" : "item"),
        e: common_vendor.o(($event) => setType(1), "f9"),
        f: common_vendor.t(state.summary.secondBrokerageUserCount || 0),
        g: common_vendor.n(state.level == 2 ? "item on" : "item"),
        h: common_vendor.o(($event) => setType(2), "c6"),
        i: common_vendor.o(submitForm, "d9"),
        j: state.nickname,
        k: common_vendor.o(($event) => state.nickname = $event.detail.value, "e9"),
        l: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/search.png"),
        m: common_vendor.o(submitForm, "e2"),
        n: common_vendor.unref(sort) === "userCountDESC"
      }, common_vendor.unref(sort) === "userCountDESC" ? {
        o: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort1.png"),
        p: common_vendor.o(($event) => setSort("userCount", "asc"), "e0")
      } : common_vendor.unref(sort) === "userCountASC" ? {
        r: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort3.png"),
        s: common_vendor.o(($event) => setSort("userCount", "desc"), "3f")
      } : {
        t: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort2.png"),
        v: common_vendor.o(($event) => setSort("userCount", "desc"), "f2")
      }, {
        q: common_vendor.unref(sort) === "userCountASC",
        w: common_vendor.unref(sort) === "priceDESC"
      }, common_vendor.unref(sort) === "priceDESC" ? {
        x: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort1.png"),
        y: common_vendor.o(($event) => setSort("price", "asc"), "8f")
      } : common_vendor.unref(sort) === "priceASC" ? {
        A: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort3.png"),
        B: common_vendor.o(($event) => setSort("price", "desc"), "f0")
      } : {
        C: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort2.png"),
        D: common_vendor.o(($event) => setSort("price", "desc"), "aa")
      }, {
        z: common_vendor.unref(sort) === "priceASC",
        E: common_vendor.unref(sort) === "orderCountDESC"
      }, common_vendor.unref(sort) === "orderCountDESC" ? {
        F: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort1.png"),
        G: common_vendor.o(($event) => setSort("orderCount", "asc"), "aa")
      } : common_vendor.unref(sort) === "orderCountASC" ? {
        I: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort3.png"),
        J: common_vendor.o(($event) => setSort("orderCount", "desc"), "e7")
      } : {
        K: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/sort2.png"),
        L: common_vendor.o(($event) => setSort("orderCount", "desc"), "81")
      }, {
        H: common_vendor.unref(sort) === "orderCountASC",
        M: common_vendor.f(state.pagination.list, (item, index, i0) => {
          return {
            a: common_vendor.unref(sheep_index.sheep).$url.cdn(item.avatar),
            b: common_vendor.t(item.nickname),
            c: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.brokerageTime, "yyyy-mm-dd hh:MM:ss")),
            d: common_vendor.t(item.brokerageUserCount || 0),
            e: common_vendor.t(item.brokerageOrderCount || 0),
            f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.brokeragePrice) || 0),
            g: index
          };
        }),
        N: state.pagination.list.length === 0
      }, state.pagination.list.length === 0 ? {} : {}, {
        O: common_vendor.n(state.scrollTop ? "team-wrap" : ""),
        P: common_vendor.s(_ctx.__cssVars()),
        Q: common_vendor.p({
          title: "我的团队",
          navbar: "inner"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-42603af6"]]);
_sfc_main.__runtimeHooks = 1;
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/team.js.map
