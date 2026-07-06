"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  __name: "su-subline",
  props: {
    // 线条颜色
    lineColor: {
      type: String,
      default: "#000"
    },
    // 线条样式：'dotted', 'solid', 'double', 'dashed'
    borderType: {
      type: String,
      default: "dashed"
    },
    // 线条宽度
    lineWidth: {
      type: Number,
      default: 1
    },
    // 高度
    height: {
      type: [Number, String],
      default: "auto"
    },
    // 左右边距：none - 无边距，horizontal - 左右留边
    paddingType: {
      type: String,
      default: "none"
    }
  },
  setup(__props) {
    const props = __props;
    const elStyle = common_vendor.computed(() => {
      return {
        "border-top-width": `${props.lineWidth}px`,
        "border-top-color": props.lineColor,
        "border-top-style": props.borderType,
        margin: props.paddingType === "none" ? "0" : "0px 16px"
      };
    });
    return (_ctx, _cache) => {
      return {
        a: common_vendor.s(elStyle.value),
        b: `${__props.height}px`
      };
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-b2944841"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-subline/su-subline.js.map
