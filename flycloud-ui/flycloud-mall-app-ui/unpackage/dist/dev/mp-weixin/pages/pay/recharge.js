"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_pay_wallet = require("../../sheep/api/pay/wallet.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
if (!Array) {
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_uni_easyinput2 + _easycom_s_layout2)();
}
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_uni_easyinput + _easycom_s_layout)();
}
const _sfc_main = {
  __name: "recharge",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "dd851cb6": common_vendor.unref(headerBg)
    }));
    const userWallet = common_vendor.computed(() => sheep_index.sheep.$store("user").userWallet);
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/user/withdraw_bg.png");
    const state = common_vendor.reactive({
      recharge_money: "",
      // 输入的充值金额
      packageList: []
    });
    function onCard(e) {
      state.recharge_money = sheep_hooks_useGoods.fen2yuan(e);
    }
    async function getRechargeTabs() {
      const { code, data } = await sheep_api_pay_wallet.PayWalletApi.getWalletRechargePackageList();
      if (code !== 0) {
        return;
      }
      state.packageList = data;
    }
    async function onConfirm() {
      var _a;
      const { code, data } = await sheep_api_pay_wallet.PayWalletApi.createWalletRecharge({
        packageId: (_a = state.packageList.find((item) => sheep_hooks_useGoods.fen2yuan(item.payPrice) === state.recharge_money)) == null ? void 0 : _a.id,
        payPrice: state.recharge_money * 100
      });
      if (code !== 0) {
        return;
      }
      sheep_index.sheep.$platform.useProvider("wechat").subscribeMessage(sheep_helper_const.WxaSubscribeTemplate.PAY_WALLET_RECHARGER_SUCCESS);
      sheep_index.sheep.$router.go("/pages/pay/index", {
        id: data.payOrderId,
        orderType: "recharge"
      });
    }
    common_vendor.onLoad(() => {
      getRechargeTabs();
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(userWallet.value.balance)),
        b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/pay/recharge-log"), "1f"),
        c: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 108) + "rpx"
        }),
        d: common_vendor.o(($event) => state.recharge_money = $event, "ad"),
        e: common_vendor.p({
          type: "digit",
          placeholder: "请输入充值金额",
          inputBorder: false,
          modelValue: state.recharge_money
        }),
        f: common_vendor.f(state.packageList, (item, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.payPrice)),
            b: item.bonusPrice
          }, item.bonusPrice ? {
            c: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.bonusPrice))
          } : {}, {
            d: item.money,
            e: common_vendor.n({
              "btn-active": state.recharge_money === common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(item.payPrice)
            }),
            f: common_vendor.o(($event) => onCard(item.payPrice), item.money)
          });
        }),
        g: common_vendor.o(onConfirm, "67"),
        h: common_vendor.s(_ctx.__cssVars()),
        i: common_vendor.p({
          title: "充值",
          navbar: "inner"
        })
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-b0187d83"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/pay/recharge.js.map
