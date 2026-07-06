"use strict";
const common_vendor = require("../../../common/vendor.js");
const __default__ = {
  name: "UiSwitch"
};
const _sfc_main = /* @__PURE__ */ Object.assign(__default__, {
  props: {
    modelValue: {
      type: [Boolean, Number],
      default: false
    },
    ui: {
      type: String,
      default: ""
    },
    bg: {
      type: String,
      default: "ui-BG-Main"
    },
    text: {
      type: String,
      default: ""
    },
    size: {
      type: String,
      default: "sm"
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  emits: ["update:modelValue"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const change = () => {
      emits("update:modelValue", !props.modelValue);
    };
    return (_ctx, _cache) => {
      return {
        a: common_vendor.n({
          "ui-switch-input-checked": props.modelValue
        }),
        b: common_vendor.n(props.modelValue ? props.bg : ""),
        c: common_vendor.n(props.text),
        d: common_vendor.n(props.size),
        e: common_vendor.o(change, "1f"),
        f: common_vendor.n({
          disabled: props.disabled
        }),
        g: common_vendor.n(props.ui)
      };
    };
  }
});
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8eb57d65"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-switch/su-switch.js.map
