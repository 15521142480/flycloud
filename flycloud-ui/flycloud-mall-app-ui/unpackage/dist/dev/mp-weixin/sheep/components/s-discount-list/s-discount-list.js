"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Array) {
  const _easycom_su_popup2 = common_vendor.resolveComponent("su-popup");
  _easycom_su_popup2();
}
const _easycom_su_popup = () => "../../ui/su-popup/su-popup.js";
if (!Math) {
  _easycom_su_popup();
}
const _sfc_main = {
  __name: "s-discount-list",
  props: {
    promoInfo: {
      type: Array,
      default: () => []
    },
    goodsList: {
      type: Array,
      default: () => []
    },
    modelValue: {
      type: Object,
      default() {
      }
    },
    show: {
      type: Boolean,
      default: false
    }
  },
  emits: ["close"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const state = common_vendor.reactive({
      orderInfo: common_vendor.computed(() => props.modelValue)
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.f(state.orderInfo.promotions, (item, index, i0) => {
          return common_vendor.e({
            a: [1, 2, 3, 4, 5].includes(item.type)
          }, [1, 2, 3, 4, 5].includes(item.type) ? {
            b: common_vendor.t(item.description)
          } : {}, {
            c: index
          });
        }),
        b: common_vendor.o(($event) => emits("close"), "6e"),
        c: common_vendor.o(($event) => emits("close"), "ce"),
        d: common_vendor.p({
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
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8a1f672f"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-discount-list/s-discount-list.js.map
