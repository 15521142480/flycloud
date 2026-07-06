"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_pay_order = require("../../sheep/api/pay/order.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_trade_order = require("../../sheep/api/trade/order.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
if (!Array) {
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  _easycom_s_layout2();
}
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  _easycom_s_layout();
}
const SUBSCRIBE_BTN_STATUS_STORAGE_KEY = "subscribe_btn_status";
const _sfc_main = {
  __name: "result",
  setup(__props) {
    const state = common_vendor.reactive({
      id: 0,
      // 支付单号
      orderType: "goods",
      // 订单类型
      result: "unpaid",
      // 支付状态
      orderInfo: {},
      // 支付订单信息
      tradeOrder: {},
      // 商品订单信息，只有在 orderType 为 goods 才会请求。目的：【我的拼团】按钮的展示
      counter: 0
      // 获取结果次数
    });
    const payResult = common_vendor.computed(() => {
      if (state.result === "unpaid") {
        return "waiting";
      }
      if (state.result === "paid") {
        return "success";
      }
      if (state.result === "failed") {
        return "failed";
      }
      if (state.result === "closed") {
        return "closed";
      }
    });
    function showRepayModal() {
      if (state.result !== "failed")
        return;
      common_vendor.index.showModal({
        title: "确认支付",
        content: "未检测到您的支付结果,请确认您是否已经支付完成？",
        cancelText: "取消",
        confirmText: "我已支付",
        success: function(res) {
          if (res.confirm) {
            state.counter = 0;
            setTimeout(() => {
              getOrderInfo(state.id);
            }, 100);
          }
        }
      });
    }
    async function getOrderInfo(id) {
      state.counter++;
      const { data, code } = await sheep_api_pay_order.PayOrderApi.getOrder(id, true);
      if (code === 0) {
        state.orderInfo = data;
        if (!state.orderInfo || state.orderInfo.status === 30) {
          state.result = "closed";
          return;
        }
        if (state.orderInfo.status !== 0) {
          state.result = "paid";
          common_vendor.index.showModal({
            title: "支付结果",
            showCancel: false,
            // 不要取消按钮
            content: "支付成功",
            success: () => {
              autoSubscribeMessage();
            }
          });
          if (state.orderType === "goods") {
            const { data: data2, code: code2 } = await sheep_api_trade_order.OrderApi.getOrderDetail(
              state.orderInfo.merchantOrderId,
              true
            );
            if (code2 === 0) {
              state.tradeOrder = data2;
            }
          }
          return;
        }
      }
      if (state.counter < 5 && state.result === "unpaid") {
        setTimeout(() => {
          getOrderInfo(id);
        }, 2e3);
      }
      if (state.counter >= 5) {
        state.result = "failed";
        showRepayModal();
      }
    }
    function onOrder() {
      if (state.orderType === "recharge") {
        sheep_index.sheep.$router.redirect("/pages/pay/recharge-log");
      } else {
        sheep_index.sheep.$router.redirect("/pages/order/list");
      }
    }
    const showSubscribeBtn = common_vendor.ref(false);
    function subscribeMessage() {
      if (state.orderType !== "goods") {
        return;
      }
      const event = [sheep_helper_const.WxaSubscribeTemplate.TRADE_ORDER_DELIVERY];
      if (state.tradeOrder.type === 3) {
        event.push(sheep_helper_const.WxaSubscribeTemplate.PROMOTION_COMBINATION_SUCCESS);
      }
      sheep_index.sheep.$platform.useProvider("wechat").subscribeMessage(event, () => {
        common_vendor.index.removeStorageSync(SUBSCRIBE_BTN_STATUS_STORAGE_KEY);
        common_vendor.index.setStorageSync(SUBSCRIBE_BTN_STATUS_STORAGE_KEY, "已订阅");
        showSubscribeBtn.value = false;
      });
    }
    async function autoSubscribeMessage() {
      const subscribeBtnStatus = common_vendor.index.getStorageSync(SUBSCRIBE_BTN_STATUS_STORAGE_KEY);
      if (!subscribeBtnStatus) {
        showSubscribeBtn.value = true;
        return;
      }
      subscribeMessage();
    }
    common_vendor.onLoad(async (options) => {
      if (options.id) {
        state.id = options.id;
      }
      if (options.orderType) {
        state.orderType = options.orderType;
      }
      if (options.payState === "fail") {
        state.result = "failed";
      } else {
        await getOrderInfo(state.id);
      }
    });
    common_vendor.onShow(() => {
      if (common_vendor.isEmpty(state.orderInfo)) {
        return;
      }
      getOrderInfo(state.id);
    });
    common_vendor.onHide(() => {
      state.result = "unpaid";
      state.counter = 0;
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: payResult.value === "waiting"
      }, payResult.value === "waiting" ? {} : {}, {
        b: payResult.value === "success"
      }, payResult.value === "success" ? {
        c: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/order_pay_success.gif")
      } : {}, {
        d: ["failed", "closed"].includes(payResult.value)
      }, ["failed", "closed"].includes(payResult.value) ? {
        e: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/order_paty_fail.gif")
      } : {}, {
        f: payResult.value === "success"
      }, payResult.value === "success" ? {} : {}, {
        g: payResult.value === "failed"
      }, payResult.value === "failed" ? {} : {}, {
        h: payResult.value === "closed"
      }, payResult.value === "closed" ? {} : {}, {
        i: payResult.value === "waiting"
      }, payResult.value === "waiting" ? {} : {}, {
        j: payResult.value === "success"
      }, payResult.value === "success" ? {
        k: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price))
      } : {}, {
        l: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/index/index"), "98"),
        m: payResult.value === "failed"
      }, payResult.value === "failed" ? {
        n: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.redirect("/pages/pay/index", {
          id: state.id,
          orderType: state.orderType
        }), "61")
      } : {}, {
        o: payResult.value === "success"
      }, payResult.value === "success" ? {
        p: common_vendor.o(onOrder, "21")
      } : {}, {
        q: payResult.value === "success" && state.tradeOrder.type === 3
      }, payResult.value === "success" && state.tradeOrder.type === 3 ? {
        r: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.redirect("/pages/activity/groupon/order"), "4a")
      } : {}, {
        s: showSubscribeBtn.value && state.orderType === "goods"
      }, showSubscribeBtn.value && state.orderType === "goods" ? {
        t: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/cargo.png"),
        v: common_vendor.o(subscribeMessage, "e7")
      } : {}, {
        w: common_vendor.p({
          bgStyle: {
            color: "#FFF"
          },
          title: "支付结果"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-42b1ec48"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/pay/result.js.map
