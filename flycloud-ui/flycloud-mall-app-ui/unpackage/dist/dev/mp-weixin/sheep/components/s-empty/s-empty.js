"use strict";
const common_vendor = require("../../../common/vendor.js");
const sheep_index = require("../../index.js");
const _sfc_main = {
  __name: "s-empty",
  props: {
    // 图标
    icon: {
      type: String,
      default: ""
    },
    // 描述
    text: {
      type: String,
      default: ""
    },
    // 是否显示button
    showAction: {
      type: Boolean,
      default: false
    },
    // button 文字
    actionText: {
      type: String,
      default: ""
    },
    // 链接
    actionUrl: {
      type: String,
      default: ""
    },
    // 间距
    paddingTop: {
      type: String,
      default: "260"
    },
    //主题色
    buttonColor: {
      type: String,
      default: "var(--ui-BG-Main)"
    }
  },
  emits: ["clickAction"],
  setup(__props, { emit: __emit }) {
    common_vendor.useCssVars((_ctx) => ({
      "2d8eef07": __props.buttonColor
    }));
    const props = __props;
    const emits = __emit;
    function clickAction() {
      if (props.actionUrl !== "") {
        sheep_index.sheep.$router.go(props.actionUrl);
      }
      emits("clickAction");
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: __props.icon,
        b: __props.text !== ""
      }, __props.text !== "" ? {
        c: common_vendor.t(__props.text)
      } : {}, {
        d: __props.showAction
      }, __props.showAction ? {
        e: common_vendor.t(__props.actionText),
        f: common_vendor.o(clickAction, "a7")
      } : {}, {
        g: common_vendor.s({
          paddingTop: __props.paddingTop + "rpx"
        }),
        h: common_vendor.s(_ctx.__cssVars())
      });
    };
  }
};
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-5cc178a8"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/sheep/components/s-empty/s-empty.js.map
