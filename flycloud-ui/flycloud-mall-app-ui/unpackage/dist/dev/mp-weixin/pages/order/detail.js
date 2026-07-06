"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_api_trade_order = require("../../sheep/api/trade/order.js");
const sheep_api_trade_delivery = require("../../sheep/api/trade/delivery.js");
const sheep_api_pay_order = require("../../sheep/api/pay/order.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_su_fixed2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_su_fixed = () => "../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (_easycom_s_goods_item + PickUpVerify + _easycom_su_fixed + _easycom_s_layout)();
}
const PickUpVerify = () => "./pickUpVerify.js";
const _sfc_main = {
  __name: "detail",
  setup(__props) {
    common_vendor.useCssVars((_ctx) => ({
      "7295e610": common_vendor.unref(headerBg)
    }));
    const statusBarHeight = sheep_index.sheep.$platform.device.statusBarHeight * 2;
    const headerBg = sheep_index.sheep.$url.css("/static/img/shop/order/order_bg.png");
    const state = common_vendor.reactive({
      orderInfo: {}
    });
    const systemStore = common_vendor.ref({});
    const onCopy = () => {
      sheep_index.sheep.$helper.copyText(state.orderInfo.no);
    };
    function onPay(payOrderId) {
      sheep_index.sheep.$router.go("/pages/pay/index", {
        id: payOrderId
      });
    }
    function onGoodsDetail(id) {
      sheep_index.sheep.$router.go("/pages/goods/index", {
        id
      });
    }
    async function onCancel(orderId) {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要取消订单吗?",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_trade_order.OrderApi.cancelOrder(orderId);
          if (code === 0) {
            await getOrderDetail(orderId);
          }
        }
      });
    }
    async function onExpress(id) {
      sheep_index.sheep.$router.go("/pages/order/express/log", {
        id
      });
    }
    async function onConfirm(orderId, ignore = false) {
      let isOpenBusinessView = true;
      if (sheep_index.sheep.$platform.name === "WechatMiniProgram" && !common_vendor.isEmpty(state.orderInfo.wechat_extra_data) && isOpenBusinessView && !ignore) {
        mpConfirm(orderId);
        return;
      }
      common_vendor.index.showModal({
        title: "提示",
        content: "确认收货吗？",
        success: async function(res) {
          if (!res.confirm) {
            return;
          }
          const { code } = await sheep_api_trade_order.OrderApi.receiveOrder(orderId);
          if (code === 0) {
            await getOrderDetail(orderId);
          }
        }
      });
    }
    function mpConfirm(orderId) {
      if (!common_vendor.wx$1.openBusinessView) {
        sheep_index.sheep.$helper.toast(`请升级微信版本`);
        return;
      }
      common_vendor.wx$1.openBusinessView({
        businessType: "weappOrderConfirm",
        extraData: {
          merchant_trade_no: state.orderInfo.wechat_extra_data.merchant_trade_no,
          transaction_id: state.orderInfo.wechat_extra_data.transaction_id
        },
        success(response) {
          common_vendor.index.__f__("log", "at pages/order/detail.vue:375", "success:", response);
          if (response.errMsg === "openBusinessView:ok") {
            if (response.extraData.status === "success") {
              onConfirm(orderId, true);
            }
          }
        },
        fail(error) {
          common_vendor.index.__f__("log", "at pages/order/detail.vue:383", "error:", error);
        },
        complete(result) {
          common_vendor.index.__f__("log", "at pages/order/detail.vue:386", "result:", result);
        }
      });
    }
    function onComment(id) {
      sheep_index.sheep.$router.go("/pages/goods/comment/add", {
        id
      });
    }
    const pickUpVerifyRef = common_vendor.ref();
    async function getOrderDetail(id) {
      let res;
      if (state.comeinType === "wechat") {
        res = await sheep_api_trade_order.OrderApi.getOrderDetail(id, {
          merchant_trade_no: state.merchantTradeNo
        });
      } else {
        res = await sheep_api_trade_order.OrderApi.getOrderDetail(id);
      }
      if (res.code === 0) {
        state.orderInfo = res.data;
        sheep_hooks_useGoods.handleOrderButtons(state.orderInfo);
        if (res.data.pickUpStoreId) {
          const { data } = await sheep_api_trade_delivery.DeliveryApi.getDeliveryPickUpStore(res.data.pickUpStoreId);
          systemStore.value = data || {};
        }
        if (state.orderInfo.deliveryType === 2 && state.orderInfo.payStatus) {
          pickUpVerifyRef.value && pickUpVerifyRef.value.markCode(res.data.pickUpVerifyCode);
        }
      } else {
        sheep_index.sheep.$router.back();
      }
    }
    common_vendor.onShow(async () => {
      if (state.orderInfo.id) {
        await getOrderDetail(state.orderInfo.id);
      }
    });
    common_vendor.onLoad(async (options) => {
      var _a;
      let id = 0;
      if (options.id) {
        id = options.id;
      }
      if (!id && options.payOrderNo) {
        const payOrder = await sheep_api_pay_order.PayOrderApi.getOrder(void 0, void 0, options.payOrderNo);
        if (payOrder.code === 0) {
          id = (_a = payOrder.data) == null ? void 0 : _a.merchantOrderId;
        }
      }
      state.orderInfo.id = id;
      await getOrderDetail(state.orderInfo.id);
    });
    return (_ctx, _cache) => {
      var _a, _b, _c, _d, _e, _f, _g, _h, _i, _j, _k, _l, _m, _n;
      return common_vendor.e({
        a: state.orderInfo.status === 0
      }, state.orderInfo.status === 0 ? {
        b: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/no_pay.png")
      } : {}, {
        c: state.orderInfo.status === 10
      }, state.orderInfo.status === 10 ? {
        d: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/order_loading.png")
      } : state.orderInfo.status === 30 ? {
        f: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/order_success.png")
      } : state.orderInfo.status === 40 ? {
        h: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/order_close.png")
      } : state.orderInfo.status === 20 ? {
        j: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/order/order_express.png")
      } : {}, {
        e: state.orderInfo.status === 30,
        g: state.orderInfo.status === 40,
        i: state.orderInfo.status === 20,
        k: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatOrderStatus)(state.orderInfo)),
        l: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.formatOrderStatusDescription)(state.orderInfo)),
        m: common_vendor.s({
          marginTop: "-" + Number(statusBarHeight + 88) + "rpx",
          paddingTop: Number(statusBarHeight + 88) + "rpx"
        }),
        n: state.orderInfo.receiverAreaId > 0
      }, state.orderInfo.receiverAreaId > 0 ? {
        o: common_vendor.t(state.orderInfo.receiverName),
        p: common_vendor.t(state.orderInfo.receiverMobile),
        q: common_vendor.t(state.orderInfo.receiverAreaName),
        r: common_vendor.t(state.orderInfo.receiverDetailAddress)
      } : {}, {
        s: common_vendor.f(state.orderInfo.items, (item, k0, i0) => {
          return common_vendor.e({
            a: [10, 20, 30].includes(state.orderInfo.status) && item.afterSaleStatus === 0
          }, [10, 20, 30].includes(state.orderInfo.status) && item.afterSaleStatus === 0 ? {
            b: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/aftersale/apply", {
              orderId: state.orderInfo.id,
              itemId: item.id
            }), item.goods_id)
          } : {}, {
            c: item.afterSaleStatus === 10
          }, item.afterSaleStatus === 10 ? {
            d: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/aftersale/detail", {
              id: item.afterSaleId
            }), item.goods_id)
          } : {}, {
            e: item.afterSaleStatus === 20
          }, item.afterSaleStatus === 20 ? {
            f: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/order/aftersale/detail", {
              id: item.afterSaleId
            }), item.goods_id)
          } : {}, {
            g: item.status_text
          }, item.status_text ? {
            h: common_vendor.t(item.status_text)
          } : {}, {
            i: common_vendor.o(($event) => onGoodsDetail(item.spuId), item.goods_id),
            j: "6b23c96c-1-" + i0 + ",6b23c96c-0",
            k: common_vendor.p({
              img: item.picUrl,
              title: item.spuName,
              skuText: item.properties.map((property) => property.valueName).join(" "),
              price: item.price,
              num: item.count
            }),
            l: item.goods_id
          });
        }),
        t: common_vendor.s({
          marginTop: state.orderInfo.receiverAreaId > 0 ? "0" : "-40rpx"
        }),
        v: common_vendor.sr(pickUpVerifyRef, "6b23c96c-2,6b23c96c-0", {
          "k": "pickUpVerifyRef"
        }),
        w: common_vendor.p({
          ["order-info"]: state.orderInfo,
          systemStore: systemStore.value
        }),
        x: common_vendor.t(state.orderInfo.no),
        y: common_vendor.o(onCopy, "4b"),
        z: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.orderInfo.createTime, "yyyy-mm-dd hh:MM:ss")),
        A: state.orderInfo.payTime
      }, state.orderInfo.payTime ? {
        B: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(state.orderInfo.payTime, "yyyy-mm-dd hh:MM:ss"))
      } : {}, {
        C: common_vendor.t(state.orderInfo.payChannelName || "-"),
        D: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.totalPrice)),
        E: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.deliveryPrice)),
        F: state.orderInfo.couponPrice > 0
      }, state.orderInfo.couponPrice > 0 ? {
        G: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.couponPrice))
      } : {}, {
        H: state.orderInfo.pointPrice > 0
      }, state.orderInfo.pointPrice > 0 ? {
        I: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.pointPrice))
      } : {}, {
        J: state.orderInfo.discountPrice > 0
      }, state.orderInfo.discountPrice > 0 ? {
        K: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.discountPrice))
      } : {}, {
        L: state.orderInfo.vipPrice > 0
      }, state.orderInfo.vipPrice > 0 ? {
        M: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.vipPrice))
      } : {}, {
        N: common_vendor.t(state.orderInfo.payStatus ? "已付款" : "需付款"),
        O: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.payPrice)),
        P: state.orderInfo.refundPrice > 0
      }, state.orderInfo.refundPrice > 0 ? {
        Q: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.refundPrice))
      } : {}, {
        R: (_a = state.orderInfo.buttons) == null ? void 0 : _a.length
      }, ((_b = state.orderInfo.buttons) == null ? void 0 : _b.length) ? common_vendor.e({
        S: (_c = state.orderInfo.buttons) == null ? void 0 : _c.includes("cancel")
      }, ((_d = state.orderInfo.buttons) == null ? void 0 : _d.includes("cancel")) ? {
        T: common_vendor.o(($event) => onCancel(state.orderInfo.id), "46")
      } : {}, {
        U: (_e = state.orderInfo.buttons) == null ? void 0 : _e.includes("pay")
      }, ((_f = state.orderInfo.buttons) == null ? void 0 : _f.includes("pay")) ? {
        V: common_vendor.o(($event) => onPay(state.orderInfo.payOrderId), "34")
      } : {}, {
        W: (_g = state.orderInfo.buttons) == null ? void 0 : _g.includes("combination")
      }, ((_h = state.orderInfo.buttons) == null ? void 0 : _h.includes("combination")) ? {
        X: common_vendor.o(($event) => common_vendor.unref(sheep_index.sheep).$router.go("/pages/activity/groupon/detail", {
          id: state.orderInfo.combinationRecordId
        }), "9f")
      } : {}, {
        Y: (_i = state.orderInfo.buttons) == null ? void 0 : _i.includes("express")
      }, ((_j = state.orderInfo.buttons) == null ? void 0 : _j.includes("express")) ? {
        Z: common_vendor.o(($event) => onExpress(state.orderInfo.id), "46")
      } : {}, {
        aa: (_k = state.orderInfo.buttons) == null ? void 0 : _k.includes("confirm")
      }, ((_l = state.orderInfo.buttons) == null ? void 0 : _l.includes("confirm")) ? {
        ab: common_vendor.o(($event) => onConfirm(state.orderInfo.id), "e4")
      } : {}, {
        ac: (_m = state.orderInfo.buttons) == null ? void 0 : _m.includes("comment")
      }, ((_n = state.orderInfo.buttons) == null ? void 0 : _n.includes("comment")) ? {
        ad: common_vendor.o(($event) => onComment(state.orderInfo.id), "b9")
      } : {}, {
        ae: common_vendor.p({
          bottom: true,
          placeholder: true,
          bg: "bg-white"
        })
      }) : {}, {
        af: common_vendor.s(_ctx.__cssVars()),
        ag: common_vendor.p({
          title: "订单详情",
          navbar: "inner"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-6b23c96c"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/detail.js.map
