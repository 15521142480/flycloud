"use strict";
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  __name: "detail-cell",
  props: {
    label: {
      type: String,
      default: ""
    },
    value: {
      type: String,
      default: ""
    }
  },
  emits: ["click"],
  setup(__props, { emit: __emit }) {
    const emits = __emit;
    const onClick = () => {
      emits("click");
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(__props.label),
        b: common_vendor.t(__props.value),
        c: common_vendor.o(onClick, "c4")
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-4e5c0150"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-cell.js.map
