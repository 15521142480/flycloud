"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const sheep_api_trade_order = require("../../sheep/api/trade/order.js");
const sheep_api_trade_config = require("../../sheep/api/trade/config.js");
const sheep_hooks_useGoods = require("../../sheep/hooks/useGoods.js");
const sheep_helper_const = require("../../sheep/helper/const.js");
if (!Array) {
  const _easycom_s_goods_item2 = common_vendor.resolveComponent("s-goods-item");
  const _easycom_uni_easyinput2 = common_vendor.resolveComponent("uni-easyinput");
  const _easycom_s_coupon_select2 = common_vendor.resolveComponent("s-coupon-select");
  const _easycom_s_discount_list2 = common_vendor.resolveComponent("s-discount-list");
  const _easycom_su_fixed2 = common_vendor.resolveComponent("su-fixed");
  const _easycom_s_layout2 = common_vendor.resolveComponent("s-layout");
  (_easycom_s_goods_item2 + _easycom_uni_easyinput2 + _easycom_s_coupon_select2 + _easycom_s_discount_list2 + _easycom_su_fixed2 + _easycom_s_layout2)();
}
const _easycom_s_goods_item = () => "../../sheep/components/s-goods-item/s-goods-item.js";
const _easycom_uni_easyinput = () => "../../uni_modules/uni-easyinput/components/uni-easyinput/uni-easyinput.js";
const _easycom_s_coupon_select = () => "../../sheep/components/s-coupon-select/s-coupon-select.js";
const _easycom_s_discount_list = () => "../../sheep/components/s-discount-list/s-discount-list.js";
const _easycom_su_fixed = () => "../../sheep/ui/su-fixed/su-fixed.js";
const _easycom_s_layout = () => "../../sheep/components/s-layout/s-layout.js";
if (!Math) {
  (AddressSelection + _easycom_s_goods_item + _easycom_uni_easyinput + _easycom_s_coupon_select + _easycom_s_discount_list + _easycom_su_fixed + _easycom_s_layout)();
}
const AddressSelection = () => "./addressSelection.js";
const _sfc_main = {
  __name: "confirm",
  setup(__props) {
    const state = common_vendor.reactive({
      orderPayload: {},
      orderInfo: {
        items: [],
        // 商品项列表
        price: {}
        // 价格信息
      },
      showCoupon: false,
      // 是否展示优惠劵
      couponInfo: [],
      // 优惠劵列表
      showDiscount: false,
      // 是否展示营销活动
      // ========== 积分 ==========
      pointStatus: false
      //是否使用积分
    });
    const addressState = common_vendor.ref({
      addressInfo: {},
      // 选择的收货地址
      deliveryType: void 0,
      // 收货方式：1-快递配送，2-门店自提
      isPickUp: true,
      // 门店自提是否开启
      pickUpInfo: {},
      // 选择的自提门店信息
      receiverName: "",
      // 收件人名称
      receiverMobile: ""
      // 收件人手机
    });
    const changeIntegral = async () => {
      state.pointStatus = !state.pointStatus;
      await getOrderInfo();
    };
    async function onSelectCoupon(couponId) {
      state.orderPayload.couponId = couponId;
      await getOrderInfo();
      state.showCoupon = false;
    }
    function onConfirm() {
      if (addressState.value.deliveryType === 1 && !addressState.value.addressInfo.id) {
        sheep_index.sheep.$helper.toast("请选择收货地址");
        return;
      }
      if (addressState.value.deliveryType === 2) {
        if (!addressState.value.pickUpInfo.id) {
          sheep_index.sheep.$helper.toast("请选择自提门店地址");
          return;
        }
        if (addressState.value.receiverName === "" || addressState.value.receiverMobile === "") {
          sheep_index.sheep.$helper.toast("请填写联系人或联系人电话");
          return;
        }
        if (!/^[\u4e00-\u9fa5\w]{2,16}$/.test(addressState.value.receiverName)) {
          sheep_index.sheep.$helper.toast("请填写您的真实姓名");
          return;
        }
        if (!/^1(3|4|5|7|8|9|6)\d{9}$/.test(addressState.value.receiverMobile)) {
          sheep_index.sheep.$helper.toast("请填写正确的手机号");
          return;
        }
      }
      submitOrder();
    }
    async function submitOrder() {
      const { code, data } = await sheep_api_trade_order.OrderApi.createOrder({
        items: state.orderPayload.items,
        couponId: state.orderPayload.couponId,
        remark: state.orderPayload.remark,
        deliveryType: addressState.value.deliveryType,
        addressId: addressState.value.addressInfo.id,
        // 收件地址编号
        pickUpStoreId: addressState.value.pickUpInfo.id,
        //自提门店编号
        receiverName: addressState.value.receiverName,
        // 选择门店自提时，该字段为联系人名
        receiverMobile: addressState.value.receiverMobile,
        // 选择门店自提时，该字段为联系人手机
        pointStatus: state.pointStatus,
        combinationActivityId: state.orderPayload.combinationActivityId,
        combinationHeadId: state.orderPayload.combinationHeadId,
        seckillActivityId: state.orderPayload.seckillActivityId,
        pointActivityId: state.orderPayload.pointActivityId
      });
      if (code !== 0) {
        return;
      }
      if (state.orderPayload.items[0].cartId > 0) {
        sheep_index.sheep.$store("cart").getList();
      }
      if (data.payOrderId && data.payOrderId > 0) {
        sheep_index.sheep.$router.redirect("/pages/pay/index", {
          id: data.payOrderId
        });
      } else {
        sheep_index.sheep.$router.redirect("/pages/order/detail", {
          id: data.id
        });
      }
    }
    async function getOrderInfo() {
      const { data, code } = await sheep_api_trade_order.OrderApi.settlementOrder({
        items: state.orderPayload.items,
        couponId: state.orderPayload.couponId,
        deliveryType: addressState.value.deliveryType,
        addressId: addressState.value.addressInfo.id,
        // 收件地址编号
        pickUpStoreId: addressState.value.pickUpInfo.id,
        //自提门店编号
        receiverName: addressState.value.receiverName,
        // 选择门店自提时，该字段为联系人名
        receiverMobile: addressState.value.receiverMobile,
        // 选择门店自提时，该字段为联系人手机
        pointStatus: state.pointStatus,
        combinationActivityId: state.orderPayload.combinationActivityId,
        combinationHeadId: state.orderPayload.combinationHeadId,
        seckillActivityId: state.orderPayload.seckillActivityId,
        pointActivityId: state.orderPayload.pointActivityId
      });
      if (code !== 0) {
        return code;
      }
      state.orderInfo = data;
      state.couponInfo = data.coupons || [];
      if (state.orderInfo.address) {
        addressState.value.addressInfo = state.orderInfo.address;
      }
      return code;
    }
    common_vendor.onLoad(async (options) => {
      if (!options.data) {
        sheep_index.sheep.$helper.toast("参数不正确，请检查！");
        return;
      }
      state.orderPayload = JSON.parse(options.data);
      const { data, code } = await sheep_api_trade_config.TradeConfigApi.getTradeConfig();
      if (code === 0) {
        addressState.value.isPickUp = data.deliveryPickUpEnabled;
      }
      addressState.value.deliveryType = sheep_helper_const.DeliveryTypeEnum.EXPRESS.type;
      let orderCode = await getOrderInfo();
      if (orderCode === 0) {
        return;
      }
      if (addressState.value.isPickUp) {
        addressState.value.deliveryType = sheep_helper_const.DeliveryTypeEnum.PICK_UP.type;
        let orderCode2 = await getOrderInfo();
        if (orderCode2 === 0) {
          return;
        }
      }
      addressState.value.deliveryType = void 0;
      await getOrderInfo();
    });
    common_vendor.watch(addressState, async (newAddress, oldAddress) => {
      if (newAddress.addressInfo.id !== oldAddress.addressInfo.id || newAddress.deliveryType !== oldAddress.deliveryType) {
        await getOrderInfo();
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(($event) => addressState.value = $event, "db"),
        b: common_vendor.p({
          modelValue: addressState.value
        }),
        c: common_vendor.f(state.orderInfo.items, (item, k0, i0) => {
          return {
            a: item.skuId,
            b: "324e7894-2-" + i0 + ",324e7894-0",
            c: common_vendor.p({
              img: item.picUrl,
              title: item.spuName,
              skuText: item.properties.map((property) => property.valueName).join(" "),
              price: item.price,
              num: item.count,
              marginBottom: "10"
            })
          };
        }),
        d: common_vendor.o(($event) => state.orderPayload.remark = $event, "e6"),
        e: common_vendor.p({
          maxlength: "20",
          placeholder: "建议留言前先与商家沟通",
          inputBorder: false,
          clearable: false,
          modelValue: state.orderPayload.remark
        }),
        f: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price.totalPrice)),
        g: state.orderPayload.pointActivityId
      }, state.orderPayload.pointActivityId ? {
        h: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        i: common_vendor.t(state.orderInfo.usePoint)
      } : {}, {
        j: state.orderInfo.type === 0 || state.orderPayload.pointActivityId
      }, state.orderInfo.type === 0 || state.orderPayload.pointActivityId ? common_vendor.e({
        k: common_vendor.t(state.pointStatus || state.orderPayload.pointActivityId ? "剩余积分" : "当前积分"),
        l: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/goods/score1.svg"),
        m: common_vendor.t(state.pointStatus || state.orderPayload.pointActivityId ? state.orderInfo.totalPoint - state.orderInfo.usePoint : state.orderInfo.totalPoint || 0),
        n: !state.orderPayload.pointActivityId
      }, !state.orderPayload.pointActivityId ? {
        o: state.pointStatus,
        p: !state.orderInfo.totalPoint || state.orderInfo.totalPoint <= 0,
        q: common_vendor.o(changeIntegral, "8c")
      } : {}) : {}, {
        r: addressState.value.deliveryType === 1
      }, addressState.value.deliveryType === 1 ? common_vendor.e({
        s: state.orderInfo.price.deliveryPrice > 0
      }, state.orderInfo.price.deliveryPrice > 0 ? {
        t: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price.deliveryPrice))
      } : {}) : {}, {
        v: addressState.value.deliveryType === 2
      }, addressState.value.deliveryType === 2 ? {
        w: common_vendor.o(($event) => addressState.value.receiverName = $event, "81"),
        x: common_vendor.p({
          maxlength: "20",
          placeholder: "请填写您的联系姓名",
          inputBorder: false,
          clearable: false,
          modelValue: addressState.value.receiverName
        })
      } : {}, {
        y: addressState.value.deliveryType === 2
      }, addressState.value.deliveryType === 2 ? {
        z: common_vendor.o(($event) => addressState.value.receiverMobile = $event, "4a"),
        A: common_vendor.p({
          maxlength: "20",
          placeholder: "请填写您的联系电话",
          inputBorder: false,
          clearable: false,
          modelValue: addressState.value.receiverMobile
        })
      } : {}, {
        B: state.orderInfo.type === 0
      }, state.orderInfo.type === 0 ? common_vendor.e({
        C: state.orderPayload.couponId > 0
      }, state.orderPayload.couponId > 0 ? {
        D: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price.couponPrice))
      } : {
        E: common_vendor.t(state.couponInfo.filter((coupon) => coupon.match).length > 0 ? state.couponInfo.filter((coupon) => coupon.match).length + " 张可用" : "暂无可用优惠券"),
        F: common_vendor.n(state.couponInfo.filter((coupon) => coupon.match).length > 0 ? "text-red" : "text-disabled")
      }, {
        G: common_vendor.o(($event) => state.showCoupon = true, "a0")
      }) : {}, {
        H: state.orderInfo.price.discountPrice > 0
      }, state.orderInfo.price.discountPrice > 0 ? {
        I: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price.discountPrice)),
        J: common_vendor.o(($event) => state.showDiscount = true, "a9")
      } : {}, {
        K: state.orderInfo.price.vipPrice > 0
      }, state.orderInfo.price.vipPrice > 0 ? {
        L: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price.vipPrice))
      } : {}, {
        M: common_vendor.t(state.orderInfo.items.reduce((acc, item) => acc + item.count, 0)),
        N: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price.payPrice)),
        O: common_vendor.o(onSelectCoupon, "8f"),
        P: common_vendor.o(($event) => state.showCoupon = false, "14"),
        Q: common_vendor.o(($event) => state.couponInfo = $event, "23"),
        R: common_vendor.p({
          show: state.showCoupon,
          modelValue: state.couponInfo
        }),
        S: common_vendor.o(($event) => state.showDiscount = false, "86"),
        T: common_vendor.o(($event) => state.orderInfo = $event, "b9"),
        U: common_vendor.p({
          show: state.showDiscount,
          modelValue: state.orderInfo
        }),
        V: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(state.orderInfo.price.payPrice)),
        W: common_vendor.o(onConfirm, "c3"),
        X: common_vendor.p({
          bottom: true,
          opacity: false,
          bg: "bg-white",
          placeholder: true,
          noFixed: false,
          index: 200
        }),
        Y: common_vendor.p({
          title: "确认订单"
        })
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-324e7894"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/confirm.js.map
