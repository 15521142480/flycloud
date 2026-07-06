"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../../sheep/index.js");
const sheep_api_pay_wallet = require("../../../sheep/api/pay/wallet.js");
const sheep_hooks_useGoods = require("../../../sheep/hooks/useGoods.js");
const sheep_helper_utils = require("../../../sheep/helper/utils.js");
if (!Array) {
  const _easycom_uni_datetime_picker2 = common_vendor.resolveComponent("uni-datetime-picker");
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_datetime_picker2 + _easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_empty2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_uni_datetime_picker = () => "../../../uni_modules/uni-datetime-picker/components/uni-datetime-picker/uni-datetime-picker.js";
const _easycom_su_tabs = () => "../../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_empty = () => "../../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_load_more = () => "../../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_datetime_picker + _easycom_su_tabs + _easycom_su_sticky + _easycom_s_empty + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "money",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "05bea019": common_vendor.unref(headerBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/user/wallet_card_bg.png");
    const state = common_vendor.reactive({
      showMoney: false,
      date: [],
      // 筛选的时间段
      currentTab: 0,
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 8
      },
      summary: {
        totalIncome: 0,
        totalExpense: 0
      },
      loadStatus: "",
      today: ""
    });
    const tabMaps = [
      {
        name: "全部",
        value: ""
      },
      {
        name: "收入",
        value: "1"
      },
      {
        name: "支出",
        value: "2"
      }
    ];
    const userWallet = common_vendor.computed(() => sheep_index.sheep.$store("user").userWallet);
    const dateFilterText = common_vendor.computed(() => {
      if (state.date[0] === state.date[1]) {
        return state.date[0];
      } else {
        return state.date.join("~");
      }
    });
    async function getLogList() {
      state.loadStatus = "loading";
      const { data, code } = await sheep_api_pay_wallet.PayWalletApi.getWalletTransactionPage({
        pageNum: state.pagination.pageNum,
        pageSize: state.pagination.pageSize,
        type: tabMaps[state.currentTab].value,
        "createTime[0]": state.date[0] + " 00:00:00",
        "createTime[1]": state.date[1] + " 23:59:59"
      });
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    async function getSummary() {
      const { data, code } = await sheep_api_pay_wallet.PayWalletApi.getWalletTransactionSummary({
        createTime: [state.date[0] + " 00:00:00", state.date[1] + " 23:59:59"]
      });
      if (code !== 0) {
        return;
      }
      state.summary = data;
    }
    common_vendor.onLoad(() => {
      state.today = common_vendor.dayjs().format("YYYY-MM-DD");
      state.date = [state.today, state.today];
      getLogList();
      getSummary();
      sheep_index.sheep.$store("user").getWallet();
    });
    function onChange(e) {
      state.currentTab = e.index;
      sheep_helper_utils.resetPagination(state.pagination);
      getLogList();
      getSummary();
    }
    function onChangeTime(e) {
      state.date[0] = e[0];
      state.date[1] = e[e.length - 1];
      sheep_helper_utils.resetPagination(state.pagination);
      getLogList();
      getSummary();
    }
    common_vendor.onReachBottom(() => {
      if (state.loadStatus === "noMore") {
        return;
      }
      state.pagination.pageNum++;
      getLogList();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(($event) => state.showMoney = !state.showMoney, "50"),
        b: common_vendor.n(state.showMoney ? "cicon-eye" : "cicon-eye-off"),
        c: common_vendor.t(state.showMoney ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(userWallet.value.balance) : "*****"),
        d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/pay/recharge"), "3e"),
        e: common_vendor.t(dateFilterText.value),
        f: common_vendor.o(onChangeTime, "27"),
        g: common_vendor.o(($event) => state.data = $event, "30"),
        h: common_vendor.p({
          type: "daterange",
          end: state.today,
          modelValue: state.data
        }),
        i: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.totalIncome)),
        j: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.totalExpense)),
        k: common_vendor.o(onChange, "1c"),
        l: common_vendor.p({
          list: tabMaps,
          scrollable: false,
          current: state.currentTab
        }),
        m: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        n: common_vendor.p({
          text: "暂无数据",
          icon: "/static/data-empty.png"
        })
      } : {}, {
        o: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        p: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(item.title),
            b: item.price >= 0
          }, item.price >= 0 ? {
            c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price))
          } : {
            d: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price))
          }, {
            e: item.id
          });
        }),
        q: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.createTime, "yyyy-mm-dd hh:MM:ss"))
      } : {}, {
        r: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        s: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        t: common_vendor.s(_ctx.__cssVars()),
        v: common_vendor.p({
          title: "钱包"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-69f29528"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/user/wallet/money.js.map
