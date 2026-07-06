"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_s_coupon_list2 = common_vendor.resolveComponent("s-coupon-list");
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  (_easycom_s_coupon_list2 + _easycom_su_popup2)();
}
const _easycom_s_coupon_list = () => "../s-coupon-list/s-coupon-list.js";
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  (_easycom_s_coupon_list + _easycom_su_popup)();
}
const _sfc_main = {
  __name: "s-coupon-select",
  props: {
    modelValue: {
      // 优惠劵列表
      type: Object,
      default() {
      }
    },
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ["confirm", "close"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const state = common_vendor.reactive({
      couponInfo: common_vendor.computed(() => props.modelValue),
      // 优惠劵列表
      couponId: void 0
      // 选中的优惠劵编号
    });
    function radioChange(couponId) {
      if (state.couponId === couponId) {
        state.couponId = void 0;
      } else {
        state.couponId = couponId;
      }
    }
    const onConfirm = () => {
      emits("confirm", state.couponId);
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(state.couponInfo.filter((coupon) => coupon.match), (item, index, i0) => {
          return {
            a: state.couponId === item.id,
            b: common_vendor.o(($event) => radioChange(item.id), index),
            c: common_vendor.o(($event) => radioChange(item.id), index),
            d: "7362f63a-1-" + i0 + ",7362f63a-0",
            e: common_vendor.p({
              data: item,
              type: "user",
              disabled: false
            }),
            f: index
          };
        }),
        b: common_vendor.f(state.couponInfo.filter((coupon) => !coupon.match), (item, k0, i0) => {
          return {
            a: common_vendor.t(item.mismatchReason || "未达到使用门槛"),
            b: "7362f63a-2-" + i0 + ",7362f63a-0",
            c: common_vendor.p({
              data: item,
              type: "user",
              disabled: true
            }),
            d: item.id
          };
        }),
        c: common_vendor.o(onConfirm, "43"),
        d: common_vendor.o(($event) => emits("close"), "ce"),
        e: common_vendor.p({
          show: __props.show,
          type: "bottom",
          round: "20",
          showClose: true,
          backgroundColor: "#f2f2f2"
        })
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7362f63a"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-coupon-select/s-coupon-select.js.map
