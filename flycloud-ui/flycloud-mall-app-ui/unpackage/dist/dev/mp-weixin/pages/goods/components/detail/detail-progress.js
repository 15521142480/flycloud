"use strict";
const common_vendor = require("../../../../common/vendor.js");
const _sfc_main = {
  __name: "detail-progress",
  props: {
    percent: {
      type: Number,
      default: 0
    }
  },
  setup(__props) {
    return (_ctx, _cache) => {
      return {
        a: common_vendor.t(__props.percent),
        b: __props.percent < 10 ? "10%" : __props.percent + "%"
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-af274eda"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../../.sourcemap/mp-weixin/pages/goods/components/detail/detail-progress.js.map
