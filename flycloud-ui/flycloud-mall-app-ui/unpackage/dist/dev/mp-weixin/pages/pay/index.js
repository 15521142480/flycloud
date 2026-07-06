"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_pay_order = require("../../sheep/api/pay/order.js");
const sheep_api_pay_channel = require("../../sheep/api/pay/channel.js");
const sheep_platform_pay = require("../../sheep/platform/pay.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  _easycom_s_layout();
}
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const userWallet = common_vendor.computed(() => sheep_index.sheep.$store("user").userWallet);
    const state = common_vendor.reactive({
      orderType: "goods",
      // 订单类型; goods - 商品订单, recharge - 充值订单
      orderInfo: {},
      // 支付单信息
      payStatus: 0,
      // 0=检测支付环境, -2=未查询到支付单信息， -1=支付已过期， 1=待支付，2=订单已支付
      payMethods: [],
      // 可选的支付方式
      payment: ""
      // 选中的支付方式
    });
    const onPay = () => {
      if (state.payment === "") {
        sheep_index.sheep.$helper.toast("请选择支付方式");
        return;
      }
      if (state.payment === "wallet") {
        common_vendor.index.showModal({
          title: "提示",
          content: "确定要支付吗?",
          success: function(res) {
            if (res.confirm) {
              sheep_index.sheep.$platform.pay(state.payment, state.orderType, state.orderInfo.id);
            }
          }
        });
      } else {
        sheep_index.sheep.$platform.pay(state.payment, state.orderType, state.orderInfo.id);
      }
    };
    const payDescText = common_vendor.computed(() => {
      if (state.payStatus === 2) {
        return "该订单已支付";
      }
      if (state.payStatus === 1) {
        const time = sheep_hooks_useGoods.useDurationTime(state.orderInfo.expireTime);
        if (time.ms <= 0) {
          state.payStatus = -1;
          return "";
        }
        return `剩余支付时间 ${time.h}:${time.m}:${time.s} `;
      }
      if (state.payStatus === -2) {
        return "未查询到支付单信息";
      }
      return "";
    });
    function checkPayStatus() {
      if (state.orderInfo.status === 10 || state.orderInfo.status === 20) {
        state.payStatus = 2;
        common_vendor.index.showModal({
          title: "提示",
          content: "订单已支付",
          showCancel: false,
          success: function() {
            sheep_platform_pay.goPayResult(state.orderInfo.id, state.orderType);
          }
        });
        return;
      }
      if (state.orderInfo.status === 30) {
        state.payStatus = -1;
        return;
      }
      state.payStatus = 1;
    }
    function onTapPay(e) {
      state.payment = e.detail.value;
    }
    async function setOrder(id) {
      const { data, code } = await sheep_api_pay_order.PayOrderApi.getOrder(id, true);
      if (code !== 0 || !data) {
        state.payStatus = -2;
        return;
      }
      state.orderInfo = data;
      checkPayStatus();
      await setPayMethods();
    }
    async function setPayMethods() {
      const { data, code } = await sheep_api_pay_channel.PayChannelApi.getEnableChannelCodeList(state.orderInfo.appId);
      if (code !== 0) {
        return;
      }
      state.payMethods = sheep_platform_pay.getPayMethods(data);
      state.payMethods.find((item) => {
        if (item.value && !item.disabled) {
          state.payment = item.value;
          return true;
        }
      });
    }
    common_vendor.onLoad((options) => {
      if (sheep_index.sheep.$platform.name === "WechatOfficialAccount" && sheep_index.sheep.$platform.os === "ios" && !sheep_index.sheep.$platform.landingPage.includes("pages/pay/index")) {
        location.reload();
        return;
      }
      let id = options.id;
      if (options.orderType) {
        state.orderType = options.orderType;
      }
      setOrder(id);
      sheep_index.sheep.$store("user").getWallet();
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price)),
        b: common_vendor.t(payDescText.value),
        c: common_vendor.f(state.payMethods, (item, k0, i0) => {
          return common_vendor.e({
            a: item.disabled
          }, item.disabled ? {
            b: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/pay/cod_disabled.png")
          } : {
            c: common_vendor.unref(sheep_index.sheep).$url.static(item.icon)
          }, {
            d: common_vendor.t(item.title),
            e: item.value === "wallet"
          }, item.value === "wallet" ? {
            f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(userWallet.value.balance))
          } : {}, {
            g: item.value,
            h: item.disabled,
            i: state.payment === item.value,
            j: item.disabled ? 1 : "",
            k: item.title
          });
        }),
        d: common_vendor.o(onTapPay, "58"),
        e: state.payStatus === 0
      }, state.payStatus === 0 ? {} : state.payStatus === -1 ? {} : {
        g: common_vendor.o(onPay, "b4"),
        h: state.payStatus !== 1,
        i: state.payStatus !== 1 ? 1 : ""
      }, {
        f: state.payStatus === -1,
        j: common_vendor.p({
          title: "收银台"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-d7fd7b38"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/pay/index.js.map
