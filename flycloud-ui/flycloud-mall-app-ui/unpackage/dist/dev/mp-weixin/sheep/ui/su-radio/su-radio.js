"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  __name: "su-radio",
  props: {
    customStyle: {
      type: Object,
      default: () => ({})
    },
    ui: {
      type: String,
      default: "check"
      //check line
    },
    modelValue: {
      type: [String, Number, Boolean],
      default: false
    },
    disabled: {
      type: Boolean,
      default: false
    },
    bg: {
      type: String,
      default: "ui-BG-Main"
    },
    unbg: {
      type: String,
      default: "borderss"
    },
    src: {
      type: String,
      default: ""
    },
    label: {
      type: String,
      default: ""
    },
    labelStyle: {
      type: Object,
      default: () => ({})
    },
    none: {
      type: Boolean,
      default: false
    }
  },
  emits: ["change", "update:modelValue"],
  setup(__props, { emit: __emit }) {
    const state = common_vendor.reactive({
      currentValue: false
    });
    const emits = __emit;
    const props = __props;
    common_vendor.watchPostEffect(() => {
      state.currentValue = props.modelValue;
      emits("update:modelValue", state.currentValue);
    });
    const isChecked = common_vendor.computed(() => state.currentValue);
    const onRaido = () => {
      if (props.disabled)
        return;
      state.currentValue = !state.currentValue;
      emits("update:modelValue", state.currentValue);
      emits("change", {
        label: props.label,
        value: state.currentValue
      });
    };
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: !__props.none
      }, !__props.none ? {
        b: common_vendor.n(isChecked.value ? "cur " + __props.bg : __props.unbg),
        c: common_vendor.n(__props.src ? "radius" : "round")
      } : {}, {
        d: __props.src
      }, __props.src ? {
        e: __props.src
      } : {
        f: common_vendor.t(__props.label),
        g: common_vendor.s(__props.labelStyle)
      }, {
        h: __props.ui.includes("card")
      }, __props.ui.includes("card") ? {
        i: common_vendor.n(isChecked.value ? "cur " + __props.bg : "")
      } : {}, {
        j: common_vendor.o(onRaido, "bc"),
        k: common_vendor.n({
          disabled: __props.disabled
        }),
        l: common_vendor.n({
          img: __props.src
        }),
        m: common_vendor.n(__props.ui),
        n: common_vendor.s(__props.customStyle)
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c395529d"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-radio/su-radio.js.map
