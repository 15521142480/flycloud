"use strict";
const common_vendor = require("../../common/vendor.js");
const sheep_index = require("../../sheep/index.js");
const _sfc_main = {
  __name: "addressSelection",
  props: {
    modelValue: {
      type: Object,
      default() {
      }
    }
  },
  emits: ["update:modelValue"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const state = common_vendor.computed({
      get() {
        return new Proxy(props.modelValue, {
          set(obj, name, val) {
            emits("update:modelValue", {
              ...obj,
              [name]: val
            });
            return true;
          }
        });
      },
      set(val) {
        emits("update:modelValue", val);
      }
    });
    function onSelectAddress() {
      let emitName = "SELECT_ADDRESS";
      let addressPage = "/pages/user/address/list?type=select";
      if (state.value.deliveryType === 2) {
        emitName = "SELECT_PICK_UP_INFO";
        addressPage = "/pages/user/goods_details_store/index";
      }
      common_vendor.index.$once(emitName, (e) => {
        changeConsignee(e.addressInfo);
      });
      sheep_index.sheep.$router.go(addressPage);
    }
    async function changeConsignee(addressInfo = {}) {
      if (!common_vendor.isEmpty(addressInfo)) {
        if (state.value.deliveryType === 1) {
          state.value.addressInfo = addressInfo;
        }
        if (state.value.deliveryType === 2) {
          state.value.pickUpInfo = addressInfo;
        }
      }
    }
    const switchDeliveryType = (type) => {
      state.value.deliveryType = type;
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: state.value.isPickUp
      }, state.value.isPickUp ? {
        b: common_vendor.n(state.value.deliveryType === 1 ? "on" : "on2"),
        c: common_vendor.o(($event) => switchDeliveryType(1), "31")
      } : {}, {
        d: state.value.isPickUp
      }, state.value.isPickUp ? {
        e: common_vendor.n(state.value.deliveryType === 2 ? "on" : "on2"),
        f: common_vendor.o(($event) => switchDeliveryType(2), "26")
      } : {}, {
        g: state.value.deliveryType === 1
      }, state.value.deliveryType === 1 ? common_vendor.e({
        h: state.value.addressInfo.name
      }, state.value.addressInfo.name ? common_vendor.e({
        i: common_vendor.t(state.value.addressInfo.name),
        j: common_vendor.t(state.value.addressInfo.mobile),
        k: state.value.addressInfo.defaultStatus
      }, state.value.addressInfo.defaultStatus ? {} : {}, {
        l: common_vendor.t(state.value.addressInfo.areaName),
        m: common_vendor.t(state.value.addressInfo.detailAddress)
      }) : {}, {
        n: common_vendor.o(onSelectAddress, "86"),
        o: common_vendor.s(state.value.isPickUp ? "" : "border-top-left-radius: 14rpx;border-top-right-radius: 14rpx;")
      }) : {}, {
        p: state.value.deliveryType === 2
      }, state.value.deliveryType === 2 ? common_vendor.e({
        q: state.value.pickUpInfo.name
      }, state.value.pickUpInfo.name ? {
        r: common_vendor.t(state.value.pickUpInfo.name),
        s: common_vendor.t(state.value.pickUpInfo.phone),
        t: common_vendor.t(state.value.pickUpInfo.areaName),
        v: common_vendor.t(", " + state.value.pickUpInfo.detailAddress)
      } : {}, {
        w: common_vendor.o(onSelectAddress, "15")
      }) : {}, {
        x: common_vendor.unref(sheep_index.sheep).$url.static("/static/img/shop/line.png"),
        y: common_vendor.s(state.value.isPickUp ? "" : "padding-top:10rpx;")
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-94711674"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/addressSelection.js.map
