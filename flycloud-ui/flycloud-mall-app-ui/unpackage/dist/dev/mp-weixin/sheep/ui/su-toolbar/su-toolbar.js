"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  name: "SuToolbar",
  props: {
    // 是否展示工具条
    show: {
      type: Boolean,
      default: true
    },
    // 取消按钮的文字
    cancelText: {
      type: String,
      default: "取消"
    },
    // 确认按钮的文字
    confirmText: {
      type: String,
      default: "确认"
    },
    // 取消按钮的颜色
    cancelColor: {
      type: String,
      default: "#909193"
    },
    // 确认按钮的颜色
    confirmColor: {
      type: String,
      default: "#3c9cff"
    },
    // 标题文字
    title: {
      type: String,
      default: ""
    }
  },
  methods: {
    // 点击取消按钮
    cancel() {
      this.$emit("cancel");
    },
    // 点击确定按钮
    confirm() {
      this.$emit("confirm");
    },
    // 阻止事件冒泡
    preventEvent(e) {
      e && typeof e.stopPropagation === "function" && e.stopPropagation();
    },
    // 空操作
    noop(e) {
      this.preventEvent(e);
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $props.show
  }, $props.show ? common_vendor.e({
    b: common_vendor.t($props.cancelText),
    c: common_vendor.o((...args) => $options.cancel && $options.cancel(...args), "f2"),
    d: $props.cancelColor,
    e: $props.title
  }, $props.title ? {
    f: common_vendor.t($props.title)
  } : {}, {
    g: common_vendor.t($props.confirmText),
    h: common_vendor.o((...args) => $options.confirm && $options.confirm(...args), "8a"),
    i: $props.confirmColor,
    j: common_vendor.o((...args) => $options.noop && $options.noop(...args), "31")
  }) : {});
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-2c641e39"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/ui/su-toolbar/su-toolbar.js.map
