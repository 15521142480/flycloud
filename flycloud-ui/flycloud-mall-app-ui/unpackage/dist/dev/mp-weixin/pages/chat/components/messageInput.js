"use strict";
const common_vendor = require("../../../common/vendor.js");
if (!Math) {
  OptimizeInput();
}
const OptimizeInput = () => "./optimize-input.js";
const _sfc_main = {
  __name: "messageInput",
  props: {
    // 消息
    modelValue: {
      type: String,
      default: ""
    },
    // 工具模式
    toolsMode: {
      type: String,
      default: ""
    }
  },
  emits: ["update:modelValue", "onTools", "sendMessage"],
  setup(__props, { emit: __emit }) {
    const props = __props;
    const emits = __emit;
    const message = common_vendor.computed({
      get() {
        return props.modelValue;
      },
      set(newValue) {
        emits(`update:modelValue`, newValue);
      }
    });
    function onTools(mode) {
      emits("onTools", mode);
    }
    function sendMessage() {
      emits("sendMessage");
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(($event) => message.value = $event, "d4"),
        b: common_vendor.p({
          inputBorder: false,
          clearable: false,
          placeholder: "请输入你要咨询的问题",
          modelValue: message.value
        }),
        c: common_vendor.o(($event) => onTools("emoji"), "d1"),
        d: !message.value
      }, !message.value ? {
        e: __props.toolsMode === "tools" ? 1 : "",
        f: common_vendor.o(($event) => onTools("tools"), "01")
      } : {}, {
        g: message.value
      }, message.value ? common_vendor.e({
        h: _ctx.sending
      }, _ctx.sending ? {} : {}, {
        i: common_vendor.o(sendMessage, "71"),
        j: _ctx.isDisabled || _ctx.sending,
        k: _ctx.isDisabled || _ctx.sending ? 1 : ""
      }) : {});
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-ef1914c4"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/chat/components/messageInput.js.map
