"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_trade_brokerage = require("../../sheep/api/trade/brokerage.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_helper_utils = require("../../sheep/helper/utils.js");
const sheep_api_pay_transfer = require("../../sheep/api/pay/transfer.js");
if (!Array) {
  const _easycom_uni_datetime_picker2 = common_vendor.resolveComponent("uni-datetime-picker");
  const _easycom_su_tabs2 = common_vendor.resolveComponent("su-tabs");
  const _easycom_su_sticky2 = common_vendor.resolveComponent("su-sticky");
  const _easycom_s_empty2 = common_vendor.resolveComponent("s-empty");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  const _easycom_uni_load_more2 = common_vendor.resolveComponent("uni-load-more");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_datetime_picker2 + _easycom_su_tabs2 + _easycom_su_sticky2 + _easycom_s_empty2 + _easycom_uni_easyinput2 + _easycom_su_popup2 + _easycom_uni_load_more2 + _easycom_s_layout2)();
}
const _easycom_uni_datetime_picker = () => "../../uni_modules/uni-datetime-picker/components/uni-datetime-picker/uni-datetime-picker.js";
const _easycom_su_tabs = () => "../../sheep/ui/su-tabs/su-tabs.js";
const _easycom_su_sticky = () => "../../sheep/ui/su-sticky/su-sticky.js";
const _easycom_s_empty = () => "../../sheep/components/s-empty/s-empty.js";
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_su_popup = () => "../../sheep/ui/su-popup/su-popup.js";
const _easycom_uni_load_more = () => "../../uni_modules/uni-load-more/components/uni-load-more/uni-load-more.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_datetime_picker + _easycom_su_tabs + _easycom_su_sticky + _easycom_s_empty + _easycom_uni_easyinput + _easycom_su_popup + _easycom_uni_load_more + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "wallet",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "bce371da": common_vendor.unref(headerBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/user/wallet_card_bg.png");
    const state = common_vendor.reactive({
      showMoney: false,
      summary: {},
      // 分销信息
      today: "",
      date: [],
      currentTab: 0,
      pagination: {
        list: [],
        total: 0,
        pageNum: 1,
        pageSize: 8
      },
      loadStatus: "",
      price: void 0,
      showModal: false
    });
    const tabMaps = [
      {
        name: "分佣",
        value: "1"
      },
      {
        name: "提现",
        value: "2"
      }
    ];
    const dateFilterText = common_vendor.computed(() => {
      if (state.date[0] === state.date[1]) {
        return state.date[0];
      } else {
        return state.date.join("~");
      }
    });
    async function getLogList() {
      state.loadStatus = "loading";
      let { code, data } = await (state.currentTab === 0 ? sheep_api_trade_brokerage.BrokerageApi.getBrokerageRecordPage({
        pageSize: state.pagination.pageSize,
        pageNum: state.pagination.pageNum,
        "createTime[0]": state.date[0] + " 00:00:00",
        "createTime[1]": state.date[1] + " 23:59:59"
      }) : sheep_api_trade_brokerage.BrokerageApi.getBrokerageWithdrawPage({
        pageSize: state.pagination.pageSize,
        pageNum: state.pagination.pageNum,
        "createTime[0]": state.date[0] + " 00:00:00",
        "createTime[1]": state.date[1] + " 23:59:59"
      }));
      if (code !== 0) {
        return;
      }
      state.pagination.list = common_vendor.concat(state.pagination.list, data.list);
      state.pagination.total = data.total;
      state.loadStatus = state.pagination.list.length < state.pagination.total ? "more" : "noMore";
    }
    function onChangeTab(e) {
      sheep_helper_utils.resetPagination(state.pagination);
      state.currentTab = e.index;
      getLogList();
    }
    function onChangeTime(e) {
      state.date[0] = e[0];
      state.date[1] = e[e.length - 1];
      sheep_helper_utils.resetPagination(state.pagination);
      getLogList();
    }
    async function onConfirm() {
      if (state.price <= 0) {
        sheep_index.sheep.$helper.toast("请输入正确的金额");
        return;
      }
      common_vendor.index.showModal({
        title: "提示",
        content: "确认把您的佣金转入到余额钱包中？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_trade_brokerage.BrokerageApi.createBrokerageWithdraw({
            type: 1,
            // 钱包
            price: state.price * 100
          });
          if (code === 0) {
            state.showModal = false;
            await getAgentInfo();
            onChangeTab({
              index: 1
            });
          }
        }
      });
    }
    async function getAgentInfo() {
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUserSummary();
      if (code !== 0) {
        return;
      }
      state.summary = data;
    }
    async function onRequestMerchantTransfer(item) {
      const requestMerchantTransfer = sheep_index.sheep.$platform.useProvider() ? sheep_index.sheep.$platform.useProvider().requestMerchantTransfer : void 0;
      if (!requestMerchantTransfer) {
        sheep_index.sheep.$helper.toast("仅微信平台支持该功能");
        return;
      }
      const { code, data } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageWithdraw(item.id);
      if (code !== 0) {
        return;
      }
      if (data.status === 11) {
        sheep_index.sheep.$helper.toast("该提现单已确认收款");
        item.status = 11;
        return;
      }
      if (!data.transferChannelMchId || !data.transferChannelPackageInfo) {
        sheep_index.sheep.$helper.toast("提现信息异常，请稍后再试");
        return;
      }
      const payTransferId = data.payTransferId;
      await requestMerchantTransfer(
        data.transferChannelMchId,
        data.transferChannelPackageInfo,
        async (res) => {
          if (res.result !== "success") {
            sheep_index.sheep.$helper.toast(res.errMsg);
            return;
          }
          try {
            const syncTransferResult = await sheep_api_pay_transfer.PayTransferApi.syncTransfer(payTransferId);
            common_vendor.index.__f__("log", "at pages/commission/wallet.vue:348", "syncTransferResult 结果", syncTransferResult);
          } catch (e) {
            common_vendor.index.__f__("error", "at pages/commission/wallet.vue:350", "syncTransferResult 异常", e);
          }
          const { data: data2 } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageWithdraw(item.id);
          if (data2 && data2.status !== 11) {
            sheep_index.sheep.$helper.toast("确认收款成功，但数据存在延迟，请以实际【微信支付】到账为准");
            return;
          }
          sheep_index.sheep.$helper.toast("确认收款成功");
          item.status = 11;
        }
      );
    }
    common_vendor.onLoad(async (options) => {
      state.today = common_vendor.dayjs().format("YYYY-MM-DD");
      state.date = [state.today, state.today];
      if (options.type === "2") {
        state.currentTab = 1;
      }
      getLogList();
      getAgentInfo();
    });
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
        c: common_vendor.t(state.showMoney ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.withdrawPrice || 0) : "*****"),
        d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/commission/withdraw"), "6e"),
        e: common_vendor.o(($event) => state.showModal = true, "1c"),
        f: common_vendor.t(state.showMoney ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.frozenPrice || 0) : "*****"),
        g: common_vendor.t(state.showMoney ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.summary.brokeragePrice || 0) : "*****"),
        h: common_vendor.t(dateFilterText.value),
        i: common_vendor.o(onChangeTime, "da"),
        j: common_vendor.o(($event) => state.date = $event, "a6"),
        k: common_vendor.p({
          type: "daterange",
          end: state.today,
          modelValue: state.date
        }),
        l: common_vendor.o(onChangeTab, "6c"),
        m: common_vendor.p({
          list: tabMaps,
          scrollable: false,
          current: state.currentTab
        }),
        n: state.pagination.total === 0
      }, state.pagination.total === 0 ? {
        o: common_vendor.p({
          icon: "/static/data-empty.png",
          text: "暂无数据"
        })
      } : {}, {
        p: common_vendor.o(($event) => state.price = $event, "ed"),
        q: common_vendor.p({
          inputBorder: false,
          type: "number",
          placeholder: "请输入金额",
          modelValue: state.price
        }),
        r: common_vendor.o(onConfirm, "33"),
        s: common_vendor.o(($event) => state.showModal = false, "8c"),
        t: common_vendor.p({
          show: state.showModal,
          type: "bottom",
          round: "20",
          showClose: true
        }),
        v: state.pagination.total > 0
      }, state.pagination.total > 0 ? common_vendor.e({
        w: state.currentTab === 0
      }, state.currentTab === 0 ? {
        x: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(item.title),
            b: item.price >= 0
          }, item.price >= 0 ? {
            c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price))
          } : {
            d: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price))
          }, {
            e: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.createTime, "yyyy-mm-dd hh:MM:ss")),
            f: common_vendor.t(item.statusName),
            g: common_vendor.n("status-" + item.status),
            h: item.id
          });
        })
      } : {
        y: common_vendor.f(state.pagination.list, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(item.typeName),
            b: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.price)),
            c: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(item.createTime, "yyyy-mm-dd hh:MM:ss")),
            d: item.status === 10 && item.type === 5 && item.payTransferId > 0
          }, item.status === 10 && item.type === 5 && item.payTransferId > 0 ? {
            e: common_vendor.o(($event) => onRequestMerchantTransfer(item), item.id)
          } : {
            f: common_vendor.t(item.statusName),
            g: common_vendor.n("status-" + item.status)
          }, {
            h: item.id
          });
        })
      }) : {}, {
        z: state.pagination.total > 0
      }, state.pagination.total > 0 ? {
        A: common_vendor.p({
          status: state.loadStatus,
          ["content-text"]: {
            contentdown: "上拉加载更多"
          }
        })
      } : {}, {
        B: common_vendor.s(_ctx.__cssVars()),
        C: common_vendor.p({
          title: "佣金"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-52afd294"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/wallet.js.map
