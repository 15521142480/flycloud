"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_trade_config = require("../../sheep/api/trade/config.js");
const sheep_api_trade_brokerage = require("../../sheep/api/trade/brokerage.js");
const sheep_api_system_dict = require("../../sheep/api/system/dict.js");
const sheep_platform_pay = require("../../sheep/platform/pay.js");
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_s_uploader2 = common_vendor.resolveComponent("s-uploader");
  (_easycom_uni_easyinput2 + _easycom_s_uploader2)();
}
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_s_uploader = () => "../../sheep/components/s-uploader/s-uploader.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_s_uploader + accountTypeSelect + SLayout)();
}
const accountTypeSelect = () => "./components/account-type-select.js";
const SLayout = () => "../../sheep/components/s-layout/s-layout.js";
const _sfc_main = {
  __name: "withdraw",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "11aa8d88": common_vendor.unref(headerBg)
    }));
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/user/withdraw_bg.png");
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const state = common_vendor.reactive({
      accountInfo: {
        // 提现表单
        type: void 0,
        userAccount: void 0,
        userName: void 0,
        qrCodeUrl: void 0,
        bankName: void 0,
        bankAddress: void 0
      },
      accountSelect: false,
      brokerageInfo: {},
      // 分销信息
      frozenDays: 0,
      // 冻结天数
      minPrice: 0,
      // 最低提现金额
      withdrawTypes: [],
      // 提现方式
      bankList: [],
      // 银行字典数据
      bankListSelectedIndex: ""
      // 选中银行 bankList 的 index
    });
    const bankNameLabel = common_vendor.computed(() => {
      if (!state.accountInfo.bankName || !state.bankList || state.bankList.length === 0) {
        return "";
      }
      const item = state.bankList.find((it) => it.value === state.accountInfo.bankName);
      return item ? item.label : "";
    });
    const onAccountSelect = (e) => {
      state.accountSelect = e;
    };
    const onConfirm = async () => {
      if (!state.accountInfo.price || state.accountInfo.price > state.brokerageInfo.price || state.accountInfo.price <= 0) {
        sheep_index.sheep.$helper.toast("请输入正确的提现金额");
        return;
      }
      if (!state.accountInfo.type) {
        sheep_index.sheep.$helper.toast("请选择提现方式");
        return;
      }
      let openid;
      if (state.accountInfo.type === "5") {
        openid = await sheep_index.sheep.$platform.useProvider("wechat").getOpenid();
        if (!openid) {
          sheep_platform_pay.goBindWeixin();
          return;
        }
      }
      const data = {
        ...state.accountInfo,
        price: state.accountInfo.price * 100
      };
      if (state.accountInfo.type === "5") {
        data.userAccount = openid;
        data.transferChannelCode = sheep_platform_pay.getWeixinPayChannelCode();
      } else if (state.accountInfo.type === "6" || state.accountInfo.type === "2") {
        delete data.transferChannelCode;
      } else {
        delete data.userAccount;
        delete data.transferChannelCode;
      }
      let { code } = await sheep_api_trade_brokerage.BrokerageApi.createBrokerageWithdraw(data);
      if (code !== 0) {
        return;
      }
      common_vendor.index.showModal({
        title: "操作成功",
        content: "您的提现申请已成功提交",
        cancelText: "继续提现",
        confirmText: "查看记录",
        success: (res) => {
          if (res.confirm) {
            sheep_index.sheep.$router.go("/pages/commission/wallet", { type: 2 });
            return;
          }
          getBrokerageUser();
          state.accountInfo = {};
        }
      });
    };
    async function getWithdrawRules() {
      let { code, data } = await sheep_api_trade_config.TradeConfigApi.getTradeConfig();
      if (code !== 0) {
        return;
      }
      if (data) {
        state.minPrice = data.brokerageWithdrawMinPrice || 0;
        state.frozenDays = data.brokerageFrozenDays || 0;
        state.withdrawTypes = data.brokerageWithdrawTypes;
      }
    }
    async function getBrokerageUser() {
      const { data, code } = await sheep_api_trade_brokerage.BrokerageApi.getBrokerageUser();
      if (code === 0) {
        state.brokerageInfo = data;
      }
    }
    async function getDictDataListByType() {
      let { code, data } = await sheep_api_system_dict.DictApi.getDictDataListByType("brokerage_bank_name");
      if (code !== 0) {
        return;
      }
      if (data && data.length > 0) {
        state.bankList = data;
      }
    }
    function bankChange(e) {
      const value = e.detail.value;
      state.bankListSelectedIndex = value;
      const item = state.bankList[value];
      state.accountInfo.bankName = item ? item.value : void 0;
    }
    common_vendor.onBeforeMount(() => {
      getWithdrawRules();
      getBrokerageUser();
      getDictDataListByType();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.brokerageInfo.brokeragePrice)),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/commission/wallet", {
          type: 2
        }), "ce"),
        c: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 108) + "rpx"
        }),
        d: !state.accountInfo.type
      }, !state.accountInfo.type ? {} : {}, {
        e: state.accountInfo.type === "1"
      }, state.accountInfo.type === "1" ? {} : {}, {
        f: state.accountInfo.type === "2"
      }, state.accountInfo.type === "2" ? {} : {}, {
        g: state.accountInfo.type === "3"
      }, state.accountInfo.type === "3" ? {} : {}, {
        h: state.accountInfo.type === "4"
      }, state.accountInfo.type === "4" ? {} : {}, {
        i: state.accountInfo.type === "5"
      }, state.accountInfo.type === "5" ? {} : {}, {
        j: common_vendor.o(($event) => onAccountSelect(true), "b8"),
        k: common_vendor.o(($event) => state.accountInfo.price = $event, "ec"),
        l: common_vendor.p({
          inputBorder: false,
          type: "number",
          placeholder: "请输入提现金额",
          modelValue: state.accountInfo.price
        }),
        m: ["2", "6"].includes(state.accountInfo.type),
        n: common_vendor.o(($event) => state.accountInfo.userAccount = $event, "3b"),
        o: common_vendor.p({
          inputBorder: false,
          placeholder: "请输入提现账号",
          modelValue: state.accountInfo.userAccount
        }),
        p: ["2", "6"].includes(state.accountInfo.type),
        q: ["3", "4"].includes(state.accountInfo.type),
        r: common_vendor.o((payload) => state.accountInfo.qrCodeUrl = payload.tempFilePaths[0], "69"),
        s: common_vendor.o(($event) => state.accountInfo.qrCodeUrl = $event, "17"),
        t: common_vendor.p({
          fileMediatype: "image",
          limit: "1",
          mode: "grid",
          imageStyles: {
            width: "168rpx",
            height: "168rpx"
          },
          url: state.accountInfo.qrCodeUrl
        }),
        v: ["3", "4"].includes(state.accountInfo.type),
        w: ["2", "5", "6"].includes(state.accountInfo.type),
        x: common_vendor.o(($event) => state.accountInfo.userName = $event, "35"),
        y: common_vendor.p({
          inputBorder: false,
          placeholder: "请输入收款真名",
          modelValue: state.accountInfo.userName
        }),
        z: ["2", "5", "6"].includes(state.accountInfo.type),
        A: state.accountInfo.type === "2",
        B: common_vendor.p({
          inputBorder: false,
          value: bankNameLabel.value,
          placeholder: "请选择银行",
          suffixIcon: "right",
          disabled: true,
          styles: {
            disableColor: "#fff",
            borderColor: "#fff",
            color: "#333!important"
          }
        }),
        C: common_vendor.o(bankChange, "98"),
        D: state.bankListSelectedIndex,
        E: state.bankList,
        F: state.accountInfo.type === "2",
        G: state.accountInfo.type === "2",
        H: common_vendor.o(($event) => state.accountInfo.bankAddress = $event, "67"),
        I: common_vendor.p({
          inputBorder: false,
          placeholder: "请输入开户地址",
          modelValue: state.accountInfo.bankAddress
        }),
        J: state.accountInfo.type === "2",
        K: common_vendor.o(onConfirm, "66"),
        L: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.minPrice)),
        M: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.brokerageInfo.frozenPrice)),
        N: common_vendor.t(state.frozenDays),
        O: common_vendor.o(($event) => onAccountSelect(false), "a8"),
        P: common_vendor.o(($event) => state.accountInfo = $event, "88"),
        Q: common_vendor.p({
          show: state.accountSelect,
          round: "10",
          methods: state.withdrawTypes,
          modelValue: state.accountInfo
        }),
        R: common_vendor.s(_ctx.__cssVars()),
        S: common_vendor.p({
          title: "申请提现",
          navbar: "inner"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-cf14c8af"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/commission/withdraw.js.map
