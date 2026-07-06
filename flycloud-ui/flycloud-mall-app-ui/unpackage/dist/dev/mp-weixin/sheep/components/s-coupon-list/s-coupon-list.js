"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_hooks_useGoods = require("../../hooks/useGoods.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-coupon-list",
  props: {
    data: {
      type: Object,
      default: {}
    },
    disabled: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: "coupon"
      // coupon 优惠劵模版；user 用户优惠劵
    }
  },
  setup(__props) {
    const isDisable = common_vendor.computed(() => {
      if (props.type === "coupon") {
        return false;
      }
      return props.disabled;
    });
    const props = __props;
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.t(__props.data.discountType === 1 ? "满减券" : "折扣券"),
        b: common_vendor.n(isDisable.value ? "disabled-bg-color" : "info-bg-color"),
        c: common_vendor.t(__props.data.name),
        d: common_vendor.n(isDisable.value ? "disabled-color" : "info-color"),
        e: __props.data.discountType === 1
      }, __props.data.discountType === 1 ? {} : {}, {
        f: common_vendor.t(__props.data.discountType === 1 ? common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.discountPrice) : __props.data.discountPercent / 10),
        g: __props.data.discountType === 2
      }, __props.data.discountType === 2 ? {} : {}, {
        h: common_vendor.n(isDisable.value ? "disabled-color" : "price-text"),
        i: __props.data.validityType === 2
      }, __props.data.validityType === 2 ? {
        j: common_vendor.t(__props.data.fixedEndTerm),
        k: common_vendor.n(isDisable.value ? "disabled-color" : "subtitle-color")
      } : {
        l: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(__props.data.validStartTime, "yyyy-mm-dd")),
        m: common_vendor.t(common_vendor.unref(sheep_index.sheep).$helper.timeFormat(__props.data.validEndTime, "yyyy-mm-dd")),
        n: common_vendor.n(isDisable.value ? "disabled-color" : "subtitle-color")
      }, {
        o: common_vendor.t(common_vendor.unref(sheep_hooks_useGoods.fen2yuan)(__props.data.usePrice)),
        p: common_vendor.n(isDisable.value ? "disabled-color" : "subtitle-color"),
        q: common_vendor.t(__props.data.description),
        r: __props.disabled ? "0.5" : "1"
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-81b7b3dc"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-coupon-list/s-coupon-list.js.map
